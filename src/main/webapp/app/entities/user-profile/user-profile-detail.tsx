import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { getPaginationState, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from '../../entities/user-skill/user-skill.reducer';
import { getEntity } from './user-profile.reducer';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const account = useAppSelector(state => state.authentication.account); // To fetch the account name
  const loading = useAppSelector(state => state.userProfile.loading); // Get the loading state

  const { id } = useParams<'id'>();

  const userSkillList = useAppSelector(state => state.userSkill.entities);

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const getAllUserSkillsByUserId = () => {
    dispatch(
      getEntities({
        query: `userProfileId.equals=${id}&skillTypeId.equals=3`,
        //query: `skillTypeId.equals=4`,
      }),
    );
  };

  const sortEntities = () => {
    getAllUserSkillsByUserId();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
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

  const userSkillEntity = useAppSelector(state => state.userSkill.entity);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);

  const [attainedSkills, setAttainedSkills] = useState(['.NET', 'Angular']);
  const [goalSkills, setGoalSkills] = useState(['JavaScript', 'React', 'Java', 'Python']);
  const [newAttainedSkill, setNewAttainedSkill] = useState(''); // State for Skills Attained input
  const [newGoalSkill, setNewGoalSkill] = useState(''); // State for Skills Goals input

  const addSkill = type => {
    if (type === 'attained' && newAttainedSkill.trim() !== '') {
      setAttainedSkills([...attainedSkills, newAttainedSkill]);
      setNewAttainedSkill(''); // Clear the input after adding
    }

    if (type === 'goal' && newGoalSkill.trim() !== '') {
      setGoalSkills([...goalSkills, newGoalSkill]);
      setNewGoalSkill(''); // Clear the input after adding
    }
  };

  const removeSkill = (type, skill) => {
    if (type === 'attained') {
      setAttainedSkills(attainedSkills.filter(s => s !== skill));
    } else {
      setGoalSkills(goalSkills.filter(s => s !== skill));
    }
  };

  return (
    <div style={{ fontFamily: 'Arial, sans-serif', backgroundColor: '#f4f4f4', margin: 0, padding: 0, minHeight: '100vh', width: '100%' }}>
      <header style={{ backgroundColor: '#3A83C7', color: 'white', padding: '15px', textAlign: 'left', width: '100%' }}>
        <div style={{ display: 'inline-block', fontSize: '1.5em' }}>Scaleup</div>
      </header>

      <main
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'flex-start',
          padding: '20px 0',
          minHeight: 'calc(100vh - 60px)',
          width: '100%',
        }}
      >
        <div style={{ width: '95%', maxWidth: '1400px', display: 'flex', justifyContent: 'space-between', gap: '20px' }}>
          <div
            style={{
              flex: 1,
              backgroundColor: 'white',
              padding: '200px',
              borderRadius: '100px',
              boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)',
              textAlign: 'center',
            }}
          >
            <img
              src="https://wallpapers-clan.com/wp-content/uploads/2022/05/meme-pfp-34.jpg"
              alt="Profile Picture"
              style={{ width: '150px', height: '150px', borderRadius: '50%', marginBottom: '20px' }}
            />
            <h2>{userProfileEntity.user ? userProfileEntity.user.login : ''}</h2>

            <h2>{userProfileEntity.id}</h2>

            <h2>{account.id}</h2>

            <h2>{userProfileEntity.login}</h2>

            {loading ? (
              <p>Loading...</p>
            ) : (
              <p>
                <strong>About Me:</strong> {userProfileEntity?.aboutMe || 'No profile data available'}
              </p>
            )}

            <div style={{ marginTop: '20px', textAlign: 'left' }}>
              <p>
                <strong>Email:</strong> {account.email}
              </p>
              <p>
                <strong>Phone:</strong> (123) 456-7890
              </p>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '20px' }}>
                <a href="#facebook" style={{ textDecoration: 'none', margin: '0 10px', fontSize: '1.5em', color: '#555' }}>
                  FB
                </a>
                <a href="#twitter" style={{ textDecoration: 'none', margin: '0 10px', fontSize: '1.5em', color: '#555' }}>
                  TW
                </a>
                <a href="#linkedin" style={{ textDecoration: 'none', margin: '0 10px', fontSize: '1.5em', color: '#555' }}>
                  LI
                </a>
                <a href="#instagram" style={{ textDecoration: 'none', margin: '0 10px', fontSize: '1.5em', color: '#555' }}>
                  IG
                </a>
                <a href="#google" style={{ textDecoration: 'none', margin: '0 10px', fontSize: '1.5em', color: '#555' }}>
                  G+
                </a>
                <a href="#pinterest" style={{ textDecoration: 'none', margin: '0 10px', fontSize: '1.5em', color: '#555' }}>
                  PT
                </a>
              </div>
            </div>
            <Button color="danger" block>
              Edit Profile
            </Button>
          </div>

          <div style={{ flex: 2, display: 'flex', flexDirection: 'column', justifyContent: 'space-between', gap: '20px' }}>
            <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '10px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
              <h3 style={{ marginBottom: '20px' }}>Skills Attained</h3>
              {/* <input
                type="text"
                value={newAttainedSkill}
                onChange={e => setNewAttainedSkill(e.target.value)}
                placeholder="Type a Skill Here"
                style={{ width: '100%', padding: '10px', marginBottom: '10px', borderRadius: '5px', border: '1px solid #ccc' }}
              />
              <Button color="primary" block onClick={() => addSkill('attained')}>
                Add Skill
              </Button>
              <ul style={{ paddingLeft: '20px', marginTop: '20px' }}>
                {attainedSkills.map(skill => (
                  <li key={skill} style={{ margin: '10px 0', display: 'flex', justifyContent: 'space-between' }}>
                    <span>{skill}</span>
                    <Button color="danger" onClick={() => removeSkill('attained', skill)}>
                      Remove
                    </Button>
                  </li>
                ))}
              </ul> */}

              <div className="table-responsive">
                {userSkillList && userSkillList.length > 0 ? (
                  <Table responsive>
                    <thead>
                      <tr>
                        <th className="hand" onClick={sort('id')}>
                          ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                        </th>
                        <th className="hand" onClick={sort('yearsOfExperience')}>
                          Years Of Experience <FontAwesomeIcon icon={getSortIconByFieldName('yearsOfExperience')} />
                        </th>
                        <th>
                          User Profile <FontAwesomeIcon icon="sort" />
                        </th>
                        <th>
                          Skill <FontAwesomeIcon icon="sort" />
                        </th>
                        <th>
                          Skill Type <FontAwesomeIcon icon="sort" />
                        </th>
                        <th />
                      </tr>
                    </thead>
                    <tbody>
                      {userSkillList.map((userSkill, i) => (
                        <tr key={`entity-${i}`} data-cy="entityTable">
                          <td>
                            <Button tag={Link} to={`/user-skill/${userSkill.id}`} color="link" size="sm">
                              {userSkill.id}
                            </Button>
                          </td>
                          <td>{userSkill.yearsOfExperience}</td>
                          <td>
                            {userSkill.userProfile ? (
                              <Link to={`/user-profile/${userSkill.userProfile.id}`}>{userSkill.userProfile.id}</Link>
                            ) : (
                              ''
                            )}
                          </td>
                          <td>{userSkill.skill ? <Link to={`/skill/${userSkill.skill.id}`}>{userSkill.skill.id}</Link> : ''}</td>
                          <td>
                            {userSkill.skillType ? <Link to={`/code-tables/${userSkill.skillType.id}`}>{userSkill.skillType.id}</Link> : ''}
                          </td>
                          <td className="text-end">
                            <div className="btn-group flex-btn-group-container">
                              <Button tag={Link} to={`/user-skill/${userSkill.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                                <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                              </Button>
                              <Button
                                tag={Link}
                                to={`/user-skill/${userSkill.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                                color="primary"
                                size="sm"
                                data-cy="entityEditButton"
                              >
                                <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                              </Button>
                              <Button
                                onClick={() =>
                                  (window.location.href = `/user-skill/${userSkill.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                                }
                                color="danger"
                                size="sm"
                                data-cy="entityDeleteButton"
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
                  !loading && <div className="alert alert-warning">No User Skills found</div>
                )}
              </div>
            </div>

            <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '10px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
              <h3 style={{ marginBottom: '20px' }}>Skills Goals</h3>
              <input
                type="text"
                value={newGoalSkill}
                onChange={e => setNewGoalSkill(e.target.value)}
                placeholder="Type a Skill Here"
                style={{ width: '100%', padding: '10px', marginBottom: '10px', borderRadius: '5px', border: '1px solid #ccc' }}
              />
              <Button color="primary" block onClick={() => addSkill('goal')}>
                Add Skill
              </Button>
              <ul style={{ paddingLeft: '20px', marginTop: '20px' }}>
                {goalSkills.map(skill => (
                  <li key={skill} style={{ margin: '10px 0', display: 'flex', justifyContent: 'space-between' }}>
                    <span>{skill}</span>
                    <Button color="danger" onClick={() => removeSkill('goal', skill)}>
                      Remove
                    </Button>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

// original return statement
// return (
//   <Row>
//     <Col md="8">
//       <h2 data-cy="userProfileDetailsHeading">User Profile</h2>
//       <dl className="jh-entity-details">
//         <dt>
//           <span id="id">ID</span>
//         </dt>
//         <dd>{userProfileEntity.id}</dd>
//         <dt>
//           <span id="nickname">Nickname</span>
//         </dt>
//         <dd>{userProfileEntity.nickname}</dd>
//         <dt>
//           <span id="jobRole">Job Role</span>
//         </dt>
//         <dd>{userProfileEntity.jobRole}</dd>
//         <dt>
//           <span id="aboutMe">About Me</span>
//         </dt>
//         <dd>{userProfileEntity.aboutMe}</dd>
//         <dt>
//           <span id="profilePicture">Profile Picture</span>
//         </dt>
//         <dd>{userProfileEntity.profilePicture}</dd>
//         <dt>
//           <span id="socialLinks">Social Links</span>
//         </dt>
//         <dd>{userProfileEntity.socialLinks}</dd>
//         <dt>
//           <span id="createdBy">Created By</span>
//         </dt>
//         <dd>{userProfileEntity.createdBy}</dd>
//         <dt>
//           <span id="createdDate">Created Date</span>
//         </dt>
//         <dd>
//           {userProfileEntity.createdDate ? (
//             <TextFormat value={userProfileEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
//           ) : null}
//         </dd>
//         <dt>
//           <span id="lastModifiedBy">Last Modified By</span>
//         </dt>
//         <dd>{userProfileEntity.lastModifiedBy}</dd>
//         <dt>
//           <span id="lastModifiedDate">Last Modified Date</span>
//         </dt>
//         <dd>
//           {userProfileEntity.lastModifiedDate ? (
//             <TextFormat value={userProfileEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
//           ) : null}
//         </dd>
//         <dt>User</dt>
//         <dd>{userProfileEntity.user ? userProfileEntity.user.login : ''}</dd>
//       </dl>
//       <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
//         <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
//       </Button>
//       &nbsp;
//       <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
//         <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
//       </Button>
//     </Col>
//   </Row>
// );

export default UserProfileDetail;
