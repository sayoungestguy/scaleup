import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Card, CardBody, Table } from 'reactstrap';
import { getPaginationState, JhiItemCount, JhiPagination } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getAllActivityInvites } from './activity-invite.reducer';
import { getActivityById } from 'app/entities/activity/activity.reducer';
import { getAllUserProfiles, getUserProfileById } from 'app/entities/user-profile/user-profile.reducer';
import { getCodeTableById } from 'app/entities/code-tables/code-tables.reducer';

export const ActivityInvite = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const activityInviteList = useAppSelector(state => state.activityInvite.entities);
  const loading = useAppSelector(state => state.activityInvite.loading);
  const totalItems = useAppSelector(state => state.activityInvite.totalItems);

  // Assuming the user's roles are stored in the authentication state
  const currentUser = useAppSelector(state => state.authentication.account);

  // Check if the current user has the "admin" role
  const isAdmin = currentUser?.authorities?.includes('ROLE_ADMIN');

  const [activityNames, setActivityNames] = useState<{ [key: number]: string }>({});
  const [inviteeProfileNames, setInviteeProfileNames] = useState<{ [key: number]: string }>({});
  const [statusNames, setStatusNames] = useState<{ [key: number]: string }>({});

  const getAllEntities = () => {
    dispatch(
      getAllActivityInvites({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  // Filter the activityInviteList on the client-side
  const filteredActivityInviteList = activityInviteList.filter(
    invite => invite.inviteeProfile.id === currentUser.id || invite.createdBy === currentUser.login,
  );

  // Fetch Activity Name
  const fetchActivityName = async (activityId: number) => {
    const response = await dispatch(getActivityById(activityId)).unwrap();
    setActivityNames(prev => ({ ...prev, [activityId]: response.data.activityName }));
  };

  // Fetch Invitee Profile Name
  const fetchInviteeProfileName = async (profileId: number) => {
    const response = await dispatch(getUserProfileById(profileId)).unwrap();

    setInviteeProfileNames(prev => ({ ...prev, [profileId]: response.data.nickname }));
  };

  // Fetch Status Name
  const fetchStatusName = async (statusId: number) => {
    const response = await dispatch(getCodeTableById(statusId)).unwrap();
    setStatusNames(prev => ({ ...prev, [statusId]: response.data.codeValue }));
  };

  useEffect(() => {
    // Check if User Profile is existent else navigate to profile page
    const userProfileExist = async () => {
      const response = await dispatch(
        getAllUserProfiles({
          query: `createdBy.equals=${currentUser.login}`,
        }),
      ).unwrap();
      return response.data.length > 0;
    };

    // Run the async function to check profile existence and handle redirect
    const validateUserProfile = async () => {
      const profileExists = await userProfileExist();
      if (!profileExists) {
        navigate('/user-profile/new');
      }
    };

    validateUserProfile();
  }, []);

  // UseEffect to fetch Invitee Profile names after activityInviteList is updated
  useEffect(() => {
    if (filteredActivityInviteList.length > 0) {
      filteredActivityInviteList.forEach(invite => {
        if (invite.activity.id && !activityNames[invite.activity.id]) {
          fetchActivityName(invite.activity.id);
        }
        if (invite.inviteeProfile.id && !inviteeProfileNames[invite.inviteeProfile.id]) {
          fetchInviteeProfileName(invite.inviteeProfile.id);
        }
        if (invite.status.id && !statusNames[invite.status.id]) {
          fetchStatusName(invite.status.id);
        }
      });
    }
  }, [filteredActivityInviteList]);

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
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
      </h2>
      <Card className="p-2 m-2">
        <CardBody>
          <p>*Use this page to track all your activity invites, both created and invited~</p>
        </CardBody>
      </Card>
      <div className="d-flex justify-content-end">
        <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
          <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
        </Button>
        <Link to="/activity-invite/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create a new Activity Invite
        </Link>
      </div>

      <div className="table-responsive">
        {filteredActivityInviteList && filteredActivityInviteList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>S/No.</th>
                <th className="hand" onClick={sort('activity')}>
                  Activity <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('inviteeProfile')}>
                  Invitee Profile <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filteredActivityInviteList.map((activityInvite, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{i + 1}</td>
                  <td>
                    {activityInvite.activity ? (
                      <Link to={`/activity/${activityInvite.activity.id}`}>{activityNames[activityInvite.activity.id]}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {activityInvite.inviteeProfile ? (
                      <Link to={`/user-profile/${activityInvite.inviteeProfile.id}`}>
                        {inviteeProfileNames[activityInvite.inviteeProfile.id]}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{activityInvite.status ? statusNames[activityInvite.status.id] : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {isAdmin ? (
                        <Button
                          tag={Link}
                          to={`/activity-invite/${activityInvite.id}`}
                          color="info"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                      ) : (
                        ''
                      )}
                      <Button
                        tag={Link}
                        to={`/activity-invite/${activityInvite.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      {
                        <Button
                          onClick={() =>
                            (window.location.href = `/activity-invite/${activityInvite.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                          }
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      }
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Activity Invites found</div>
        )}
      </div>
      {totalItems ? (
        <div className={activityInviteList && activityInviteList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default ActivityInvite;
