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
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { ISkill } from 'app/shared/model/skill.model';
import { getEntities as getSkills } from 'app/entities/skill/skill.reducer';
import { ICodeTables } from 'app/shared/model/code-tables.model';
import { getEntities as getCodeTables } from 'app/entities/code-tables/code-tables.reducer';
import { IUserSkill } from 'app/shared/model/user-skill.model';
import { getEntity, updateEntity, createEntity, reset } from './user-skill.reducer';

export const UserSkillUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const skills = useAppSelector(state => state.skill.entities);
  const codeTables = useAppSelector(state => state.codeTables.entities);
  const userSkillEntity = useAppSelector(state => state.userSkill.entity);
  const loading = useAppSelector(state => state.userSkill.loading);
  const updating = useAppSelector(state => state.userSkill.updating);
  const updateSuccess = useAppSelector(state => state.userSkill.updateSuccess);

  const handleClose = () => {
    navigate('/user-skill' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getSkills({}));
    dispatch(getCodeTables({}));
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
    if (values.yearsOfExperience !== undefined && typeof values.yearsOfExperience !== 'number') {
      values.yearsOfExperience = Number(values.yearsOfExperience);
    }

    const entity = {
      ...userSkillEntity,
      ...values,
      userProfile: userProfiles.find(it => it.id.toString() === values.userProfile?.toString()),
      skill: skills.find(it => it.id.toString() === values.skill?.toString()),
      skillType: codeTables.find(it => it.id.toString() === values.skillType?.toString()),
      user: users.find(it => it.id.toString() === values.user?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...userSkillEntity,
          userProfile: userSkillEntity?.userProfile?.id,
          skill: userSkillEntity?.skill?.id,
          skillType: userSkillEntity?.skillType?.id,
          user: userSkillEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="scaleupApp.userSkill.home.createOrEditLabel" data-cy="UserSkillCreateUpdateHeading">
            Create or edit a User Skill
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="user-skill-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Years Of Experience"
                id="user-skill-yearsOfExperience"
                name="yearsOfExperience"
                data-cy="yearsOfExperience"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField id="user-skill-userProfile" name="userProfile" data-cy="userProfile" label="User Profile" type="select">
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nickname}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="user-profile-user" name="user" data-cy="user" label="User" type="select" required>
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="user-skill-skill" name="skill" data-cy="skill" label="Skill" type="select">
                <option value="" key="0" />
                {skills
                  ? skills.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.skillName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="user-skill-skillType" name="skillType" data-cy="skillType" label="Skill Type" type="select">
                <option value="" key="0" />
                {codeTables
                  ? codeTables.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.codeValue}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-skill" replace color="info">
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

export default UserSkillUpdate;
