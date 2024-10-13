import './home.scss';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Row, Col, Alert, Button, Popover, PopoverBody, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TextFormat, JhiPagination, JhiItemCount } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getAllActivity } from 'app/entities/activity/activity.reducer';
import { ASC } from 'app/shared/util/pagination.constants';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';

// function DisplayUpcomingActivity() {

//   return (
//     <div>
//       <h3>Upcoming Activities</h3>

//       <p>There are no upcoming activities.</p>
//     </div>
//   );
// }

function DisplayUpcomingActivity() {
  const dispatch = useAppDispatch();

  const activityList = useAppSelector(state => state.activity.entities);
  const loading = useAppSelector(state => state.activity.loading);

  const [currentActivities, setCurrentActivities] = useState([]);
  const [paginationState, setPaginationState] = useState({
    activePage: 1,
    itemsPerPage: 5,
    sort: 'activityTime',
    order: ASC,
  });

  // Fetch activities when the component mounts
  useEffect(() => {
    dispatch(getAllActivity({ sort: `${paginationState.sort},${paginationState.order}` }));
  }, [paginationState]);

  // Filter current activities based on the activityTime
  useEffect(() => {
    if (activityList && activityList.length > 0) {
      const current = activityList.filter(
        (activity: { activityTime: string | number | Date }) => new Date(activity.activityTime) >= new Date(),
      );
      setCurrentActivities(current);
    }
  }, [activityList]);

  // Handle pagination for current activities
  const handlePagination = (currentPage: any) => {
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });
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

  // Paginate current activities
  const currentActivitiesPaginated = currentActivities.slice(
    (paginationState.activePage - 1) * paginationState.itemsPerPage,
    paginationState.activePage * paginationState.itemsPerPage,
  );

  return (
    <div>
      <h3>Upcoming Activities</h3>
      <div className="current-activities border border-5 p-3 m-2">
        <div className="table-responsive">
          {currentActivitiesPaginated.length > 0 ? (
            <Table responsive bordered>
              <thead>
                <tr>
                  <th>S/No.</th>
                  <th>
                    Activity Name <FontAwesomeIcon icon={getSortIconByFieldName('activityName')} />
                  </th>
                  <th>
                    Activity Time <FontAwesomeIcon icon={getSortIconByFieldName('activityTime')} />
                  </th>
                  <th>
                    Duration <FontAwesomeIcon icon={getSortIconByFieldName('duration')} />
                  </th>
                  <th>
                    Venue <FontAwesomeIcon icon={getSortIconByFieldName('venue')} />
                  </th>
                  <th>
                    Details <FontAwesomeIcon icon={getSortIconByFieldName('details')} />
                  </th>
                </tr>
              </thead>
              <tbody>
                {currentActivitiesPaginated.map((activity, i) => (
                  <tr key={`entity-${i}`}>
                    <td>{(paginationState.activePage - 1) * paginationState.itemsPerPage + i + 1}</td>
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
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && <div className="alert alert-warning">No Upcoming Activities found</div>
          )}
        </div>

        {currentActivities.length > 0 && (
          <div className="pagination-wrapper">
            <div className="justify-content-center d-flex">
              <JhiItemCount
                page={paginationState.activePage}
                total={currentActivities.length}
                itemsPerPage={paginationState.itemsPerPage}
              />
            </div>
            <div className="justify-content-center d-flex">
              <JhiPagination
                activePage={paginationState.activePage}
                onSelect={handlePagination}
                itemsPerPage={paginationState.itemsPerPage}
                maxButtons={5}
                totalItems={currentActivities.length}
              />
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

function SkillsPopover() {
  const [popoverOpen, setPopoverOpen] = useState({
    softSkills: false,
    technicalSkills: false,
    programmingSkills: false,
    communicationSkills: false,
  });

  const togglePopover = skill => {
    setPopoverOpen({
      ...popoverOpen,
      [skill]: !popoverOpen[skill],
    });
  };

  // Add the following code to the Home component

  return (
    <ul>
      <li>
        <Button id="softSkills" color="link" className="skill-button">
          Soft Skills
        </Button>
        <Popover
          placement="right"
          isOpen={popoverOpen.softSkills}
          target="softSkills"
          toggle={() => togglePopover('softSkills')}
          trigger="hover"
        >
          <PopoverBody>
            <ul>
              <li>Communication</li>
              <li>Teamwork</li>
              <li>Problem-solving</li>
              <li>Adaptability</li>
              <li>Time Management</li>
            </ul>
          </PopoverBody>
        </Popover>
      </li>
      <li>
        <Button id="technicalSkills" color="link" className="skill-button">
          Technical Skills
        </Button>
        <Popover
          placement="right"
          isOpen={popoverOpen.technicalSkills}
          target="technicalSkills"
          toggle={() => togglePopover('technicalSkills')}
          trigger="hover"
        >
          <PopoverBody>
            <ul>
              <li>Engineering Principles</li>
              <li>Information Technology (IT)</li>
              <li>Data Analysis</li>
              <li>Software Development</li>
              <li>System Design</li>
            </ul>
          </PopoverBody>
        </Popover>
      </li>
      <li>
        <Button id="programmingSkills" color="link" className="skill-button">
          Programming Skills
        </Button>
        <Popover
          placement="right"
          isOpen={popoverOpen.programmingSkills}
          target="programmingSkills"
          toggle={() => togglePopover('programmingSkills')}
          trigger="hover"
        >
          <PopoverBody>
            <ul>
              <li>Python</li>
              <li>Java</li>
              <li>C++</li>
              <li>JavaScript</li>
              <li>SQL</li>
            </ul>
          </PopoverBody>
        </Popover>
      </li>
      <li>
        <Button id="communicationSkills" color="link" className="skill-button">
          Communication Skills
        </Button>
        <Popover
          placement="right"
          isOpen={popoverOpen.communicationSkills}
          target="communicationSkills"
          toggle={() => togglePopover('communicationSkills')}
          trigger="hover"
        >
          <PopoverBody>
            <ul>
              <li>Active Listening</li>
              <li>Presentation Skills</li>
              <li>Non-Verbal Communication</li>
              <li>Public Speaking</li>
              <li>Writing Skills</li>
            </ul>
          </PopoverBody>
        </Popover>
      </li>
    </ul>
  );
}
function AlertRegister() {
  return (
    <Alert color="info" className="custom-alert">
      <Link to="/account/register" className="alert-link">
        <h3>Register a new account</h3>
      </Link>
    </Alert>
  );
}

function Home() {
  const account = useAppSelector(state => state.authentication.account); // Get account information
  const dispatch = useAppDispatch();
  const userProfileEntitties = useAppSelector(state => state.userProfile.entities);
  const loggedInUserProfile = userProfileEntitties.find((profile: IUserProfile) => profile.createdBy === account.login);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h1 className="display-4">Welcome to ScaleUp</h1>
        {account?.login ? (
          <div>
            <Alert color="success">You are logged in as user &quot;{account.login}&quot;.</Alert>
            <DisplayUpcomingActivity />
          </div>
        ) : (
          <div />
        )}
        <h3>
          <p>Types of Skills</p>
        </h3>
        <SkillsPopover />
        <div>
          <AlertRegister />
        </div>
      </Col>
    </Row>
  );
}

export default Home;
