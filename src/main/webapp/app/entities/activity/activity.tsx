import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Pagination, PaginationItem, PaginationLink, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getAllActivity, reset } from './activity.reducer';
import { getSkillById } from 'app/entities/skill/skill.reducer';

export const Activity = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = ITEMS_PER_PAGE; // Limit to 5 items per page

  const activityList = useAppSelector(state => state.activity.entities);
  const loading = useAppSelector(state => state.activity.loading);
  const links = useAppSelector(state => state.activity.links);
  const updateSuccess = useAppSelector(state => state.activity.updateSuccess);

  const [currentActivities, setCurrentActivities] = useState([]);
  const [pastActivities, setPastActivities] = useState([]);

  const [currentSkills, setCurrentSkills] = React.useState<{ [key: number]: string }>({});
  const [pastSkills, setPastSkills] = React.useState<{ [key: number]: string }>({});

  // Get activities for the current page
  const currentPageActivities = currentActivities.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);
  const pastPageActivities = pastActivities.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

  const getAllActivities = () => {
    dispatch(
      getAllActivity({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const fetchSkill = async (skillId: number, isCurrent: boolean) => {
    const response = await dispatch(getSkillById(skillId)).unwrap();
    const skillName = response.data.skillName;

    if (isCurrent) {
      setCurrentSkills(prevSkills => ({
        ...prevSkills,
        [skillId]: skillName,
      }));
    } else {
      setPastSkills(prevSkills => ({
        ...prevSkills,
        [skillId]: skillName,
      }));
    }

    return skillName;
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getAllActivity({}));
    console.log('(2)', activityList);
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
    getAllActivities();
    console.log('(3)', activityList);
  }, [paginationState.activePage]);

  useEffect(() => {
    if (activityList && activityList.length > 0) {
      const current = activityList.filter(
        (activity: { activityTime: string | number | Date }) => new Date(activity.activityTime) >= new Date(),
      );
      const past = activityList.filter(
        (activity: { activityTime: string | number | Date }) => new Date(activity.activityTime) < new Date(),
      );

      setCurrentActivities(current);
      setPastActivities(past);
    }
  }, [activityList]);

  useEffect(() => {
    if (currentActivities.length > 0) {
      currentActivities.forEach(async activity => {
        if (activity.skill?.id && !currentSkills[activity.skill.id]) {
          await fetchSkill(activity.skill.id, true);
        }
      });
    }

    if (pastActivities.length > 0) {
      pastActivities.forEach(async activity => {
        if (activity.skill?.id && !pastSkills[activity.skill.id]) {
          await fetchSkill(activity.skill.id, false);
        }
      });
    }
  }, [currentActivities, pastActivities]);

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
      getAllActivities();
      console.log('(4)', activityList);
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
        </div>
      </h2>
      <div className="current-activities border border-5 p-3 m-2">
        <h3>
          Current Activities
          <div className="d-flex justify-content-end">
            <Link to="/activity/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp; New Activity
            </Link>
          </div>
        </h3>
        <div className="table-responsive">
          {/*<InfiniteScroll*/}
          {/*  dataLength={currentActivities ? 5 : 0}*/}
          {/*  next={handleLoadMore}*/}
          {/*  hasMore={paginationState.activePage - 1 < links.next}*/}
          {/*  loader={<div className="loader">Loading ...</div>}*/}
          {/*>*/}
          {currentPageActivities.length > 0 ? (
            <Table responsive bordered>
              <thead>
                <tr>
                  <th>S/No</th>
                  <th className="hand" onClick={sort('activityName')}>
                    Activity Name <FontAwesomeIcon icon={getSortIconByFieldName('activityName')} />
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
                  <th>
                    Skill <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {currentPageActivities.map((activity, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/activity/${activity.id}`} color="link" size="sm">
                        {(currentPage - 1) * itemsPerPage + i + 1}
                      </Button>
                    </td>
                    <td>{activity.activityName}</td>
                    <td>
                      {activity.activityTime ? <TextFormat type="date" value={activity.activityTime} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{activity.duration}</td>
                    <td>{activity.venue}</td>
                    <td>{activity.details}</td>
                    <td>
                      {activity.skill ? (
                        <Link to={`/skill/${activity.skill.id}`}>{currentSkills[activity.skill.id] || 'Loading...'}</Link>
                      ) : (
                        ''
                      )}
                    </td>
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
          {/*</InfiniteScroll>*/}
        </div>
        <Pagination className="justify-content-center d-flex">
          <PaginationItem disabled={currentPage === 1}>
            <PaginationLink previous onClick={() => setCurrentPage(currentPage > 1 ? currentPage - 1 : 1)}></PaginationLink>
          </PaginationItem>

          {[...Array(Math.ceil(currentActivities.length / itemsPerPage)).keys()].map(pageNumber => (
            <PaginationItem key={pageNumber} active={pageNumber + 1 === currentPage}>
              <PaginationLink onClick={() => setCurrentPage(pageNumber + 1)}>{pageNumber + 1}</PaginationLink>
            </PaginationItem>
          ))}

          <PaginationItem disabled={currentPage === Math.ceil(currentActivities.length / itemsPerPage)}>
            <PaginationLink
              next
              onClick={() =>
                setCurrentPage(currentPage < Math.ceil(currentActivities.length / itemsPerPage) ? currentPage + 1 : currentPage)
              }
            ></PaginationLink>
          </PaginationItem>
        </Pagination>
      </div>
      <div className="past-activities border border-5 p-3 mx-2 my-5">
        <h3>Past Activities</h3>
        <div className="table-responsive">
          {/*<InfiniteScroll*/}
          {/*  dataLength={pastActivities ? pastActivities.length : 0}*/}
          {/*  next={handleLoadMore}*/}
          {/*  hasMore={paginationState.activePage - 1 < links.next}*/}
          {/*  loader={<div className="loader">Loading ...</div>}*/}
          {/*>*/}
          {pastPageActivities.length > 0 ? (
            <Table responsive bordered>
              <thead>
                <tr>
                  <th>S/No</th>
                  <th className="hand" onClick={sort('activityName')}>
                    Activity Name <FontAwesomeIcon icon={getSortIconByFieldName('activityName')} />
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
                  <th>
                    Skill <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {pastPageActivities.map((activity, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/activity/${activity.id}`} color="link" size="sm">
                        {(currentPage - 1) * itemsPerPage + i + 1}
                      </Button>
                    </td>
                    <td>{activity.activityName}</td>
                    <td>
                      {activity.activityTime ? <TextFormat type="date" value={activity.activityTime} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{activity.duration}</td>
                    <td>{activity.venue}</td>
                    <td>{activity.details}</td>
                    <td>
                      {activity.skill ? (
                        <Link to={`/skill/${activity.skill.id}`}>{pastSkills[activity.skill.id] || 'Loading...'}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/activity/${activity.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
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
          {/*</InfiniteScroll>*/}
        </div>
        <Pagination className="justify-content-center d-flex">
          <PaginationItem disabled={currentPage === 1}>
            <PaginationLink previous onClick={() => setCurrentPage(currentPage > 1 ? currentPage - 1 : 1)}></PaginationLink>
          </PaginationItem>

          {[...Array(Math.ceil(pastActivities.length / itemsPerPage)).keys()].map(pageNumber => (
            <PaginationItem key={pageNumber} active={pageNumber + 1 === currentPage}>
              <PaginationLink onClick={() => setCurrentPage(pageNumber + 1)}>{pageNumber + 1}</PaginationLink>
            </PaginationItem>
          ))}

          <PaginationItem disabled={currentPage === Math.ceil(pastActivities.length / itemsPerPage)}>
            <PaginationLink
              next
              onClick={() => setCurrentPage(currentPage < Math.ceil(pastActivities.length / itemsPerPage) ? currentPage + 1 : currentPage)}
            ></PaginationLink>
          </PaginationItem>
        </Pagination>
      </div>
    </div>
  );
};

export default Activity;
