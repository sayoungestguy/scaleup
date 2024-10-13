import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { getPaginationState, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getUserSkills } from '../../entities/user-skill/user-skill.reducer';
import { getEntities as getUserProfiles } from '../../entities/user-profile/user-profile.reducer';
import { getEntity } from './user-profile.reducer';
import { getSkillById as getSkillName } from 'app/entities/skill/skill.reducer';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const account = useAppSelector(state => state.authentication.account); // To fetch the account name
  const loading = useAppSelector(state => state.userProfile.loading); // Get the loading state

  const { id } = useParams<'id'>();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const [skillsAttained, setSkillsAttained] = useState([]);
  const [skillsGoals, setSkillsGoals] = useState([]);
  const [skillNames, setSkillNames] = useState({});

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);

  const getAllUserSkillsByUserId = () => {
    dispatch(
      getUserSkills({
        query: `userProfileId.equals=${id}&skillTypeId.equals=1`,
      }),
    ).then(response => {
      setSkillsAttained((response.payload as any).data);
    });

    dispatch(
      getUserSkills({
        query: `userProfileId.equals=${id}&skillTypeId.equals=2`,
      }),
    ).then(response => {
      setSkillsGoals((response.payload as any).data);
    });
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

  //To Fetch skill names from Skill Table
  // Fetch skill names for skillsAttained and skillsGoals
  useEffect(() => {
    const fetchSkillNames = async () => {
      const newSkillNames = { ...skillNames };

      // Fetch skill names for skillsAttained
      for (const userSkill of [...skillsAttained, ...skillsGoals]) {
        if (userSkill.skill && !newSkillNames[userSkill.skill.id]) {
          const response = await dispatch(getSkillName(userSkill.skill.id));
          newSkillNames[userSkill.skill.id] = (response.payload as { data: { skillName: string } }).data.skillName;
        }
      }

      setSkillNames(newSkillNames);
    };

    if (skillsAttained.length > 0 || skillsGoals.length > 0) {
      fetchSkillNames();
    }
  }, [skillsAttained, skillsGoals]);

  //**************************************************************************** */

  /*  This is the search bar for the user profile */
  const [searchQuery, setSearchQuery] = useState(''); // Track the search query
  const [filteredUsers, setFilteredUsers] = useState([]); // Store filtered user profiles

  const handleSearch = query => {
    setSearchQuery(query);
    if (query) {
      dispatch(
        getUserProfiles({
          query: `userProfileId.contains=${query}`, // Assuming search by login (username)
        }),
      ).then(response => {
        setFilteredUsers((response.payload as { data: any[] }).data); // Update filtered user profiles
      });
    } else {
      setFilteredUsers([]); // Clear when input is empty
    }
  };
  /************************************************************************/

  return (
    <div style={{ fontFamily: 'Arial, sans-serif', backgroundColor: '#f4f4f4', margin: 0, padding: 0, minHeight: '100vh', width: '100%' }}>
      <header style={{ backgroundColor: '#f4f4f4', color: 'white', padding: '15px', textAlign: 'left', width: '100%' }}>
        <div style={{ display: 'inline-block', fontSize: '1.5em' }}></div>
      </header>

      {/*  This is the search bar for the user profile */}
      <div className="d-flex justify-content-end align-items-center mb-3" style={{ gap: '10px', position: 'relative' }}>
        {/* <input
          type="text"
          className="form-control"
          placeholder="Search User's Profile"
          style={{
            borderRadius: '20px',
            padding: '10px 20px',
            border: '1px solid #ced4da',
            boxShadow: '0px 0px 5px rgba(0, 0, 0, 0.1)',
            width: '300px',
          }}
          value={searchQuery}
          onChange={e => handleSearch(e.target.value)} // Trigger search onChange
        />
        <Button
          className="me-2"
          color="info"
          onClick={() => handleSearch(searchQuery)}
          disabled={loading}
          style={{
            borderRadius: '20px',
            padding: '10px 20px',
            display: 'flex',
            alignItems: 'center',
            gap: '5px',
          }}
        >
          <FontAwesomeIcon icon="search" spin={loading} /> Search
        </Button> */}

        {/* Scrollable dropdown for search results */}
        {searchQuery && filteredUsers.length === 0 && !loading && (
          <div
            style={{
              maxHeight: '200px',
              overflowY: 'auto',
              marginTop: '10px',
              width: '300px',
              border: '1px solid #ced4da',
              borderRadius: '10px',
              position: 'absolute',
              top: '50px',
              zIndex: 1000,
              backgroundColor: 'white',
              padding: '10px',
              textAlign: 'center',
            }}
          >
            <p>No Results</p>
          </div>
        )}

        {filteredUsers.length > 0 && (
          <div
            style={{
              maxHeight: '200px',
              overflowY: 'auto',
              marginTop: '10px',
              width: '300px',
              border: '1px solid #ced4da',
              borderRadius: '10px',
              position: 'absolute',
              top: '50px',
              zIndex: 1000,
              backgroundColor: 'white',
            }}
          >
            <ul className="list-group">
              {filteredUsers.map(user => (
                <li key={user.id} className="list-group-item" style={{ cursor: 'pointer' }}>
                  <Link to={`/user-profile/${user.id}`} onClick={() => setSearchQuery(user.id)}>
                    {user.nickname}
                  </Link>
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>
      {/*  end of search bar for the user profile */}

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
        <div
          style={{
            width: '95%',
            maxWidth: '1400px',
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'flex-start',
            gap: '20px',
          }}
        >
          <div
            style={{
              flex: 1,
              backgroundColor: 'white',
              padding: '200px',
              borderRadius: '100px',
              boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)',
              textAlign: 'center',
              alignSelf: 'flex-start',
            }}
          >
            <div
              style={{
                width: '300px', // Increase container width
                height: '300px', // Increase container height
                borderRadius: '50%', // Make container circular
                marginBottom: '30px',
                backgroundColor: '#f4f4f4',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                margin: '0 auto',
              }}
            >
              {!userProfileEntity?.profilePicture ? (
                // If no profile picture, show FontAwesomeIcon
                <FontAwesomeIcon icon="user" style={{ fontSize: '100px', color: '#555' }} />
              ) : (
                // Otherwise, show the profile picture
                <img
                  src={
                    userProfileEntity?.profilePicture === 'Male Profile Picture'
                      ? 'https://images2.imgbox.com/58/df/QqnDcnpM_o.png'
                      : 'https://images2.imgbox.com/b3/e3/eB7ZJyVa_o.png'
                  }
                  alt="Profile Picture"
                  style={{
                    width: '280px',
                    height: '280px',
                    borderRadius: '50%',
                  }}
                />
              )}
            </div>

            <p>{userProfileEntity.profilePicture}</p>
            <div style={{ padding: '15px', textAlign: 'left', width: '100%' }}></div>
            <h2>{userProfileEntity.user ? userProfileEntity.user.login : ''}</h2>

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
                <strong>Socials:</strong>{' '}
                {userProfileEntity?.socialLinks ? (
                  <a
                    href={
                      userProfileEntity.socialLinks.startsWith('http')
                        ? userProfileEntity.socialLinks
                        : `https://${userProfileEntity.socialLinks}`
                    }
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    {userProfileEntity.socialLinks}
                  </a>
                ) : (
                  'No social links available'
                )}
              </p>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '20px' }}>
                <p>
                  <strong>Job Role:</strong> {userProfileEntity?.jobRole}
                </p>
              </div>
            </div>
            <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} color="danger" block>
              Edit Profile
            </Button>
          </div>

          {/* Skills Attained Section */}
          <div style={{ flex: 2, display: 'flex', flexDirection: 'column', gap: '20px' }}>
            <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '10px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
              <h3 style={{ marginBottom: '20px' }}>Skills Attained</h3>

              {/* Scrollable Table Container */}
              <div style={{ maxHeight: '250px', overflowY: 'auto' }}>
                <div className="table-responsive">
                  {skillsAttained && skillsAttained.length > 0 ? (
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
                            Skill <FontAwesomeIcon icon="sort" />
                          </th>
                          <th />
                        </tr>
                      </thead>
                      <tbody>
                        {skillsAttained.map((userSkill, i) => (
                          <tr key={`entity-${i}`} data-cy="entityTable">
                            <td>
                              <Button tag={Link} to={`/user-skill/${userSkill.id}`} color="link" size="sm">
                                {userSkill.id}
                              </Button>
                            </td>
                            <td style={{ textAlign: 'center', verticalAlign: 'middle' }}>{userSkill.yearsOfExperience}</td>
                            <td>
                              {userSkill.skill ? (
                                <Link to={`/skill/${userSkill.skill.id}`}>{skillNames[userSkill.skill.id] || 'Loading...'}</Link>
                              ) : (
                                ''
                              )}
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
              <Button tag={Link} to={`/user-skill/new`} color="primary" block>
                Add Skill
              </Button>
            </div>

            {/* Skills Goals Section */}
            <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '10px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
              <h3 style={{ marginBottom: '20px' }}>Skills Goals</h3>

              {/* Scrollable Table Container */}
              <div style={{ maxHeight: '250px', overflowY: 'auto' }}>
                <div className="table-responsive">
                  {skillsGoals && skillsGoals.length > 0 ? (
                    <Table responsive>
                      <thead>
                        <tr>
                          <th className="hand" onClick={sort('id')}>
                            ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                          </th>
                          <th className="hand" onClick={sort('yearsOfExperience')}></th>
                          <th>
                            Skill <FontAwesomeIcon icon="sort" />
                          </th>
                          <th />
                        </tr>
                      </thead>
                      <tbody>
                        {skillsGoals.map((userSkill, i) => (
                          <tr key={`entity-${i}`} data-cy="entityTable">
                            <td>
                              <Button tag={Link} to={`/user-skill/${userSkill.id}`} color="link" size="sm">
                                {userSkill.id}
                              </Button>
                            </td>
                            <td>{}</td>
                            <td>
                              {userSkill.skill ? (
                                <Link to={`/skill/${userSkill.skill.id}`}>{skillNames[userSkill.skill.id] || 'Loading...'}</Link>
                              ) : (
                                ''
                              )}
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
              <Button tag={Link} to={`/user-skill/new`} color="primary" block>
                Add Skill
              </Button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default UserProfileDetail;
