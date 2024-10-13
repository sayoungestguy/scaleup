import React, { useState } from 'react';
import { Button, Card, CardBody, CardText, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/user-profile/user-profile.reducer';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getPaginationState, TextFormat } from 'react-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { getAllSkills } from 'app/entities/skill/skill.reducer';
import { getAllActivity } from 'app/entities/activity/activity.reducer';
import CommonNameCard from 'app/entities/search/commonNameCard';

export const SearchAppActivity = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const [searchType, setSearchType] = useState<string>('activity');
  const [query, setQuery] = useState<string>('');
  const [results, setResults] = useState<any[]>([]); // to store results
  const [loading, setLoading] = useState<boolean>(false); // to manage loading state
  const [error, setError] = useState<string | null>(null); // for error handling

  // Profile Loading
  const userProfileList = useAppSelector(state => state.userProfile.entities);
  const loadingUserProfile = useAppSelector(state => state.userProfile.loading);
  const totalUserProfItems = useAppSelector(state => state.userProfile.totalItems);

  // Skill Loading
  const skillList = useAppSelector(state => state.skill.entities);
  const skillLoading = useAppSelector(state => state.skill.loading);
  const skillTotalItems = useAppSelector(state => state.skill.totalItems);

  // Activity Loading
  const activityList = useAppSelector(state => state.activity.entities);
  const activityLoading = useAppSelector(state => state.activity.loading);

  // Example function to fetch activities (integrating the API call)
  const fetchUserProfile = async (searchQuery: string) => {
    return new Promise<any[]>(resolve => {
      dispatch(
        getEntities({
          sort: `${paginationState.sort},${paginationState.order}`,
          query: `nickname.contains=${searchQuery}`,
        }),
      );
    });
  };

  // Simulated API calls for other types
  const fetchUserSkills = async (searchQuery: string) => {
    return new Promise<any[]>(resolve => {
      dispatch(
        getAllSkills({
          sort: `${paginationState.sort},${paginationState.order}`,
          query: `skillname.contains=${searchQuery}`,
        }),
      );
    });
  };

  // Function to fetch activities linked to a specific user profile
  const fetchActivitiesByUserProfile = async (userProfileId: number) => {
    return new Promise<any[]>(resolve => {
      dispatch(
        getAllActivity({
          sort: `${paginationState.sort},${paginationState.order}`,
          query: `userProfileId.equals=${userProfileId}`, // Assuming 'userProfileId' is the field in the activity entity
        }),
      );
    });
  };

  // Function to fetch activities linked to a specific user skill
  const fetchActivitiesByUserSkill = async (skillId: number) => {
    return new Promise<any[]>(resolve => {
      dispatch(
        getAllActivity({
          sort: `${paginationState.sort},${paginationState.order}`,
          query: `skillId.equals=${skillId}`, // Assuming 'skillId' is the field in the activity entity
        }),
      );
    });
  };

  const handleSearch = async (): Promise<void> => {
    // Add your search logic here
    // console.log(`Searching for ${query} in ${searchType}`);
    setLoading(true);
    setError(null); // Reset any previous error
    setResults([]); // Clear previous results

    try {
      if (searchType === 'userSkills') {
        // API call for user skills
        const userSkills = await fetchUserSkills(query);
        setResults(userSkills);
      } else if (searchType === 'userProfile') {
        // API call for user profile
        const response = await fetchUserProfile(query);
        // console.log("Search User Profile")
        setResults(response);
      }
    } catch (err) {
      setError('Failed to fetch data.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <h2 id="search-app-activity-heading" data-cy="SearchAppActivityHeading">
        Search For
      </h2>
      <div className="search-div border border-5 p-3 m-2">
        <select value={searchType} onChange={e => setSearchType(e.target.value)} style={{ padding: '5px' }}>
          <option value="" disabled>
            Please Select -
          </option>
          <option value="userSkills">User Skills</option>
          <option value="userProfile">User Profile</option>
        </select>
        <input type="text" value={query} onChange={e => setQuery(e.target.value)} placeholder="Enter search query..." />
        <Button
          color="primary"
          data-cy="entityEditButton"
          onClick={() => {
            handleSearch();
          }}
          style={{ padding: '5px 10px' }}
        >
          Search
        </Button>
      </div>
      <div className="search-results-div">
        {error && <p style={{ color: 'red' }}>{error}</p>}

        {/* {results && results.length > 0 ? */}
        <div className="border border-5 p-3 m-2">
          <h3>Results</h3>
          {/* Show search Results */}
          {userProfileList && userProfileList.length > 0 ? (
            <div className="flex flex-row">
              {userProfileList.map((userProfile, i) => (
                <CommonNameCard
                  key={i}
                  id={userProfile.id}
                  profilePic={userProfile.profilePicture}
                  nickname={userProfile.nickname}
                  jobRole={userProfile.jobRole}
                />
              ))}
            </div>
          ) : (
            <div className="table-responsive">
              {skillList && skillList.length > 0 ? (
                <Table responsive>
                  <thead>
                    <tr>
                      <th>ID </th>
                      <th> Skill Name </th>
                    </tr>
                  </thead>
                  <tbody>
                    {skillList.map((skill, i) => (
                      <tr key={`entity-${i}`} data-cy="entityTable">
                        <td>
                          <Button tag={Link} to={`/skill/${skill.id}`} color="link" size="sm">
                            {skill.id}
                          </Button>
                        </td>
                        <td>{skill.skillName}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : (
                !loading && <div className="alert alert-warning">No Results found</div>
              )}
            </div>
          )}
        </div>

        {results.length === 0 && !loading && (
          <p>
            No results found for {query} in {searchType}
          </p>
        )}
      </div>
    </>
  );
};

export default SearchAppActivity;
