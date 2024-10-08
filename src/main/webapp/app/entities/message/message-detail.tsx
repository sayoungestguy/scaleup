import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './message.reducer';

export const MessageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const messageEntity = useAppSelector(state => state.message.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="messageDetailsHeading">Message</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{messageEntity.id}</dd>
          <dt>
            <span id="content">Content</span>
          </dt>
          <dd>{messageEntity.content}</dd>
          {/* <dt>
            <span id="sentAt">Sent At</span>
          </dt>
          <dd>{messageEntity.sentAt ? <TextFormat value={messageEntity.sentAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isDeleted">Is Deleted</span>
          </dt>
          <dd>{messageEntity.isDeleted ? 'true' : 'false'}</dd> */}
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{messageEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {messageEntity.createdDate ? <TextFormat value={messageEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          {/* <dt>
            <span id="lastModifiedBy">Last Modified By</span>
          </dt>
          <dd>{messageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">Last Modified Date</span>
          </dt>
          <dd>
            {messageEntity.lastModifiedDate ? (
              <TextFormat value={messageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd> */}
          <dt>Sender Profile</dt>
          <dd>{messageEntity.senderProfile ? messageEntity.senderProfile.id : ''}</dd>
          <dt>Receiver Profile</dt>
          <dd>{messageEntity.receiverProfile ? messageEntity.receiverProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/message" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        {/* <Button tag={Link} to={`/message/${messageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button> */}
      </Col>
    </Row>
  );
};

export default MessageDetail;
