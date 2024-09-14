import React, { useState, useEffect } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import { Button } from 'reactstrap';
import { getPaginationState } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities, getEntityByCreatedBy, reset } from './user-profile.reducer';
import { getEntity } from './user-profile.reducer';

export const ProfilePage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account); // To fetch the account name
  const loading = useAppSelector(state => state.userProfile.loading); // Get the loading state
  const { username } = useParams<'username'>(); // Get the username from the URL

  const userProfileEntity = useAppSelector(state => state.userProfile.entity); // Get the fetched user profile entity

  useEffect(() => {
    dispatch(
      getEntityByCreatedBy({
        query: `createdBy.equals=${account.login}`,
      }),
    );
  }, [dispatch, account.login]);

  useEffect(() => {
    dispatch(
      getEntity({
        query: `createdBy.equals=${account.login}`,
      }),
    );
  }, []);

  // Check the fetched data in the console (optional, for debugging purposes)
  useEffect(() => {
    console.log('Fetched user profile entity:', userProfileEntity);
  }, [userProfileEntity]);

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

            <h2>User Profile Name: {userProfileEntity.user ? userProfileEntity.user.login : ''}</h2>

            <h2>User Profile ID: {userProfileEntity.id}</h2>

            <h2>Account Name: {account.login}</h2>

            <h2>Account ID: {account.id}</h2>

            <h2>User Profile Login Name:{userProfileEntity.login}</h2>

            {loading ? (
              <p>Loading...</p>
            ) : (
              <p>
                <strong>About Me:</strong> {userProfileEntity?.aboutMe || 'No profile data available'}
              </p>
            )}

            <div style={{ marginTop: '20px', textAlign: 'left' }}>
              <p>
                <strong>Email:</strong>
                {userProfileEntity.email}
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
              <input
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
              </ul>
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

export default ProfilePage;
