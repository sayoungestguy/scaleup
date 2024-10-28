import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getUserProfileById, updateUserProfile, createUserProfile, reset } from './user-profile.reducer';

export const UserProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  const loading = useAppSelector(state => state.userProfile.loading);
  const updating = useAppSelector(state => state.userProfile.updating);
  const updateSuccess = useAppSelector(state => state.userProfile.updateSuccess);
  const currentUser = useAppSelector(state => state.authentication.account);

  const handleClose = () => {
    navigate('/user-profile' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getUserProfileById(id));
    }

    dispatch(getUsers({}));
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
      ...userProfileEntity,
      ...values,
      user: users.find(it => it.id.toString() === currentUser.id?.toString()),
    };

    if (isNew) {
      dispatch(createUserProfile(entity));
    } else {
      dispatch(updateUserProfile(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...userProfileEntity,
          createdDate: convertDateTimeFromServer(userProfileEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userProfileEntity.lastModifiedDate),
          user: userProfileEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="scaleupApp.userProfile.home.createOrEditLabel" data-cy="UserProfileCreateUpdateHeading">
            {isNew ? 'Create' : 'Edit'} a User Profile
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="user-profile-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Nickname"
                id="user-profile-nickname"
                name="nickname"
                data-cy="nickname"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Job Role"
                id="user-profile-jobRole"
                name="jobRole"
                data-cy="jobRole"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="About Me"
                id="user-profile-aboutMe"
                name="aboutMe"
                data-cy="aboutMe"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Profile Picture"
                id="user-profile-profilePicture"
                name="profilePicture"
                data-cy="profilePicture"
                type="select"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              >
                <option value="" disabled>
                  Select a profile picture
                </option>
                <option value="Male Profile Picture">Male Profile Picture</option>
                <option value="Female Profile Picture">Female Profile Picture</option>
              </ValidatedField>
              <ValidatedField
                label="Social Links"
                id="user-profile-socialLinks"
                name="socialLinks"
                data-cy="socialLinks"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to={-1} replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button
                color="primary"
                id="save-entity"
                data-cy="entityCreateSaveButton"
                onClick={() => navigate(-1)}
                type="submit"
                disabled={updating}
              >
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

export default UserProfileUpdate;
