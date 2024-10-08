// src/config/rootReducer.ts

import { combineReducers } from 'redux';

// Example reducers (replace with actual logic in your app)
const userReducer = (state = { isAuthenticated: false }, action) => {
  switch (action.type) {
    case 'LOGIN':
      return { ...state, isAuthenticated: true };
    case 'LOGOUT':
      return { ...state, isAuthenticated: false };
    default:
      return state;
  }
};

const authenticationReducer = (state = { account: null, isAuthenticated: false }, action) => {
  switch (action.type) {
    case 'LOGIN':
      return { ...state, account: action.payload, isAuthenticated: true };
    case 'LOGOUT':
      return { ...state, account: null, isAuthenticated: false };
    default:
      return state;
  }
};

const userProfileReducer = (state = { entities: [] }, action) => {
  switch (action.type) {
    default:
      return state;
  }
};

// Combine all reducers into the root reducer
const rootReducer = combineReducers({
  user: userReducer, // Example user reducer
  authentication: authenticationReducer, // Add the authentication reducer here
  userProfile: userProfileReducer, // Add the user profile reducer here
});

export default rootReducer;
