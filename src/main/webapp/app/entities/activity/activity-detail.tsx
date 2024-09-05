import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './activity.reducer';

export const ActivityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const activityEntity = useAppSelector(state => state.activity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="activityDetailsHeading">Activity</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{activityEntity.id}</dd>
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
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{activityEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {activityEntity.createdDate ? <TextFormat value={activityEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">Last Modified By</span>
          </dt>
          <dd>{activityEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">Last Modified Date</span>
          </dt>
          <dd>
            {activityEntity.lastModifiedDate ? (
              <TextFormat value={activityEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="activityName">Activity Name</span>
          </dt>
          <dd>{activityEntity.activityName}</dd>
          <dt>Creator Profile</dt>
          <dd>{activityEntity.creatorProfile ? activityEntity.creatorProfile.id : ''}</dd>
          <dt>Skill</dt>
          <dd>{activityEntity.skill ? activityEntity.skill.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/activity" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/activity/${activityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActivityDetail;
