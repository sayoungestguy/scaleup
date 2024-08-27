import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">User Profile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="nickname">Nickname</span>
          </dt>
          <dd>{userProfileEntity.nickname}</dd>
          <dt>
            <span id="jobRole">Job Role</span>
          </dt>
          <dd>{userProfileEntity.jobRole}</dd>
          <dt>
            <span id="aboutMe">About Me</span>
          </dt>
          <dd>{userProfileEntity.aboutMe}</dd>
          <dt>
            <span id="profilePicture">Profile Picture</span>
          </dt>
          <dd>{userProfileEntity.profilePicture}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{userProfileEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {userProfileEntity.createdDate ? (
              <TextFormat value={userProfileEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">Last Modified By</span>
          </dt>
          <dd>{userProfileEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">Last Modified Date</span>
          </dt>
          <dd>
            {userProfileEntity.lastModifiedDate ? (
              <TextFormat value={userProfileEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>User</dt>
          <dd>{userProfileEntity.user ? userProfileEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;
