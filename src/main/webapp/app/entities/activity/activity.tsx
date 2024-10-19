import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
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

  const activityList = useAppSelector(state => state.activity.entities);
  const loading = useAppSelector(state => state.activity.loading);
  // Assuming the user's roles are stored in the authentication state
  const currentUser = useAppSelector(state => state.authentication.account);

  // Check if the current user has the "admin" role
  const isAdmin = currentUser?.authorities?.includes('ROLE_ADMIN');

  const [currentActivities, setCurrentActivities] = useState([]);
  const [pastActivities, setPastActivities] = useState([]);

  const [currentSkills, setCurrentSkills] = React.useState<{ [key: number]: string }>({});
  const [pastSkills, setPastSkills] = React.useState<{ [key: number]: string }>({});

  const getAllActivities = () => {
    dispatch(
      getAllActivity(
        isAdmin
          ? { sort: `${currentPaginationState.sort},${currentPaginationState.order}` }
          : {
              sort: `${currentPaginationState.sort},${currentPaginationState.order}`,
              query: `creatorProfileId.equals=${currentUser.id.toString()}`,
            },
      ),
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

  // Filter
  const [searchQuery, setSearchQuery] = useState(''); // To hold the search input value

  const filterActivities = activities => {
    return activities.filter(activity => {
      const activityNameMatch = activity.activityName?.toLowerCase().includes(searchQuery.toLowerCase());
      const activityVenueMatch = activity.venue?.toLowerCase().includes(searchQuery.toLowerCase());

      return activityNameMatch || activityVenueMatch; // Return true if any field matches
    });
  };

  // Filtered activities
  const filteredCurrentActivities = filterActivities(currentActivities);
  const filteredPastActivities = filterActivities(pastActivities);

  // Paginate current and past activities
  const currentActivitiesPaginated = filteredCurrentActivities.slice(
    (currentPaginationState.activePage - 1) * currentPaginationState.itemsPerPage,
    currentPaginationState.activePage * currentPaginationState.itemsPerPage,
  );

  const pastActivitiesPaginated = filteredPastActivities.slice(
    (pastPaginationState.activePage - 1) * pastPaginationState.itemsPerPage,
    pastPaginationState.activePage * pastPaginationState.itemsPerPage,
  );

  // Handle pagination for current activities
  const handlePaginationCurrentActivities = (currentPage: any) => {
    setCurrentPaginationState({
      ...currentPaginationState,
      activePage: currentPage, // Update only the current activities page
    });
  };

  // Handle pagination for past activities
  const handlePaginationPastActivities = (currentPage: any) => {
    setPastPaginationState({
      ...pastPaginationState,
      activePage: currentPage, // Update only the past activities page
    });
  };

  useEffect(() => {
    sortEntities();
  }, [currentPaginationState.activePage, currentPaginationState.order, currentPaginationState.sort]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

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
    const fetchSkillsForCurrentActivities = async () => {
      const promises = currentActivities.map(activity => {
        if (activity.skill?.id && !currentSkills[activity.skill.id]) {
          return fetchSkill(activity.skill.id, true);
        }
      });
      await Promise.all(promises);
    };

    const fetchSkillsForPastActivities = async () => {
      const promises = pastActivities.map(activity => {
        if (activity.skill?.id && !pastSkills[activity.skill.id]) {
          return fetchSkill(activity.skill.id, false);
        }
      });
      await Promise.all(promises);
    };

    // Call the async functions
    if (currentActivities.length > 0) {
      fetchSkillsForCurrentActivities();
    }

    if (pastActivities.length > 0) {
      fetchSkillsForPastActivities();
    }
  }, [currentActivities, pastActivities]);

  return (
    <div>
      <h2 id="activity-heading" data-cy="ActivityHeading">
        Activities
        <div className="d-flex justify-content-end">
          <input
            type="text"
            className="form-control"
            placeholder="Search by Activity Name or Venue"
            value={searchQuery}
            onChange={e => setSearchQuery(e.target.value)} // Update search query on input change
          />
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
                    <td>{(currentPaginationState.activePage - 1) * currentPaginationState.itemsPerPage + i + 1}</td>
                    <td>
                      <Button tag={Link} to={`/activity/${activity.id}`} color="link" size="sm">
                        {activity.activityName}
                      </Button>
                    </td>
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

        {filteredCurrentActivities ? (
          <div className={filteredCurrentActivities && filteredCurrentActivities.length > 0 ? '' : 'd-none'}>
            <div className="justify-content-center d-flex">
              <JhiItemCount
                page={currentPaginationState.activePage}
                total={filteredCurrentActivities.length}
                itemsPerPage={currentPaginationState.itemsPerPage}
              />
            </div>
            <div className="justify-content-center d-flex">
              <JhiPagination
                activePage={currentPaginationState.activePage}
                onSelect={handlePaginationCurrentActivities} // Independent pagination handler for current activities
                itemsPerPage={currentPaginationState.itemsPerPage}
                maxButtons={5}
                totalItems={filteredCurrentActivities.length} // Total number of current activities
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
                    <td>{(pastPaginationState.activePage - 1) * pastPaginationState.itemsPerPage + i + 1}</td>
                    <td>
                      {' '}
                      <Button tag={Link} to={`/activity/${activity.id}`} color="link" size="sm">
                        {activity.activityName}
                      </Button>
                    </td>
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
                        {isAdmin && (
                          <Button
                            tag={Link}
                            to={`/activity/${activity.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                            color="primary"
                            size="sm"
                            data-cy="entityEditButton"
                          >
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                          </Button>
                        )}

                        {isAdmin && (
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
                        )}
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

        {filteredPastActivities ? (
          <div className={filteredPastActivities && filteredPastActivities.length > 0 ? '' : 'd-none'}>
            <div className="justify-content-center d-flex">
              <JhiItemCount
                page={pastPaginationState.activePage}
                total={filteredPastActivities.length}
                itemsPerPage={pastPaginationState.itemsPerPage}
              />
            </div>
            <div className="justify-content-center d-flex">
              <JhiPagination
                activePage={pastPaginationState.activePage}
                onSelect={handlePaginationPastActivities} // Independent pagination handler for past activities
                itemsPerPage={pastPaginationState.itemsPerPage}
                maxButtons={5}
                totalItems={filteredPastActivities.length} // Total number of past activities
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
