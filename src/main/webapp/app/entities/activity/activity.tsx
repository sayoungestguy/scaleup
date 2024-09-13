import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
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
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  // Separate pagination states for current and past activities
  const [currentPaginationState, setCurrentPaginationState] = useState({
    activePage: 1,
    itemsPerPage: 5,
    sort: 'activityTime',
    order: ASC,
  });
  const [pastPaginationState, setPastPaginationState] = useState({
    activePage: 1,
    itemsPerPage: 5,
    sort: 'activityTime',
    order: ASC,
  });

  const [sorting, setSorting] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = ITEMS_PER_PAGE; // Limit to 5 items per page

  const activityList = useAppSelector(state => state.activity.entities);
  const loading = useAppSelector(state => state.activity.loading);
  const totalItems = useAppSelector(state => state.activity.totalItems);

  const [currentActivities, setCurrentActivities] = useState([]);
  const [pastActivities, setPastActivities] = useState([]);

  const [currentSkills, setCurrentSkills] = React.useState<{ [key: number]: string }>({});
  const [pastSkills, setPastSkills] = React.useState<{ [key: number]: string }>({});

  // Get activities for the current page
  // const currentPageActivities = currentActivities.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);
  // const pastPageActivities = pastActivities.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

  const getAllActivities = () => {
    dispatch(
      getAllActivity({
        sort: `${currentPaginationState.sort},${currentPaginationState.order}`,
      }),
    );
  };

  // Sort activities
  const sortEntities = () => {
    getAllActivities();
    const endURL = `?page=${currentPaginationState.activePage}&sort=${currentPaginationState.sort},${currentPaginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };
  // Fetch skill names for the activities
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

  // Paginate current and past activities
  const indexOfLastCurrentActivity = currentPaginationState.activePage * currentPaginationState.itemsPerPage;
  const indexOfFirstCurrentActivity = indexOfLastCurrentActivity - currentPaginationState.itemsPerPage;
  const currentActivitiesPaginated = currentActivities.slice(indexOfFirstCurrentActivity, indexOfLastCurrentActivity);

  const indexOfLastPastActivity = pastPaginationState.activePage * pastPaginationState.itemsPerPage;
  const indexOfFirstPastActivity = indexOfLastPastActivity - pastPaginationState.itemsPerPage;
  const pastActivitiesPaginated = pastActivities.slice(indexOfFirstPastActivity, indexOfLastPastActivity);

  // Handle pagination for current activities
  const handlePaginationCurrentActivities = currentPage => {
    setCurrentPaginationState({
      ...currentPaginationState,
      activePage: currentPage, // Update only the current activities page
    });
  };

  // Handle pagination for past activities
  const handlePaginationPastActivities = currentPage => {
    setPastPaginationState({
      ...pastPaginationState,
      activePage: currentPage, // Update only the past activities page
    });
  };

  useEffect(() => {
    sortEntities();
  }, [currentPaginationState.activePage, currentPaginationState.order, currentPaginationState.sort]);

  // useEffect(() => {
  //   const params = new URLSearchParams(pageLocation.search);
  //   const page = params.get('page');
  //   const sort = params.get(SORT);
  //   if (page && sort) {
  //     const sortSplit = sort.split(',');
  //     setPaginationState({
  //       ...paginationState,
  //       activePage: +page,
  //       sort: sortSplit[0],
  //       order: sortSplit[1],
  //     });
  //   }
  // }, [pageLocation.search]);

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
          {currentActivitiesPaginated.length > 0 ? (
            <Table responsive bordered>
              <thead>
                <tr>
                  <th>S/No.</th>
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
                {currentActivitiesPaginated.map((activity, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/activity/${activity.id}`} color="link" size="sm">
                        {(currentPaginationState.activePage - 1) * currentPaginationState.itemsPerPage + i + 1}
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
                        <Button
                          tag={Link}
                          to={`/activity/${activity.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          onClick={() =>
                            (window.location.href = `/activity/${activity.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                          }
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
        </div>

        {currentActivities ? (
          <div className={currentActivities && currentActivities.length > 0 ? '' : 'd-none'}>
            <div className="justify-content-center d-flex">
              <JhiItemCount
                page={currentPaginationState.activePage}
                total={currentActivities.length}
                itemsPerPage={currentPaginationState.itemsPerPage}
              />
            </div>
            <div className="justify-content-center d-flex">
              <JhiPagination
                activePage={currentPaginationState.activePage}
                onSelect={handlePaginationCurrentActivities} // Independent pagination handler for current activities
                itemsPerPage={currentPaginationState.itemsPerPage}
                maxButtons={5}
                totalItems={currentActivities.length} // Total number of current activities
              />
            </div>
          </div>
        ) : (
          ''
        )}
      </div>
      <div className="past-activities border border-5 p-3 m-2">
        <h3>Past Activities</h3>
        <div className="table-responsive">
          {pastActivitiesPaginated.length > 0 ? (
            <Table responsive bordered>
              <thead>
                <tr>
                  <th>S/No.</th>
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
                {pastActivitiesPaginated.map((activity, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/activity/${activity.id}`} color="link" size="sm">
                        {(pastPaginationState.activePage - 1) * pastPaginationState.itemsPerPage + i + 1}
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
                        {/*<Button*/}
                        {/*  tag={Link}*/}
                        {/*  to={`/activity/${activity.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}*/}
                        {/*  color="primary"*/}
                        {/*  size="sm"*/}
                        {/*  data-cy="entityEditButton"*/}
                        {/*>*/}
                        {/*  <FontAwesomeIcon icon="pencil-alt"/> <span className="d-none d-md-inline">Edit</span>*/}
                        {/*</Button>*/}
                        {/*<Button*/}
                        {/*  onClick={() =>*/}
                        {/*    (window.location.href = `/activity/${activity.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)*/}
                        {/*  }*/}
                        {/*  color="danger"*/}
                        {/*  size="sm"*/}
                        {/*  data-cy="entityDeleteButton"*/}
                        {/*>*/}
                        {/*  <FontAwesomeIcon icon="trash"/> <span className="d-none d-md-inline">Delete</span>*/}
                        {/*</Button>*/}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && <div className="alert alert-warning">No Activities found</div>
          )}
        </div>

        {pastActivities ? (
          <div className={pastActivities && pastActivities.length > 0 ? '' : 'd-none'}>
            <div className="justify-content-center d-flex">
              <JhiItemCount
                page={pastPaginationState.activePage}
                total={pastActivities.length}
                itemsPerPage={pastPaginationState.itemsPerPage}
              />
            </div>
            <div className="justify-content-center d-flex">
              <JhiPagination
                activePage={pastPaginationState.activePage}
                onSelect={handlePaginationPastActivities} // Independent pagination handler for past activities
                itemsPerPage={pastPaginationState.itemsPerPage}
                maxButtons={5}
                totalItems={pastActivities.length} // Total number of past activities
              />
            </div>
          </div>
        ) : (
          ''
        )}
      </div>
    </div>
  );
};
export default Activity;
