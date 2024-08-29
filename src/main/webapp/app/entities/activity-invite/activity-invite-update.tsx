import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IActivity } from 'app/shared/model/activity.model';
import { getEntities as getActivities } from 'app/entities/activity/activity.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { ICodeTables } from 'app/shared/model/code-tables.model';
import { getEntities as getCodeTables } from 'app/entities/code-tables/code-tables.reducer';
import { IActivityInvite } from 'app/shared/model/activity-invite.model';
import { getEntity, updateEntity, createEntity, reset } from './activity-invite.reducer';

export const ActivityInviteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const activities = useAppSelector(state => state.activity.entities);
  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const codeTables = useAppSelector(state => state.codeTables.entities);
  const activityInviteEntity = useAppSelector(state => state.activityInvite.entity);
  const loading = useAppSelector(state => state.activityInvite.loading);
  const updating = useAppSelector(state => state.activityInvite.updating);
  const updateSuccess = useAppSelector(state => state.activityInvite.updateSuccess);

  const handleClose = () => {
    navigate('/activity-invite');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getActivities({}));
    dispatch(getUserProfiles({}));
    dispatch(getCodeTables({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...activityInviteEntity,
      ...values,
      activity: activities.find(it => it.id.toString() === values.activity?.toString()),
      inviteeProfile: userProfiles.find(it => it.id.toString() === values.inviteeProfile?.toString()),
      status: codeTables.find(it => it.id.toString() === values.status?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...activityInviteEntity,
          createdDate: convertDateTimeFromServer(activityInviteEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(activityInviteEntity.lastModifiedDate),
          activity: activityInviteEntity?.activity?.id,
          inviteeProfile: activityInviteEntity?.inviteeProfile?.id,
          status: activityInviteEntity?.status?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="scaleupApp.activityInvite.home.createOrEditLabel" data-cy="ActivityInviteCreateUpdateHeading">
            Create or edit a Activity Invite
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="activity-invite-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Will Participate"
                id="activity-invite-willParticipate"
                name="willParticipate"
                data-cy="willParticipate"
                check
                type="checkbox"
              />
              <ValidatedField label="Created By" id="activity-invite-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField
                label="Created Date"
                id="activity-invite-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Modified By"
                id="activity-invite-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label="Last Modified Date"
                id="activity-invite-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="activity-invite-activity" name="activity" data-cy="activity" label="Activity" type="select">
                <option value="" key="0" />
                {activities
                  ? activities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="activity-invite-inviteeProfile"
                name="inviteeProfile"
                data-cy="inviteeProfile"
                label="Invitee Profile"
                type="select"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="activity-invite-status" name="status" data-cy="status" label="Status" type="select">
                <option value="" key="0" />
                {codeTables
                  ? codeTables.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/activity-invite" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ActivityInviteUpdate;
