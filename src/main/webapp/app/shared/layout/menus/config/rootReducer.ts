// src/config/rootReducer.ts

import { combineReducers } from 'redux';

// Example: A simple reducer for user authentication (replace with actual logic)
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

// Combine all reducers into the root reducer
const rootReducer = combineReducers({
  user: userReducer, // Add more reducers here as your app grows
});

export default rootReducer;
