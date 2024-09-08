import React, { useEffect, useState } from 'react';
import { Link, useLocation, useParams, useSearchParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { byteSize, getPaginationState, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getActivityById } from './activity.reducer';
import { getEntities, reset } from 'app/entities/activity-invite/activity-invite.reducer';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import InfiniteScroll from 'react-infinite-scroll-component';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export const ActivityDetail = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const { id } = useParams<'id'>();

  console.log(pageLocation.state); // Check what is being passed
  //const activityStatus = pageLocation.state?.isCurrent; // "current" or "past"

  const [searchParams] = useSearchParams();
  const activityStatus = searchParams.get('type') || 'unknown'; // Get the type from the query params
  console.log(activityStatus);

  useEffect(() => {
    dispatch(getActivityById(id));
  }, []);

  const activityEntity = useAppSelector(state => state.activity.entity);
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  // Grab Activity Invite entities
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
    <>
      <Row>
        <Col md="8">
          <h2 data-cy="activityDetailsHeading">Activity</h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="id">ID</span>
            </dt>
            <dd>{activityEntity.id}</dd>
            <dt>
              <span id="activityName">Activity Name</span>
            </dt>
            <dd>{activityEntity.activityName}</dd>
            <dt>
              <span id="activityTime">Activity Time</span>
            </dt>
            <dd>
              {activityEntity.activityTime ? <TextFormat value={activityEntity.activityTime} type="date" format={APP_DATE_FORMAT} /> : null}
            </dd>
            <dt>
              <span id="duration">Duration</span>
            </dt>
            <dd>{activityEntity.duration}</dd>
            <dt>
              <span id="venue">Venue</span>
            </dt>
            <dd>{activityEntity.venue}</dd>
            <dt>
              <span id="details">Details</span>
            </dt>
            <dd>{activityEntity.details}</dd>
            <dt>Creator Profile</dt>
            <dd>{activityEntity.creatorProfile ? activityEntity.creatorProfile.id : ''}</dd>
            <dt>Skill</dt>
            <dd>{activityEntity.skill ? activityEntity.skill.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/activity" replace color="info" data-cy="entityDetailsBackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          {activityStatus == 'current' ? (
            <Button tag={Link} to={`/activity/${activityEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
            </Button>
          ) : (
            ''
          )}
        </Col>
      </Row>
      <Row className="p-5">
        <div className="activity-invite-segment border border-2 border-black p-3">
          <h4 id="activity-invite-heading" data-cy="ActivityInviteHeading">
            Activity Invites
            <div className="d-flex justify-content-end">
              <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
              </Button>
              <Link
                to="/activity-invite/new"
                className="btn btn-primary jh-create-entity"
                id="jh-create-entity"
                data-cy="entityCreateButton"
              >
                <FontAwesomeIcon icon="plus" />
                &nbsp; Create a new Activity Invite
              </Link>
            </div>
          </h4>
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
                          {activityInvite.status ? (
                            <Link to={`/code-tables/${activityInvite.status.id}`}>{activityInvite.status.id}</Link>
                          ) : (
                            ''
                          )}
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
      </Row>
    </>
  );
};

export default ActivityDetail;
