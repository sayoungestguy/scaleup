import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IMessage } from 'app/shared/model/message.model';
import { getEntity, updateEntity, createEntity, reset } from './message.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import axios from 'axios';

export const MessageUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const messageEntity = useAppSelector(state => state.message.entity);
  const loading = useAppSelector(state => state.message.loading);
  const updating = useAppSelector(state => state.message.updating);
  const updateSuccess = useAppSelector(state => state.message.updateSuccess);
  const users = useAppSelector(state => state.userManagement.users);

  const [currentUserId, setCurrentUserId] = useState(null);

  const handleClose = () => {
    navigate('/message' + location.search);
  };

  useEffect(() => {
    const fetchCurrentUserId = async () => {
      try {
        const response = await axios.get('/api/current-user'); // Adjust this endpoint if necessary
        setCurrentUserId(response.data); // Assuming response.data contains the user ID
      } catch (error) {
        console.error('Error fetching current user ID:', error);
      }
    };

    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    fetchCurrentUserId();
    dispatch(getUsers({}));
  }, [dispatch, id, isNew]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...messageEntity,
      ...values,
      senderProfile: {
        id: currentUserId, // Set senderProfile to current user's ID
      },
      receiverProfile: users.find(it => it.id.toString() === values.receiverProfile?.toString()),
      sentAt: new Date(values.sentAt).toISOString(), // Ensure proper date format
      createdDate: new Date().toISOString(), // Example for createdDate
      lastModifiedDate: new Date().toISOString(), // Example for lastModifiedDate
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
          sentAt: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...messageEntity,
          sentAt: convertDateTimeFromServer(messageEntity.sentAt),
          createdDate: convertDateTimeFromServer(messageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(messageEntity.lastModifiedDate),
          senderProfile: messageEntity?.senderProfile?.id,
          receiverProfile: messageEntity?.receiverProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="scaleupApp.message.home.createOrEditLabel">Create or edit a Message</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="message-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Content"
                id="message-content"
                name="content"
                type="textarea"
                validate={{ required: { value: true, message: 'This field is required.' } }}
              />
              <ValidatedField
                id="message-senderProfile"
                name="senderProfile"
                label="Sender Profile"
                type="select"
                value={currentUserId}
                disabled // Sender should not be editable
              >
                <option value={currentUserId} key={currentUserId}>
                  {currentUserId}
                </option>
              </ValidatedField>
              <ValidatedField
                id="message-receiverProfile"
                name="receiverProfile"
                label="Receiver Profile"
                type="select"
                validate={{ required: { value: true, message: 'This field is required.' } }}
              >
                <option value="" key="0" />
                {users.map(otherEntity => (
                  <option value={otherEntity.id} key={otherEntity.id}>
                    {otherEntity.login}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" to="/message" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp; Back
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
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

export default MessageUpdate;
