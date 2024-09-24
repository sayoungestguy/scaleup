// import React, { useState, useEffect } from 'react';
// import { Link, useNavigate, useParams } from 'react-router-dom';
// import { Button, Row, Col, FormText } from 'reactstrap';
// import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

// import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
// import { mapIdList } from 'app/shared/util/entity-utils';
// import { useAppDispatch, useAppSelector } from 'app/config/store';

// import { IUserProfile } from 'app/shared/model/user-profile.model';
// import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
// import { IMessage } from 'app/shared/model/message.model';
// import { getEntity, updateEntity, createEntity, reset } from './message.reducer';

// export const MessageUpdate = () => {
//   const dispatch = useAppDispatch();

//   const navigate = useNavigate();

//   const { id } = useParams<'id'>();
//   const isNew = id === undefined;

//   const userProfiles = useAppSelector(state => state.userProfile.entities);
//   const messageEntity = useAppSelector(state => state.message.entity);
//   const loading = useAppSelector(state => state.message.loading);
//   const updating = useAppSelector(state => state.message.updating);
//   const updateSuccess = useAppSelector(state => state.message.updateSuccess);

//   const handleClose = () => {
//     navigate('/message');
//   };

//   useEffect(() => {
//     if (!isNew) {
//       dispatch(getEntity(id));
//     }

//     dispatch(getUserProfiles({}));
//   }, []);

//   useEffect(() => {
//     if (updateSuccess) {
//       handleClose();
//     }
//   }, [updateSuccess]);

//   // eslint-disable-next-line complexity
//   const saveEntity = values => {
//     if (values.id !== undefined && typeof values.id !== 'number') {
//       values.id = Number(values.id);
//     }
//     values.sentAt = convertDateTimeToServer(values.sentAt);
//     values.createdDate = convertDateTimeToServer(values.createdDate);
//     values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

//     const entity = {
//       ...messageEntity,
//       ...values,
//       senderProfile: userProfiles.find(it => it.id.toString() === values.senderProfile?.toString()),
//       receiverProfile: userProfiles.find(it => it.id.toString() === values.receiverProfile?.toString()),
//     };

//     if (isNew) {
//       dispatch(createEntity(entity));
//     } else {
//       dispatch(updateEntity(entity));
//     }
//   };

//   const defaultValues = () =>
//     isNew
//       ? {
//           sentAt: displayDefaultDateTime(),
//           createdDate: displayDefaultDateTime(),
//           lastModifiedDate: displayDefaultDateTime(),
//         }
//       : {
//           ...messageEntity,
//           sentAt: convertDateTimeFromServer(messageEntity.sentAt),
//           createdDate: convertDateTimeFromServer(messageEntity.createdDate),
//           lastModifiedDate: convertDateTimeFromServer(messageEntity.lastModifiedDate),
//           senderProfile: messageEntity?.senderProfile?.id,
//           receiverProfile: messageEntity?.receiverProfile?.id,
//         };

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
import { IMessage } from 'app/shared/model/message.model';
import { getEntity, updateEntity, createEntity, reset } from './message.reducer';
import axios from 'axios';

export const MessageUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const messageEntity = useAppSelector(state => state.message.entity);
  const loading = useAppSelector(state => state.message.loading);
  const updating = useAppSelector(state => state.message.updating);
  const updateSuccess = useAppSelector(state => state.message.updateSuccess);

  const [currentUserId, setCurrentUserId] = useState(null);

  const handleClose = () => {
    navigate('/message');
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

    fetchCurrentUserId();
    if (!isNew) {
      dispatch(getEntity(id));
    }
    dispatch(getUserProfiles({}));
  }, [dispatch, id, isNew]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.sentAt = convertDateTimeToServer(values.sentAt);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...messageEntity,
      ...values,
      //senderProfile: currentUserId, // Set senderProfile to the current user's ID
      senderProfile: {
        id: currentUserId, // Wrap currentUserId in an object
      },
      receiverProfile: userProfiles.find(it => it.id.toString() === values.receiverProfile?.toString()),
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
          senderProfile: currentUserId, // Set default senderProfile to current user ID
        }
      : {
          ...messageEntity,
          sentAt: convertDateTimeFromServer(messageEntity.sentAt),
          createdDate: convertDateTimeFromServer(messageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(messageEntity.lastModifiedDate),
          //senderProfile: currentUserId || messageEntity?.senderProfile?.id, // Use currentUserId or existing senderProfile ID
          senderProfile: messageEntity?.senderProfile?.id, // Safely access senderProfile
          receiverProfile: messageEntity?.receiverProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="scaleupApp.message.home.createOrEditLabel" data-cy="MessageCreateUpdateHeading">
            Create or edit a Message
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" id="message-id" label="ID" readOnly validate={{ required: true }} /> : null}
              <ValidatedField
                label="Content"
                id="message-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              {/* <ValidatedField
                label="Sent At"
                id="message-sentAt"
                name="sentAt"
                data-cy="sentAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              /> */}
              {/* <ValidatedField label="Is Deleted" id="message-isDeleted" name="isDeleted" data-cy="isDeleted" check type="checkbox" /> */}
              {/* <ValidatedField label="Created By" id="message-createdBy" name="createdBy" data-cy="createdBy" type="text" /> */}
              {/* <ValidatedField
                label="Created Date"
                id="message-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              /> */}
              {/* <ValidatedField
                label="Last Modified By"
                id="message-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              /> */}
              {/* <ValidatedField
                label="Last Modified Date"
                id="message-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              /> */}
              {/* allows user to select other user as sender, logically should not happen. */}
              {/* <ValidatedField id="message-senderProfile" name="senderProfile" data-cy="senderProfile" label="Sender Profile" type="select">
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.user.id} key={otherEntity.user.id}>
                        {otherEntity.user.login}
                      </option>
                    ))
                  : null}
              </ValidatedField> */}
              <ValidatedField
                id="message-senderProfile"
                name="senderProfile"
                data-cy="senderProfile"
                label="Sender Profile"
                type="select"
                value={currentUserId}
                disabled // Sender should not be editable
              >
                <option value={currentUserId} key={currentUserId}>
                  {userProfiles.find(profile => profile.user.id === currentUserId)?.user.login || 'Current User'}
                </option>
              </ValidatedField>
              <ValidatedField
                id="message-receiverProfile"
                name="receiverProfile"
                data-cy="receiverProfile"
                label="Receiver Profile"
                type="select"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.user.id} key={otherEntity.user.id}>
                        {otherEntity.user.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/message" replace color="info">
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

export default MessageUpdate;
