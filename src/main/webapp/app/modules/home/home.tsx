import './home.scss';
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Row, Col, Alert, Button, Popover, PopoverBody } from 'reactstrap';
import { useAppSelector } from 'app/config/store';

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
  const account = useAppSelector(state => state.authentication.account);

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
