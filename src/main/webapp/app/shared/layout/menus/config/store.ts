import { configureStore } from '@reduxjs/toolkit';
import rootReducer from './rootReducer'; // Ensure the path is correct

// Create the store with the root reducer and middleware
export const getStore = () => {
  const store = configureStore({
    reducer: rootReducer, // Root reducer for all combined reducers
    middleware: getDefaultMiddleware => getDefaultMiddleware({ serializableCheck: false }), // Use default middleware without additional thunk
    devTools: process.env.NODE_ENV !== 'production', // Enable Redux DevTools only in development mode
  });

  return store;
};
