import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './activity.reducer';

export const Activity = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const activityList = useAppSelector(state => state.activity.entities);
  const loading = useAppSelector(state => state.activity.loading);
  const links = useAppSelector(state => state.activity.links);
  const updateSuccess = useAppSelector(state => state.activity.updateSuccess);

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
      <h2 id="activity-heading" data-cy="ActivityHeading">
        Activities
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/activity/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Activity
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={activityList ? activityList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {activityList && activityList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('activityTime')}>
                    Activity Time <FontAwesomeIcon icon={getSortIconByFieldName('activityTime')} />
                  </th>
                  <th className="hand" onClick={sort('duration')}>
                    Duration <FontAwesomeIcon icon={getSortIconByFieldName('duration')} />
                  </th>
                  <th className="hand" onClick={sort('venue')}>
                    Venue <FontAwesomeIcon icon={getSortIconByFieldName('venue')} />
                  </th>
                  <th className="hand" onClick={sort('details')}>
                    Details <FontAwesomeIcon icon={getSortIconByFieldName('details')} />
                  </th>
                  {/*<th className="hand" onClick={sort('createdBy')}>*/}
                  {/*  Created By <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />*/}
                  {/*</th>*/}
                  {/*<th className="hand" onClick={sort('createdDate')}>*/}
                  {/*  Created Date <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />*/}
                  {/*</th>*/}
                  {/*<th className="hand" onClick={sort('lastModifiedBy')}>*/}
                  {/*  Last Modified By <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />*/}
                  {/*</th>*/}
                  {/*<th className="hand" onClick={sort('lastModifiedDate')}>*/}
                  {/*  Last Modified Date <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />*/}
                  {/*</th>*/}
                  {/*<th>*/}
                  {/*  Creator Profile <FontAwesomeIcon icon="sort" />*/}
                  {/*</th>*/}
                  <th>
                    Skill <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {activityList.map((activity, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/activity/${activity.id}`} color="link" size="sm">
                        {activity.id}
                      </Button>
                    </td>
                    <td>
                      {activity.activityTime ? <TextFormat type="date" value={activity.activityTime} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{activity.duration}</td>
                    <td>{activity.venue}</td>
                    <td>{activity.details}</td>
                    {/*<td>{activity.createdBy}</td>*/}
                    {/*<td>*/}
                    {/*  {activity.createdDate ? <TextFormat type="date" value={activity.createdDate} format={APP_DATE_FORMAT} /> : null}*/}
                    {/*</td>*/}
                    {/*<td>{activity.lastModifiedBy}</td>*/}
                    {/*<td>*/}
                    {/*  {activity.lastModifiedDate ? (*/}
                    {/*    <TextFormat type="date" value={activity.lastModifiedDate} format={APP_DATE_FORMAT} />*/}
                    {/*  ) : null}*/}
                    {/*</td>*/}
                    {/*<td>*/}
                    {/*  {activity.creatorProfile ? (*/}
                    {/*    <Link to={`/user-profile/${activity.creatorProfile.id}`}>{activity.creatorProfile.id}</Link>*/}
                    {/*  ) : (*/}
                    {/*    ''*/}
                    {/*  )}*/}
                    {/*</td>*/}
                    <td>{activity.skill ? <Link to={`/skill/${activity.skill.id}`}>{activity.skill.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/activity/${activity.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`/activity/${activity.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/activity/${activity.id}/delete`)}
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
            !loading && <div className="alert alert-warning">No Activities found</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Activity;
