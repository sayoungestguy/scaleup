import React, { useEffect } from 'react';
import { Link, useParams, useSearchParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getActivityById } from './activity.reducer';
import { getSkillById } from 'app/entities/skill/skill.reducer';
import { getUserProfileById } from 'app/entities/user-profile/user-profile.reducer';

export const ActivityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  const [searchParams] = useSearchParams();
  const activityStatus = searchParams.get('type') || 'unknown'; // Get the type from the query params

  useEffect(() => {
    dispatch(getActivityById(id));
  }, []);

  const activityEntity = useAppSelector(state => state.activity.entity);

  useEffect(() => {
    if (activityEntity?.creatorProfile?.id) {
      dispatch(getUserProfileById(activityEntity.creatorProfile.id)); // Fetch profile by ID
    }
    if (activityEntity?.skill?.id) {
      dispatch(getSkillById(activityEntity.skill.id)); // Fetch skill by ID
    }
  }, [dispatch, activityEntity]);

  // Get profile and skill data from the Redux state
  const creatorProfile = useAppSelector(state => (activityEntity.creatorProfile?.id ? state.userProfile.entity : null));
  const skill = useAppSelector(state => (activityEntity.skill?.id ? state.skill.entity : null));

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
            <dd>{creatorProfile ? creatorProfile.nickname : ''}</dd>
            <dt>Skill</dt>
            <dd>{skill ? skill.skillName : ''}</dd>
          </dl>
          <Button tag={Link} to={-1} replace color="info" data-cy="entityDetailsBackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          {activityStatus === 'current' ? (
            <Button tag={Link} to={`/activity/${activityEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
            </Button>
          ) : (
            ''
          )}
        </Col>
      </Row>
    </>
  );
};

export default ActivityDetail;
