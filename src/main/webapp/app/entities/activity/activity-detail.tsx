import React, { useEffect, useState } from 'react';
import { Link, useLocation, useParams, useSearchParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { byteSize, getPaginationState, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getActivityById } from './activity.reducer';
import { getAllActivityInvites, reset } from 'app/entities/activity-invite/activity-invite.reducer';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import InfiniteScroll from 'react-infinite-scroll-component';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getSkillById } from 'app/entities/skill/skill.reducer';
import { getEntity } from 'app/entities/user-profile/user-profile.reducer';
import ActivityInvite from 'app/entities/activity-invite';
import ActivityInviteTable from 'app/entities/activity-invite/activity-invite-table';

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
      dispatch(getEntity(activityEntity.creatorProfile.id)); // Fetch profile by ID
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
          <Button tag={Link} to="/activity" replace color="info" data-cy="entityDetailsBackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          {activityStatus == 'current' ? (
            <Button tag={Link} to={`/activity/${activityEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
            </Button>
          ) : (
            ''
          )}
        </Col>
      </Row>
      <Row className="p-5">
        <div className="activity-invite-tbl border border-5 p-3 m-2">
          {/*<ActivityInviteTable*/}
          {/*  activityInviteList={activityInviteList}*/}
          {/*  sort={sort}*/}
          {/*  getSortIconByFieldName={getSortIconByFieldName}*/}
          {/*  loading={loading}*/}
          {/*  paginationState={paginationState}*/}
          {/*  totalItems={totalItems}*/}
          {/*  handlePagination*/}
          {/*  handleSyncList*/}
          {/*/>*/}
          <ActivityInviteTable activityId={id} />
        </div>
      </Row>
    </>
  );
};

export default ActivityDetail;
