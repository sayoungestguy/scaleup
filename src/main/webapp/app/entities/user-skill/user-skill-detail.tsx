import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-skill.reducer';

export const UserSkillDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userSkillEntity = useAppSelector(state => state.userSkill.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userSkillDetailsHeading">User Skill</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{userSkillEntity.id}</dd>
          <dt>
            <span id="yearsOfExperience">Years Of Experience</span>
          </dt>
          <dd>{userSkillEntity.yearsOfExperience}</dd>
          <dt>User Profile</dt>
          <dd>{userSkillEntity.userProfile ? userSkillEntity.userProfile.id : ''}</dd>
          <dt>Skill</dt>
          <dd>{userSkillEntity.skill ? userSkillEntity.skill.id : ''}</dd>
          <dt>Skill Type</dt>
          <dd>{userSkillEntity.skillType ? userSkillEntity.skillType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-skill" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-skill/${userSkillEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserSkillDetail;
