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
import { Accordion, AccordionBody, AccordionHeader, AccordionItem } from 'reactstrap';

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
                      AA{' '}
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

function IntroToScaleUp() {
  const [open, setOpen] = useState('1');
  const toggle = id => {
    if (open === id) {
      setOpen('');
    } else {
      setOpen(id);
    }
  };

  return (
    <div>
      <Accordion open={open} toggle={toggle}>
        {/* ScaleUp Intro */}
        <AccordionItem>
          <AccordionHeader targetId="1">
            <strong>🚀 Introduction to ScaleUp</strong>
          </AccordionHeader>
          <AccordionBody accordionId="1">
            <div style={{ marginBottom: '20px' }}>
              <h4>🌟 ScaleUp: Your Platform for Skill Exchange and Growth</h4>
              <p>
                In today’s fast-paced world, staying competitive requires continuous learning. ScaleUp offers a community-driven platform
                for skill development, where employees can exchange skills without the need for monetary transactions, making learning
                accessible for all.
              </p>
            </div>

            <div style={{ marginBottom: '20px' }}>
              <h5>
                📈 <strong>Key Benefits:</strong>
              </h5>
              <ul className="benefits-list" style={{ listStyleType: 'none', paddingLeft: '0' }}>
                <li>
                  <strong>💡 Affordable Learning:</strong> Skill exchanges without financial barriers.
                </li>
                <li>
                  <strong>🎯 Customized Experiences:</strong> Learn specific skills directly from peers.
                </li>
                <li>
                  <strong>🤝 Community Engagement:</strong> Build a collaborative work environment.
                </li>
                <li>
                  <strong>🔄 Continuous Growth:</strong> Promote lifelong learning and development.
                </li>
              </ul>
            </div>

            <p style={{ fontStyle: 'italic' }}>🎉 Join ScaleUp today to connect, learn, and grow with your colleagues!</p>
          </AccordionBody>
        </AccordionItem>
      </Accordion>
    </div>
  );
}

function ScaleUpUserGuide() {
  const [open, setOpen] = useState('');
  const toggle = id => {
    if (open === id) {
      setOpen('');
    } else {
      setOpen(id);
    }
  };

  return (
    <div>
      <Accordion open={open} toggle={toggle}>
        {/* ScaleUp Intro */}
        {/* <AccordionItem>
          <AccordionHeader targetId="1">
            <strong>🚀 Introduction to ScaleUp</strong>
          </AccordionHeader>
          <AccordionBody accordionId="1">
            <div style={{ marginBottom: '20px' }}>
              <h4>🌟 ScaleUp: Your Platform for Skill Exchange and Growth</h4>
              <p>
                In today’s fast-paced world, staying competitive requires continuous learning. ScaleUp offers a community-driven platform
                for skill development, where employees can exchange skills without the need for monetary transactions, making learning
                accessible for all.
              </p>
            </div>

            <div style={{ marginBottom: '20px' }}>
              <h5>
                📈 <strong>Key Benefits:</strong>
              </h5>
              <ul className="benefits-list" style={{ listStyleType: 'none', paddingLeft: '0' }}>
                <li>
                  <strong>💡 Affordable Learning:</strong> Skill exchanges without financial barriers.
                </li>
                <li>
                  <strong>🎯 Customized Experiences:</strong> Learn specific skills directly from peers.
                </li>
                <li>
                  <strong>🤝 Community Engagement:</strong> Build a collaborative work environment.
                </li>
                <li>
                  <strong>🔄 Continuous Growth:</strong> Promote lifelong learning and development.
                </li>
              </ul>
            </div>

            <p style={{ fontStyle: 'italic' }}>🎉 Join ScaleUp today to connect, learn, and grow with your colleagues!</p>
          </AccordionBody>
        </AccordionItem> */}

        {/* User Access Management */}
        <AccordionItem style={{ border: '1px solid #ccc', borderRadius: '5px', marginBottom: '10px' }}>
          <AccordionHeader targetId="1">User Access Management</AccordionHeader>
          <AccordionBody accordionId="1">
            <strong>Sign Up:</strong> Create a new ScaleUp account using your email.
            <ol>
              <li>Navigate to the Sign Up page.</li>
              <li>Enter a valid email and password.</li>
              <li>Confirm the password and submit.</li>
              <li>Check your email for a verification link and click it.</li>
            </ol>
            <strong>Log In:</strong> Log into your ScaleUp account.
            <ol>
              <li>Navigate to the Log In page.</li>
              <li>Enter your email and password.</li>
              <li>Click Log In to access your account.</li>
            </ol>
            <strong>Log Out:</strong> Log out securely.
            <ol>
              <li>Click Log Out from your profile or navigation bar.</li>
            </ol>
          </AccordionBody>
        </AccordionItem>

        {/* User Profile Management */}
        <AccordionItem style={{ border: '1px solid #ccc', borderRadius: '5px', marginBottom: '10px' }}>
          <AccordionHeader targetId="2">User Profile Management</AccordionHeader>
          <AccordionBody accordionId="2">
            <strong>Add Personal Details:</strong> Update profile with personal info.
            <ol>
              <li>Navigate to Profile and click Edit Profile.</li>
              <li>Enter details like nickname and job role, then save.</li>
            </ol>
            <strong>Change Profile Picture:</strong> Upload a new profile picture.
            <ol>
              <li>Click Change Picture and upload a new image.</li>
            </ol>
            <strong>Skills to Teach/Learn:</strong> List your skills.
            <ol>
              <li>Go to Skills in your profile, add skills to teach or learn.</li>
              <li>Save your changes.</li>
            </ol>
          </AccordionBody>
        </AccordionItem>

        {/* Activity Management */}
        <AccordionItem style={{ border: '1px solid #ccc', borderRadius: '5px', marginBottom: '10px' }}>
          <AccordionHeader targetId="3">Activity Management</AccordionHeader>
          <AccordionBody accordionId="3">
            <strong>Search for Skills:</strong> Find users with specific skills.
            <ol>
              <li>Use the search feature, enter the skill you need, and review results.</li>
            </ol>
            <strong>Create a Forecast Activity:</strong> Organize a learning or teaching event.
            <ol>
              <li>Go to Activities and click Create Activity.</li>
              <li>Add skill, time, date, and venue, then save the activity.</li>
            </ol>
            <strong>Edit/Delete Activities:</strong> Modify or remove activities.
            <ol>
              <li>Navigate to Activities and click Edit or Delete.</li>
            </ol>
            <strong>View Activity Records:</strong> Check current or past activities.
            <ol>
              <li>Go to your Activities page and select Current or Past.</li>
            </ol>
            <strong>Invite Users to Activity:</strong> Send invites for your activity.
            <ol>
              <li>Use the Invite feature during activity creation or editing.</li>
            </ol>
          </AccordionBody>
        </AccordionItem>

        {/* Messaging System */}
        <AccordionItem style={{ border: '1px solid #ccc', borderRadius: '5px', marginBottom: '10px' }}>
          <AccordionHeader targetId="4">Messaging System</AccordionHeader>
          <AccordionBody accordionId="4">
            <strong>Send Messages:</strong> Communicate with other users.
            <ol>
              <li>Navigate to the Messages section.</li>
              <li>Select or search for a user, type your message, and send.</li>
            </ol>
            <strong>Persistent Messaging:</strong> Chat History Preservation
            <ol>
              <li> Messages are saved even after exiting chat.</li>
            </ol>
            <strong>Delete Messages:</strong> Remove your sent messages.
            <ol>
              <li>Find the message, click delete, and confirm.</li>
            </ol>
          </AccordionBody>
        </AccordionItem>
      </Accordion>
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
        <div className="intro-section">
          <IntroToScaleUp />
        </div>
        {account?.login ? (
          <>
            <div className="upcoming-activities-section" style={{ marginTop: '20px' }}>
              {/* <Alert color="success">You are logged in as user &quot;{account.login}&quot;.</Alert> */}
              <DisplayUpcomingActivity />
            </div>
            <div style={{ marginTop: '20px' }}>
              <h3>
                <p>ScaleUp User Guide</p>
              </h3>
              <div>
                <ScaleUpUserGuide />
              </div>
            </div>
          </>
        ) : (
          <div />
        )}

        {/* <h3>
          <p>Types of Skills</p>
        </h3>
        <SkillsPopover />
        <div>
          <AlertRegister />
        </div> */}
      </Col>
    </Row>
  );
}

export default Home;
