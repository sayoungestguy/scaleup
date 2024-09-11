import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { ISkill } from 'app/shared/model/skill.model';
import { getAllSkills as getSkills } from 'app/entities/skill/skill.reducer';
import { IActivity } from 'app/shared/model/activity.model';
import { getActivityById, updateEntity, createEntity, reset } from './activity.reducer';
import { min } from 'lodash';

export const ActivityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const currentUser = useAppSelector(state => state.authentication.account);

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const skills = useAppSelector(state => state.skill.entities);
  const activityEntity = useAppSelector(state => state.activity.entity);
  const loading = useAppSelector(state => state.activity.loading);
  const updating = useAppSelector(state => state.activity.updating);
  const updateSuccess = useAppSelector(state => state.activity.updateSuccess);

  const handleClose = () => {
    navigate('/activity' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getActivityById(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getSkills({}));
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
    values.activityTime = convertDateTimeToServer(values.activityTime);
    if (values.duration !== undefined && typeof values.duration !== 'number') {
      values.duration = Number(values.duration);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...activityEntity,
      ...values,
      creatorProfile: userProfiles.find(it => it.id.toString() == currentUser.id.toString()),
      skill: skills.find(it => it.id.toString() === values.skill?.toString()),
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
          activityTime: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...activityEntity,
          activityTime: convertDateTimeFromServer(activityEntity.activityTime),
          createdDate: convertDateTimeFromServer(activityEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(activityEntity.lastModifiedDate),
          creatorProfile: activityEntity?.creatorProfile?.id,
          skill: activityEntity?.skill?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="scaleupApp.activity.home.createOrEditLabel" data-cy="ActivityCreateUpdateHeading">
            {isNew ? 'Create an Activity' : 'Edit an Activity'}
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {/*{!isNew ? <ValidatedField name="id" required readOnly id="activity-id" label="ID" validate={{ required: true }} /> : null}*/}
              <ValidatedField
                label="Activity Name"
                id="activity-activityName"
                name="activityName"
                data-cy="activityName"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Activity Time"
                id="activity-activityTime"
                name="activityTime"
                data-cy="activityTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Duration"
                id="activity-duration"
                name="duration"
                data-cy="duration"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required' },
                  min: { value: 1, message: 'Value must be more than 0' },
                  max: { value: 999, message: 'Value cannot be more than 999' },
                  validate: {
                    notDecimal: value => Number.isInteger(Number(value)) || 'Value cannot be a decimal',
                    notNegative: value => value >= 0 || 'Value cannot be negative',
                  },
                }}
              />
              <ValidatedField
                label="Venue"
                id="activity-venue"
                name="venue"
                data-cy="venue"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required' },
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Details"
                id="activity-details"
                name="details"
                data-cy="details"
                type="textarea"
                validate={{
                  required: { value: true, message: 'This field is required' },
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              {/*<ValidatedField*/}
              {/*  id="activity-creatorProfile"*/}
              {/*  name="creatorProfile"*/}
              {/*  data-cy="creatorProfile"*/}
              {/*  label="Creator Profile"*/}
              {/*  type="select"*/}
              {/*  validate={{*/}
              {/*    required: { value: true, message: 'This field is required' },*/}
              {/*  }}*/}
              {/*>*/}
              {/*  <option value="" key="0" />*/}
              {/*  {userProfiles*/}
              {/*    ? userProfiles.map(otherEntity => (*/}
              {/*        <option value={otherEntity.id} key={otherEntity.id}>*/}
              {/*          {otherEntity.id}*/}
              {/*        </option>*/}
              {/*      ))*/}
              {/*    : null}*/}
              {/*</ValidatedField>*/}
              <ValidatedField
                id="activity-skill"
                name="skill"
                data-cy="skill"
                label="Skill"
                type="select"
                validate={{
                  required: { value: true, message: 'This field is required' },
                }}
              >
                <option value="" key="0" />
                {skills
                  ? skills.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.skillName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/activity" replace color="info">
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

export default ActivityUpdate;
