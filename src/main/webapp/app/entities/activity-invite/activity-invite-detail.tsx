import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getActivityInviteById, getStatusById } from './activity-invite.reducer';
import { getInviteeProfileById } from './activity-invite.reducer';
import { getActivityById } from 'app/entities/activity/activity.reducer';

export const ActivityInviteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getActivityInviteById(id));
  }, []);

  const activityInviteEntity = useAppSelector(state => state.activityInvite.entity);

  useEffect(() => {
    if (activityInviteEntity?.activity?.id) {
      dispatch(getActivityById(activityInviteEntity.activity.id)); // Fetch Activity by ID
    }
    if (activityInviteEntity?.inviteeProfile?.id) {
      dispatch(getInviteeProfileById(activityInviteEntity.inviteeProfile.id)); // Fetch Invitee Profile by ID
    }
    if (activityInviteEntity?.status?.id) {
      dispatch(getStatusById(activityInviteEntity.status.id)); // Fetch status by ID
    }
  }, [dispatch, activityInviteEntity]);

  // Get activity, invitee profile and status from the Redux state
  const activity = useAppSelector(state => (activityInviteEntity.activity?.id ? state.activity.entity : null));
  const inviteeProfile = useAppSelector(state => (activityInviteEntity.inviteeProfile?.id ? state.inviteeProfile.entity : null));
  const status = useAppSelector(state => (activityInviteEntity.status?.id ? state.status.entity : null));

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="activityInviteDetailsHeading">Activity Invite</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{activityInviteEntity.id}</dd>
          <dt>
            <span id="willParticipate">Will Participate</span>
          </dt>
          <dd>{activityInviteEntity.willParticipate ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{activityInviteEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {activityInviteEntity.createdDate ? (
              <TextFormat value={activityInviteEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">Last Modified By</span>
          </dt>
          <dd>{activityInviteEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">Last Modified Date</span>
          </dt>
          <dd>
            {activityInviteEntity.lastModifiedDate ? (
              <TextFormat value={activityInviteEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Activity</dt>
          <dd>{activityInviteEntity.activity ? activityInviteEntity.activity.id : ''}</dd>
          <dt>Invitee Profile</dt>
          <dd>{activityInviteEntity.inviteeProfile ? activityInviteEntity.inviteeProfile.id : ''}</dd>
          <dt>Status</dt>
          <dd>{activityInviteEntity.status ? activityInviteEntity.status.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/activity-invite" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/activity-invite/${activityInviteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActivityInviteDetail;
