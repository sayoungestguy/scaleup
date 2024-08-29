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

import { getEntities, reset } from './user-profile.reducer';

export const UserProfile = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const userProfileList = useAppSelector(state => state.userProfile.entities);
  const loading = useAppSelector(state => state.userProfile.loading);
  const links = useAppSelector(state => state.userProfile.links);
  const updateSuccess = useAppSelector(state => state.userProfile.updateSuccess);

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
      <h2 id="user-profile-heading" data-cy="UserProfileHeading">
        User Profiles
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/user-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new User Profile
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={userProfileList ? userProfileList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {userProfileList && userProfileList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('nickname')}>
                    Nickname <FontAwesomeIcon icon={getSortIconByFieldName('nickname')} />
                  </th>
                  <th className="hand" onClick={sort('jobRole')}>
                    Job Role <FontAwesomeIcon icon={getSortIconByFieldName('jobRole')} />
                  </th>
                  <th className="hand" onClick={sort('aboutMe')}>
                    About Me <FontAwesomeIcon icon={getSortIconByFieldName('aboutMe')} />
                  </th>
                  <th className="hand" onClick={sort('profilePicture')}>
                    Profile Picture <FontAwesomeIcon icon={getSortIconByFieldName('profilePicture')} />
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
                    User <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {userProfileList.map((userProfile, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/user-profile/${userProfile.id}`} color="link" size="sm">
                        {userProfile.id}
                      </Button>
                    </td>
                    <td>{userProfile.nickname}</td>
                    <td>{userProfile.jobRole}</td>
                    <td>{userProfile.aboutMe}</td>
                    <td>{userProfile.profilePicture}</td>
                    <td>{userProfile.createdBy}</td>
                    <td>
                      {userProfile.createdDate ? <TextFormat type="date" value={userProfile.createdDate} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{userProfile.lastModifiedBy}</td>
                    <td>
                      {userProfile.lastModifiedDate ? (
                        <TextFormat type="date" value={userProfile.lastModifiedDate} format={APP_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>{userProfile.user ? userProfile.user.login : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/user-profile/${userProfile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`/user-profile/${userProfile.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/user-profile/${userProfile.id}/delete`)}
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
            !loading && <div className="alert alert-warning">No User Profiles found</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default UserProfile;
