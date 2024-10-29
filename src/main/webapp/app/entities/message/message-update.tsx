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
// import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';

// export const MessageUpdate = () => {
//   const dispatch = useAppDispatch();

//   const navigate = useNavigate();

//   const { id } = useParams<'id'>();
//   const isNew = id === undefined;

//   //const userProfiles = useAppSelector(state => state.userProfile.entities);
//   const messageEntity = useAppSelector(state => state.message.entity);
//   const loading = useAppSelector(state => state.message.loading);
//   const updating = useAppSelector(state => state.message.updating);
//   const updateSuccess = useAppSelector(state => state.message.updateSuccess);
//   const users = useAppSelector(state => state.userManagement.users);

//   const handleClose = () => {
//     navigate('/message' + location.search);
//   };

//   useEffect(() => {
//     if (isNew) {
//       dispatch(reset());
//     } else {
//       dispatch(getEntity(id));
//     }

//     dispatch(getUsers({}));
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
//       senderProfile: users.find(it => it.id.toString() === values.senderProfile?.toString()),
//       receiverProfile: users.find(it => it.id.toString() === values.receiverProfile?.toString()),
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

//   return (
//     <div>
//       <Row className="justify-content-center">
//         <Col md="8">
//           <h2 id="scaleupApp.message.home.createOrEditLabel" data-cy="MessageCreateUpdateHeading">
//             Create or edit a Message
//           </h2>
//         </Col>
//       </Row>
//       <Row className="justify-content-center">
//         <Col md="8">
//           {loading ? (
//             <p>Loading...</p>
//           ) : (
//             <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
//               {!isNew ? <ValidatedField name="id" required readOnly id="message-id" label="ID" validate={{ required: true }} /> : null}
//               <ValidatedField
//                 label="Content"
//                 id="message-content"
//                 name="content"
//                 data-cy="content"
//                 type="textarea"
//                 validate={{
//                   required: { value: true, message: 'This field is required.' },
//                 }}
//               />
//               {/* <ValidatedField
//                 label="Sent At"
//                 id="message-sentAt"
//                 name="sentAt"
//                 data-cy="sentAt"
//                 type="datetime-local"
//                 placeholder="YYYY-MM-DD HH:mm"
//                 validate={{
//                   required: { value: true, message: 'This field is required.' },
//                 }}
//               />
//               <ValidatedField label="Is Deleted" id="message-isDeleted" name="isDeleted" data-cy="isDeleted" check type="checkbox" />
//               <ValidatedField label="Created By" id="message-createdBy" name="createdBy" data-cy="createdBy" type="text" />
//               <ValidatedField
//                 label="Created Date"
//                 id="message-createdDate"
//                 name="createdDate"
//                 data-cy="createdDate"
//                 type="datetime-local"
//                 placeholder="YYYY-MM-DD HH:mm"
//               />
//               <ValidatedField
//                 label="Last Modified By"
//                 id="message-lastModifiedBy"
//                 name="lastModifiedBy"
//                 data-cy="lastModifiedBy"
//                 type="text"
//               />
//               <ValidatedField
//                 label="Last Modified Date"
//                 id="message-lastModifiedDate"
//                 name="lastModifiedDate"
//                 data-cy="lastModifiedDate"
//                 type="datetime-local"
//                 placeholder="YYYY-MM-DD HH:mm"
//               /> */}

//               {/* <ValidatedField id="message-senderProfile" name="senderProfile" data-cy="senderProfile" label="Sender Profile" type="select">
//                 <option value="" key="0" />
//                 {users
//                   ? users.map(otherEntity => (
//                       <option value={otherEntity.id} key={otherEntity.id}>
//                         {otherEntity.login}
//                       </option>
//                     ))
//                   : null}
//               </ValidatedField> */}

//               <ValidatedField
//                 id="message-receiverProfile"
//                 name="receiverProfile"
//                 data-cy="receiverProfile"
//                 label="Receiver Profile"
//                 type="select"
//               >
//                 <option value="" key="0" />
//                 {users
//                   ? users.map(otherEntity => (
//                       <option value={otherEntity.id} key={otherEntity.id}>
//                         {otherEntity.login}
//                       </option>
//                     ))
//                   : null}
//               </ValidatedField>
//               <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/message" replace color="info">
//                 <FontAwesomeIcon icon="arrow-left" />
//                 &nbsp;
//                 <span className="d-none d-md-inline">Back</span>
//               </Button>
//               &nbsp;
//               <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
//                 <FontAwesomeIcon icon="save" />
//                 &nbsp; Save
//               </Button>
//             </ValidatedForm>
//           )}
//         </Col>
//       </Row>
//     </div>
//   );
// };

// export default MessageUpdate;

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
