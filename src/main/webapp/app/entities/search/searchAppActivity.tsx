import React, { useEffect, useState } from 'react';
import { Button, Card, CardBody, Col, Form, FormGroup, Input, Label, Row, Table } from 'reactstrap';
import { useAppDispatch } from 'app/config/store';
import { getEntities } from 'app/entities/user-profile/user-profile.reducer';
import { Link, useLocation } from 'react-router-dom';
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

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const [searchType, setSearchType] = useState<string>('');
  const [searchTracking, setSearchTracking] = useState<string>('');
  const [query, setQuery] = useState<string>('');
  const [results, setResults] = useState<any[]>([]); // Expecting an array of IUserProfile
  // to store results
  const [loading, setLoading] = useState<boolean>(false); // to manage loading state
  const [error, setError] = useState<string | null>(null); // for error handling
  const [activityResults, setActivityResults] = useState<any[]>([]); // To store linked activities

  // Validation state
  const [inputError, setInputError] = useState<string | null>(null);

  const fetchUserProfile = async (searchQuery: string) => {
    const response = await dispatch(
      getEntities({
        sort: `${paginationState.sort},${paginationState.order}`,
        query: `nickname.contains=${searchQuery}`,
      }),
    ).unwrap();

    //Filter out Admin User
    return response.data.filter(profile => profile.id !== 1);
  };

  const fetchUserSkills = async (searchQuery: string) => {
    const response = await dispatch(
      getAllSkills({
        sort: `${paginationState.sort},${paginationState.order}`,
        query: `skillName.contains=${searchQuery}`,
      }),
    ).unwrap();

    // Return the skills array from the response
    return response.data;
  };

  const fetchActivitiesByUserProfile = async (userProfileId: number) => {
    const response = await dispatch(
      getAllActivity({
        sort: `${paginationState.sort},${paginationState.order}`,
        query: `creatorProfileId.equals=${userProfileId}`,
      }),
    ).unwrap();

    // Return the activities array from the response
    return response.data;
  };

  const fetchActivitiesByUserSkill = async (skillId: number) => {
    const response = await dispatch(
      getAllActivity({
        sort: `${paginationState.sort},${paginationState.order}`,
        query: `skillId.equals=${skillId}`, // Assuming 'skillId' is the field in the activity entity
      }),
    ).unwrap();

    // Return the activities array from the response
    return response.data;
  };

  const handleSearch = async (): Promise<void> => {
    if (!searchType || !query) {
      setInputError('Please select a search type and enter a query.');
      return;
    }
    setLoading(true);
    setError(null); // Reset any previous error
    setResults([]); // Clear previous results

    try {
      if (searchType === 'userSkills') {
        // API call for user skills

        const userSkills = await fetchUserSkills(query);
        setResults(userSkills);

        // Fetch activities linked to each skill
        const activityPromises = userSkills.map(skill => fetchActivitiesByUserSkill(skill.id));
        const activities = await Promise.all(activityPromises);
        const flattenedActivities = activities.flat();
        // Add skillName to the activity object by matching skillId with results
        const activitiesWithSkillName = flattenedActivities.map(activity => {
          const relatedSkill = userSkills.find(skill => skill.id === activity.skill.id);
          return {
            ...activity,
            skillName: relatedSkill ? relatedSkill.skillName : 'Unknown Skill', // Add the skill name
          };
        });
        setActivityResults(activitiesWithSkillName); // Update the activity results with skill names
      } else if (searchType === 'userProfile') {
        // API call for user profile
        const userProfiles = await fetchUserProfile(query);
        setResults(userProfiles);

        // Fetch activities linked to each user profile
        const activityPromises = userProfiles.map(profile => fetchActivitiesByUserProfile(profile.id));
        const activities = await Promise.all(activityPromises);
        const flattenedActivities = activities.flat();
        // Add skillName to the activity object by matching skillId with results
        const activitiesWithProfileName = flattenedActivities.map(activity => {
          const relatedProfiles = userProfiles.find(profile => profile.id === activity.creatorProfile.id);
          return {
            ...activity,
            creatorProfileName: relatedProfiles ? relatedProfiles.nickname : 'Unknown Name', // Add the skill name
          };
        });
        setActivityResults(activitiesWithProfileName); // Update the activity results with skill names
      }
      setSearchTracking(searchType);
    } catch (err) {
      setError('Failed to fetch data.');
    } finally {
      setLoading(false);
    }
  };

  // Effect to clear results when the user comes back to the tab
  useEffect(() => {
    setResults([]); // Clear the results when user navigates back
    setActivityResults([]); // Also clear activity results
  }, []);

  return (
    <>
      <h2 id="search-app-activity-heading" data-cy="SearchAppActivityHeading">
        Search ScaleUp Network
      </h2>
      <Card className="p-3 m-2">
        <CardBody>
          <p>Use this page to discover new people and new skills with related activities that you can potentially learn!</p>
        </CardBody>
      </Card>
      <Form className="border border-5 p-3 m-2">
        <Row>
          <Col md={6}>
            <FormGroup controlId="searchType">
              <Label>Select Search Type</Label>
              <Input as="select" type="select" value={searchType} onChange={e => setSearchType(e.target.value)} required>
                <option value="" disabled>
                  Please Select -
                </option>
                <option value="userSkills">User Skills</option>
                <option value="userProfile">User Profile</option>
              </Input>
            </FormGroup>
          </Col>
          <Col md={6}>
            <FormGroup controlId="searchQuery">
              <Label>Enter Search Query</Label>
              <Input
                type="text"
                value={query}
                onChange={e => setQuery(e.target.value)}
                placeholder="Enter search query..."
                required
                as="text"
              />
            </FormGroup>
          </Col>
        </Row>

        {inputError && <p style={{ color: 'red' }}>{inputError}</p>}

        <Button
          color="primary"
          onClick={() => {
            handleSearch();
          }}
          style={{ padding: '5px 10px' }}
          disabled={!searchType || !query} // Disable the button if validation fails
        >
          {loading ? 'Searching...' : 'Search'}
        </Button>
      </Form>
      <div className="search-results-div">
        {error && <p style={{ color: 'red' }}>{error}</p>}

        {results.length > 0 && !!searchTracking ? (
          <div className="search-results-div border border-5 p-3 m-2">
            <h3>Results</h3>
            {searchTracking === 'userProfile' ? (
              <Row className="p-3 m-2">
                {results.map((userProfile, i) => (
                  <CommonNameCard
                    key={i}
                    id={userProfile.id}
                    profilePic={userProfile.profilePicture}
                    nickname={userProfile.nickname}
                    jobRole={userProfile.jobRole}
                  />
                ))}
              </Row>
            ) : (
              <div className="table-responsive ">
                <Table responsive>
                  <thead>
                    <tr>
                      <th>S/No.</th>
                      <th>Skill Name</th>
                    </tr>
                  </thead>
                  <tbody>
                    {results.map((skill, i) => (
                      <tr key={`entity-${i}`} data-cy="entityTable">
                        <td>{skill.id}</td>
                        <td>{skill.skillName}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </div>
            )}
          </div>
        ) : results.length <= 0 && searchTracking ? (
          !loading && <div className="alert alert-warning">No Related Results found</div>
        ) : (
          ''
        )}

        {searchTracking ? (
          activityResults.length > 0 && searchTracking ? (
            <div className="table-responsive border border-5 p-3 m-2">
              <h3>Related Activities</h3>
              <Table responsive bordered>
                <thead>
                  <tr>
                    <th>S/No.</th>
                    <th className="hand">Activity Name</th>
                    <th className="hand">Activity Time</th>
                    <th className="hand">Duration</th>
                    <th className="hand">Venue</th>
                    <th className="hand">Details</th>
                    {searchTracking === 'userSkills' ? <th>Skill</th> : <th>Creator Profile</th>}
                  </tr>
                </thead>
                <tbody>
                  {activityResults.map((activity, i) => (
                    <tr key={`entity-${i}`} data-cy="entityTable">
                      <td>{i + 1}</td>
                      <td>
                        <Button tag={Link} to={`/activity/${activity.id}`} color="link" size="sm">
                          {activity.activityName}
                        </Button>
                      </td>
                      <td>
                        {activity.activityTime ? <TextFormat type="date" value={activity.activityTime} format={APP_DATE_FORMAT} /> : null}
                      </td>
                      <td>{activity.duration}</td>
                      <td>{activity.venue}</td>
                      <td>{activity.details}</td>
                      {searchTracking === 'userSkills' ? <td>{activity.skillName}</td> : <td>{activity.creatorProfileName}</td>}
                    </tr>
                  ))}
                </tbody>
              </Table>
            </div>
          ) : (
            !loading && <div className="alert alert-warning">No Related Activities found</div>
          )
        ) : (
          ''
        )}
      </div>
    </>
  );
};

export default SearchAppActivity;
