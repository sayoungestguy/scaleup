import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './activity-invite.reducer';

export const ActivityInvite = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const activityInviteList = useAppSelector(state => state.activityInvite.entities);
  const loading = useAppSelector(state => state.activityInvite.loading);
  const links = useAppSelector(state => state.activityInvite.links);
  const updateSuccess = useAppSelector(state => state.activityInvite.updateSuccess);

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
    resetAll();
  }, []);

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
      <h2 id="activity-invite-heading" data-cy="ActivityInviteHeading">
        Activity Invites
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/activity-invite/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Activity Invite
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={activityInviteList ? activityInviteList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {activityInviteList && activityInviteList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('willParticipate')}>
                    Will Participate <FontAwesomeIcon icon={getSortIconByFieldName('willParticipate')} />
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
                    Activity <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Invitee Profile <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Status <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {activityInviteList.map((activityInvite, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/activity-invite/${activityInvite.id}`} color="link" size="sm">
                        {activityInvite.id}
                      </Button>
                    </td>
                    <td>{activityInvite.willParticipate ? 'true' : 'false'}</td>
                    <td>{activityInvite.createdBy}</td>
                    <td>
                      {activityInvite.createdDate ? (
                        <TextFormat type="date" value={activityInvite.createdDate} format={APP_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>{activityInvite.lastModifiedBy}</td>
                    <td>
                      {activityInvite.lastModifiedDate ? (
                        <TextFormat type="date" value={activityInvite.lastModifiedDate} format={APP_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {activityInvite.activity ? (
                        <Link to={`/activity/${activityInvite.activity.id}`}>{activityInvite.activity.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {activityInvite.inviteeProfile ? (
                        <Link to={`/user-profile/${activityInvite.inviteeProfile.id}`}>{activityInvite.inviteeProfile.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {activityInvite.status ? <Link to={`/code-tables/${activityInvite.status.id}`}>{activityInvite.status.id}</Link> : ''}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`/activity-invite/${activityInvite.id}`}
                          color="info"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/activity-invite/${activityInvite.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/activity-invite/${activityInvite.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
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
            !loading && <div className="alert alert-warning">No Activity Invites found</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default ActivityInvite;
