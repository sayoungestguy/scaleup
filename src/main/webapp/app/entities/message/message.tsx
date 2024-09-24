import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities, reset } from './message.reducer';
import axios from 'axios';

export const Message = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);
  const [currentUserId, setCurrentUserId] = useState(null);

  const messageList = useAppSelector(state => state.message.entities);
  const loading = useAppSelector(state => state.message.loading);
  const links = useAppSelector(state => state.message.links);
  const updateSuccess = useAppSelector(state => state.message.updateSuccess);

  // Fetch current user ID
  useEffect(() => {
    const fetchCurrentUserId = async () => {
      try {
        const response = await axios.get('/api/current-user');
        setCurrentUserId(response.data);
      } catch (error) {
        console.error('Error fetching current user ID:', error);
      }
    };

    fetchCurrentUserId();
    resetAll();
  }, []);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="message-heading" data-cy="MessageHeading">
        Messages
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/message/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Message
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={messageList ? messageList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {messageList && messageList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('content')}>
                    Content <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
                  </th>
                  <th className="hand" onClick={sort('sentAt')}>
                    Sent At <FontAwesomeIcon icon={getSortIconByFieldName('sentAt')} />
                  </th>
                  <th className="hand" onClick={sort('isDeleted')}>
                    Is Deleted <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                  </th>
                  <th className="hand" onClick={sort('createdBy')}>
                    Created By <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                  </th>
                  <th className="hand" onClick={sort('createdDate')}>
                    Created Date <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                  </th>
                  <th className="hand" onClick={sort('lastModifiedBy')}>
                    Last Modified By <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                  </th>
                  <th className="hand" onClick={sort('lastModifiedDate')}>
                    Last Modified Date <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                  </th>
                  <th>
                    Sender Profile <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Receiver Profile <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {messageList.map((message, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/message/${message.id}`} color="link" size="sm">
                        {message.id}
                      </Button>
                    </td>
                    <td>{message.content}</td>
                    <td>{message.sentAt ? <TextFormat type="date" value={message.sentAt} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{message.isDeleted ? 'true' : 'false'}</td>
                    <td>{message.createdBy}</td>
                    <td>{message.createdDate ? <TextFormat type="date" value={message.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{message.lastModifiedBy}</td>
                    <td>
                      {message.lastModifiedDate ? (
                        <TextFormat type="date" value={message.lastModifiedDate} format={APP_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {message.senderProfile ? (
                        <Link to={`/user-profile/${message.senderProfile.id}`}>{message.senderProfile.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {message.receiverProfile ? (
                        <Link to={`/user-profile/${message.receiverProfile.id}`}>{message.receiverProfile.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/message/${message.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        {/* Conditionally disable Edit and Delete buttons */}
                        <Button
                          tag={Link}
                          to={`/message/${message.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                          disabled={currentUserId === message.receiverProfile?.id} // Disable if current user is receiver
                        >
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/message/${message.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                          disabled={currentUserId === message.receiverProfile?.id} // Disable if current user is receiver
                        >
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && <div className="alert alert-warning">No Messages found</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Message;
