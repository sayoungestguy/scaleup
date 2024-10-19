// import React, { useState, useEffect } from 'react';
// import { Link, useLocation, useNavigate } from 'react-router-dom';
// import { Button, Table } from 'reactstrap';
// import { byteSize, Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
// import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
// import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
// import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
// import { useAppDispatch, useAppSelector } from 'app/config/store';
// import axios from 'axios';

// import { getEntities } from './message.reducer';

// export const Message = () => {
//   const dispatch = useAppDispatch();

//   const pageLocation = useLocation();
//   const navigate = useNavigate();

//   const [paginationState, setPaginationState] = useState(
//     overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
//   );

//   const messageList = useAppSelector(state => state.message.entities);
//   const loading = useAppSelector(state => state.message.loading);
//   const totalItems = useAppSelector(state => state.message.totalItems);
//   const [currentUserId, setCurrentUserId] = useState(null);

//   const getAllEntities = () => {
//     dispatch(
//       getEntities({
//         page: paginationState.activePage - 1,
//         size: paginationState.itemsPerPage,
//         sort: `${paginationState.sort},${paginationState.order}`,
//       }),
//     );
//   };

//   const sortEntities = () => {
//     getAllEntities();
//     const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
//     if (pageLocation.search !== endURL) {
//       navigate(`${pageLocation.pathname}${endURL}`);
//     }
//   };

//   const resetAll = () => {
//     setPaginationState(overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search));
//   };

//   // Fetch current user ID
//   useEffect(() => {
//     const fetchCurrentUserId = async () => {
//       try {
//         const response = await axios.get('/api/current-user');
//         setCurrentUserId(response.data);
//       } catch (error) {
//         console.error('Error fetching current user ID:', error);
//       }
//     };
//     fetchCurrentUserId();
//     resetAll();
//   }, []);

//   // Polling effect to refresh messages every 2 seconds
//   useEffect(() => {
//     const interval = setInterval(() => {
//       getAllEntities();
//     }, 2000); // 2000 ms = 2 seconds

//     return () => clearInterval(interval); // Cleanup on unmount
//   }, [paginationState.activePage, paginationState.sort, paginationState.order]);

//   useEffect(() => {
//     sortEntities();
//   }, [paginationState.activePage, paginationState.order, paginationState.sort]);

//   useEffect(() => {
//     const params = new URLSearchParams(pageLocation.search);
//     const page = params.get('page');
//     const sort = params.get(SORT);
//     if (page && sort) {
//       const sortSplit = sort.split(',');
//       setPaginationState({
//         ...paginationState,
//         activePage: +page,
//         sort: sortSplit[0],
//         order: sortSplit[1],
//       });
//     }
//   }, [pageLocation.search]);

//   const sort = p => () => {
//     setPaginationState({
//       ...paginationState,
//       order: paginationState.order === ASC ? DESC : ASC,
//       sort: p,
//     });
//   };

//   const handlePagination = currentPage =>
//     setPaginationState({
//       ...paginationState,
//       activePage: currentPage,
//     });

//   const handleSyncList = () => {
//     sortEntities();
//   };

//   const getSortIconByFieldName = (fieldName: string) => {
//     const sortFieldName = paginationState.sort;
//     const order = paginationState.order;
//     if (sortFieldName !== fieldName) {
//       return faSort;
//     } else {
//       return order === ASC ? faSortUp : faSortDown;
//     }
//   };

//   return (
//     <div>
//       <h2 id="message-heading" data-cy="MessageHeading">
//         Messages
//         <div className="d-flex justify-content-end">
//           <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
//             <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
//           </Button>
//           <Link to="/message/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
//             <FontAwesomeIcon icon="plus" />
//             &nbsp; Create a new Message
//           </Link>
//         </div>
//       </h2>
//       <div className="table-responsive">
//         {messageList && messageList.length > 0 ? (
//           <Table responsive>
//             <thead>
//               <tr>
//                 <th className="hand" onClick={sort('id')}>
//                   ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
//                 </th>
//                 <th className="hand" onClick={sort('content')}>
//                   Content <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
//                 </th>
//                 {/* <th className="hand" onClick={sort('sentAt')}>
//                   Sent At <FontAwesomeIcon icon={getSortIconByFieldName('sentAt')} />
//                 </th>
//                 <th className="hand" onClick={sort('isDeleted')}>
//                   Is Deleted <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
//                 </th> */}
//                 <th className="hand" onClick={sort('createdBy')}>
//                   Created By <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
//                 </th>
//                 <th className="hand" onClick={sort('createdDate')}>
//                   Created Date <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
//                 </th>
//                 {/* <th className="hand" onClick={sort('lastModifiedBy')}>
//                   Last Modified By <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
//                 </th>
//                 <th className="hand" onClick={sort('lastModifiedDate')}>
//                   Last Modified Date <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
//                 </th> */}
//                 <th>
//                   Sender Profile
//                 </th>
//                 <th>
//                   Receiver Profile
//                 </th>
//                 <th />
//               </tr>
//             </thead>
//             <tbody>
//               {messageList.map((message, i) => (
//                 <tr key={`entity-${i}`} data-cy="entityTable">
//                   <td>
//                     <Button tag={Link} to={`/message/${message.id}`} color="link" size="sm">
//                       {message.id}
//                     </Button>
//                   </td>
//                   <td>{message.content}</td>
//                   {/* <td>{message.sentAt ? <TextFormat type="date" value={message.sentAt} format={APP_DATE_FORMAT} /> : null}</td>
//                   <td>{message.isDeleted ? 'true' : 'false'}</td> */}
//                   <td>{message.createdBy}</td>
//                   <td>{message.createdDate ? <TextFormat type="date" value={message.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
//                   {/* <td>{message.lastModifiedBy}</td>
//                   <td>
//                     {message.lastModifiedDate ? <TextFormat type="date" value={message.lastModifiedDate} format={APP_DATE_FORMAT} /> : null}
//                   </td> */}
//                   <td>
//                     {message.senderProfile ? <Link to={`/user-profile/${message.senderProfile.id}`}>{message.senderProfile.id}</Link> : ''}
//                   </td>
//                   <td>
//                     {message.receiverProfile ? (
//                       <Link to={`/user-profile/${message.receiverProfile.id}`}>{message.receiverProfile.id}</Link>
//                     ) : (
//                       ''
//                     )}
//                   </td>
//                   <td className="text-end">
//                     <div className="btn-group flex-btn-group-container">
//                       <Button tag={Link} to={`/message/${message.id}`} color="info" size="sm" data-cy="entityDetailsButton">
//                         <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
//                       </Button>
//                       <Button
//                         tag={Link}
//                         to={`/message/${message.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
//                         color="primary"
//                         size="sm"
//                         data-cy="entityEditButton"
//                         disabled={currentUserId === message.receiverProfile?.id} // Disable if current user is receiver
//                       >
//                         <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
//                       </Button>
//                       <Button
//                         onClick={() =>
//                           (window.location.href = `/message/${message.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
//                         }
//                         color="danger"
//                         size="sm"
//                         data-cy="entityDeleteButton"
//                         disabled={currentUserId === message.receiverProfile?.id} // Disable if current user is receiver
//                       >
//                         <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
//                       </Button>
//                     </div>
//                   </td>
//                 </tr>
//               ))}

//             </tbody>
//           </Table>
//         ) : (
//           !loading && <div className="alert alert-warning">No Messages found</div>
//         )}
//       </div>
//       {totalItems ? (
//         <div className={messageList && messageList.length > 0 ? '' : 'd-none'}>
//           <div className="justify-content-center d-flex">
//             <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
//           </div>
//           <div className="justify-content-center d-flex">
//             <JhiPagination
//               activePage={paginationState.activePage}
//               onSelect={handlePagination}
//               maxButtons={5}
//               itemsPerPage={paginationState.itemsPerPage}
//               totalItems={totalItems}
//             />
//           </div>
//         </div>
//       ) : (
//         ''
//       )}
//     </div>
//   );
// };

// export default Message;

// import React, { useState, useEffect, useRef } from 'react';
// import { Link, useLocation, useNavigate } from 'react-router-dom';
// import { Button, Table } from 'reactstrap';
// import { byteSize, Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
// import { APP_DATE_FORMAT } from 'app/config/constants';
// import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
// import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
// import { useAppDispatch, useAppSelector } from 'app/config/store';
// import axios from 'axios';
// import { notify } from 'app/app'; // Adjust the path accordingly
// import { getEntities } from './message.reducer';

// export const Message = () => {
//   const dispatch = useAppDispatch();
//   const pageLocation = useLocation();
//   const navigate = useNavigate();

//   const [paginationState, setPaginationState] = useState(
//     overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
//   );

//   const messageList = useAppSelector(state => state.message.entities);
//   const loading = useAppSelector(state => state.message.loading);
//   const totalItems = useAppSelector(state => state.message.totalItems);
//   const [currentUserId, setCurrentUserId] = useState(null);
//   const previousCount = useRef(totalItems); // Track previous count of messages
//   const previousMessageIds = useRef(new Set()); // Use ref to track previous message IDs
//   const initialMount = useRef(true); // Track if the component is initially mounted

//   const getAllEntities = () => {
//     dispatch(
//       getEntities({
//         page: paginationState.activePage - 1,
//         size: paginationState.itemsPerPage,
//         sort: `${paginationState.sort},${paginationState.order}`,
//       }),
//     );
//   };

//   const sortEntities = () => {
//     const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
//     if (pageLocation.search !== endURL) {
//       navigate(`${pageLocation.pathname}${endURL}`);
//     }
//   };

//   // Fetch current user ID
//   useEffect(() => {
//     const fetchCurrentUserId = async () => {
//       try {
//         const response = await axios.get('/api/current-user');
//         setCurrentUserId(response.data);
//       } catch (error) {
//         console.error('Error fetching current user ID:', error);
//       }
//     };
//     fetchCurrentUserId();
//   }, []);

//   // Polling effect to refresh messages every 0.5 seconds
//   useEffect(() => {
//     const interval = setInterval(() => {
//       getAllEntities();
//     }, 500); // 500 ms = 0.5 seconds

//     return () => clearInterval(interval); // Cleanup on unmount
//   }, [paginationState.activePage, paginationState.sort, paginationState.order]);

//   useEffect(() => {
//     // Check for new messages after messages have been fetched
//     if (!initialMount.current) {
//       // Compare totalItems to see if it has increased
//       if (totalItems > previousCount.current) {
//         notify("New message received!");
//       }
//       previousCount.current = totalItems; // Update the previous count
//     } else {
//       initialMount.current = false; // Set to false after the first render
//     }

//     // Track new message IDs
//     previousMessageIds.current = new Set(messageList.map(message => message.id));
//   }, [messageList]);

//   useEffect(() => {
//     sortEntities();
//   }, [paginationState.activePage, paginationState.order, paginationState.sort]);

//   const handlePagination = currentPage =>
//     setPaginationState({
//       ...paginationState,
//       activePage: currentPage,
//     });

//   const sort = p => () => {
//     setPaginationState({
//       ...paginationState,
//       order: paginationState.order === ASC ? DESC : ASC,
//       sort: p,
//     });
//   };

//   const getSortIconByFieldName = (fieldName: string) => {
//     const sortFieldName = paginationState.sort;
//     const order = paginationState.order;
//     if (sortFieldName !== fieldName) {
//       return faSort;
//     } else {
//       return order === ASC ? faSortUp : faSortDown;
//     }
//   };

//   return (
//     <div>
//       <h2 id="message-heading" data-cy="MessageHeading">
//         Messages
//         <div className="d-flex justify-content-end">
//           {/* <Button className="me-2" color="info" onClick={getAllEntities} disabled={loading}>
//             <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
//           </Button> */}
//           <Link to="/message/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
//             <FontAwesomeIcon icon="plus" />
//             &nbsp; Create a new Message
//           </Link>
//         </div>
//       </h2>
//       <div className="table-responsive">
//         {messageList && messageList.length > 0 ? (
//           <Table responsive>
//             <thead>
//               <tr>
//                 <th className="hand" onClick={sort('id')}>
//                   ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
//                 </th>
//                 <th className="hand" onClick={sort('content')}>
//                   Content <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
//                 </th>
//                 <th className="hand" onClick={sort('createdBy')}>
//                   Created By <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
//                 </th>
//                 <th className="hand" onClick={sort('createdDate')}>
//                   Created Date <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
//                 </th>
//                 <th>Sender Profile</th>
//                 <th>Receiver Profile</th>
//                 <th />
//               </tr>
//             </thead>
//             <tbody>
//               {messageList.map((message, i) => (
//                 <tr key={`entity-${i}`} data-cy="entityTable">
//                   <td>
//                     <Button tag={Link} to={`/message/${message.id}`} color="link" size="sm">
//                       {message.id}
//                     </Button>
//                   </td>
//                   <td>{message.content}</td>
//                   <td>{message.createdBy}</td>
//                   <td>{message.createdDate ? <TextFormat type="date" value={message.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
//                   <td>
//                     {message.senderProfile ? <Link to={`/user-profile/${message.senderProfile.id}`}>{message.senderProfile.id}</Link> : ''}
//                   </td>
//                   <td>
//                     {message.receiverProfile ? (
//                       <Link to={`/user-profile/${message.receiverProfile.id}`}>{message.receiverProfile.id}</Link>
//                     ) : (
//                       ''
//                     )}
//                   </td>
//                   <td className="text-end">
//                     <div className="btn-group flex-btn-group-container">
//                       <Button tag={Link} to={`/message/${message.id}`} color="info" size="sm" data-cy="entityDetailsButton">
//                         <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
//                       </Button>
//                       <Button
//                         tag={Link}
//                         to={`/message/${message.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
//                         color="primary"
//                         size="sm"
//                         data-cy="entityEditButton"
//                         disabled={currentUserId === message.receiverProfile?.id} // Disable if current user is receiver
//                       >
//                         <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
//                       </Button>
//                       <Button
//                         onClick={() =>
//                           (window.location.href = `/message/${message.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
//                         }
//                         color="danger"
//                         size="sm"
//                         data-cy="entityDeleteButton"
//                         disabled={currentUserId === message.receiverProfile?.id} // Disable if current user is receiver
//                       >
//                         <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
//                       </Button>
//                     </div>
//                   </td>
//                 </tr>
//               ))}
//             </tbody>
//           </Table>
//         ) : (
//           !loading && <div className="alert alert-warning">No Messages found</div>
//         )}
//       </div>
//       {totalItems ? (
//         <div className={messageList && messageList.length > 0 ? '' : 'd-none'}>
//           <div className="justify-content-center d-flex">
//             <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
//           </div>
//           <div className="justify-content-center d-flex">
//             <JhiPagination
//               activePage={paginationState.activePage}
//               onSelect={handlePagination}
//               maxButtons={5}
//               itemsPerPage={paginationState.itemsPerPage}
//               totalItems={totalItems}
//             />
//           </div>
//         </div>
//       ) : (
//         ''
//       )}
//     </div>
//   );
// };

// export default Message;

import React, { useState, useEffect, useRef } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import axios from 'axios';
import { notify } from 'app/app';
import { getEntities } from './message.reducer';

export const Message = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const messageList = useAppSelector(state => state.message.entities);
  const loading = useAppSelector(state => state.message.loading);
  const totalItems = useAppSelector(state => state.message.totalItems);
  const [currentUserId, setCurrentUserId] = useState(null);
  const previousCount = useRef(totalItems); // Track previous count of messages
  const previousMessageIds = useRef(new Set()); // Use ref to track previous message IDs
  const initialMount = useRef(true); // Track if the component is initially mounted
  const [firstLoad, setFirstLoad] = useState(true); // New state to track first load

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  // Fetch current user ID
  useEffect(() => {
    const fetchCurrentUserId = async () => {
      try {
        const response = await axios.get('/api/current-user');
        setCurrentUserId(response.data);
      } catch (error) {
        console.error('Error fetching current user ID:', error);
      }
    };
    fetchCurrentUserId();
  }, []);

  // Polling effect to refresh messages every 0.5 seconds
  useEffect(() => {
    const interval = setInterval(() => {
      getAllEntities();
    }, 500); // 500 ms = 0.5 seconds

    return () => clearInterval(interval); // Cleanup on unmount
  }, [paginationState.activePage, paginationState.sort, paginationState.order]);

  // useEffect(() => {
  //   // Check for new messages after messages have been fetched
  //   if (!initialMount.current) {
  //     // Compare totalItems to see if it has increased
  //     if (totalItems > previousCount.current) {
  //       notify("New message received!");
  //     }
  //     previousCount.current = totalItems; // Update the previous count
  //   } else {
  //     initialMount.current = false; // Set to false after the first render
  //   }

  //   // Set firstLoad to false after the initial fetch
  //   if (firstLoad) {
  //     setFirstLoad(false);
  //   } else {
  //     // Track new message IDs
  //     previousMessageIds.current = new Set(messageList.map(message => message.id));
  //   }
  // }, [messageList]);

  const loadCountRef = useRef(0);

  useEffect(() => {
    if (totalItems > previousCount.current) {
      // Increment loadCount on each fetch successful
      loadCountRef.current += 1;
    }

    // Check for new messages after messages have been fetched
    if (loadCountRef.current > 1) {
      // Only check after the initial load
      // Compare totalItems to see if it has increased
      if (totalItems > previousCount.current) {
        notify('New message added to inbox!');
      }
    }

    previousCount.current = totalItems; // Update the previous count

    // Track new message IDs
    previousMessageIds.current = new Set(messageList.map(message => message.id));
  }, [messageList]);

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="message-heading" data-cy="MessageHeading">
        Messages
        <div className="d-flex justify-content-end">
          {/* <Button className="me-2" color="info" onClick={getAllEntities} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button> */}
          <Link to="/message/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Message
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {messageList && messageList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/* <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th> */}
                <th className="hand" onClick={sort('content')}>
                  Content <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  Created By <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  Created Date <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                {/* <th>Sender Profile</th>
                <th>Receiver Profile</th> */}
                <th />
              </tr>
            </thead>
            <tbody>
              {messageList.map((message, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/* <td>
                    <Button tag={Link} to={`/message/${message.id}`} color="link" size="sm">
                      {message.id}
                    </Button>
                  </td> */}
                  <td>{message.content}</td>
                  <td>{message.createdBy}</td>
                  <td>{message.createdDate ? <TextFormat type="date" value={message.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
                  {/* <td>
                    {message.senderProfile ? <Link to={`/user-profile/${message.senderProfile.id}`}>{message.senderProfile.id}</Link> : ''}
                  </td>
                  <td>
                    {message.receiverProfile ? (
                      <Link to={`/user-profile/${message.receiverProfile.id}`}>{message.receiverProfile.id}</Link>
                    ) : (
                      ''
                    )}
                  </td> */}
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/message/${message.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/message/${message.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                        disabled={currentUserId !== message.senderProfile?.id} // Disable if current user is receiver
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/message/${message.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                        disabled={currentUserId !== message.senderProfile?.id} // Disable if current user is receiver
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Messages found</div>
        )}
      </div>
      {totalItems ? (
        <div className={messageList && messageList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Message;
