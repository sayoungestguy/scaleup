import axios from 'axios';
import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';

import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  resetPasswordSuccess: false,
  resetPasswordFailure: false,
  successMessage: null,
};

export type PasswordResetState = Readonly<typeof initialState>;

const apiUrl = 'api/account/reset-password';
// Actions

export const handlePasswordResetInit = createAsyncThunk(
  'passwordReset/reset_password_init',
  // If the content-type isn't set that way, axios will try to encode the body and thus modify the data sent to the server.
  async (mail: string) => axios.post(`${apiUrl}/init`, mail, { headers: { ['Content-Type']: 'text/plain' } }),
  { serializeError: serializeAxiosError },
);

export const handlePasswordResetFinish = createAsyncThunk(
  'passwordReset/reset_password_finish',
  async (data: { key: string; newPassword: string }) => axios.post(`${apiUrl}/finish`, data),
  { serializeError: serializeAxiosError },
);

export const PasswordResetSlice = createSlice({
  name: 'passwordReset',
  initialState: initialState as PasswordResetState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(handlePasswordResetInit.fulfilled, () => ({
        ...initialState,
        loading: false,
        resetPasswordSuccess: true,
        successMessage: 'Check your email for details on how to reset your password.',
      }))
      .addCase(handlePasswordResetFinish.fulfilled, () => ({
        ...initialState,
        loading: false,
        //password reset state set to success
        resetPasswordSuccess: true,
        successMessage: 'Your password reset have been completed successfully',
      }))
      .addMatcher(isPending(handlePasswordResetInit, handlePasswordResetFinish), state => {
        state.loading = true;
      })
      //A matcher function to handle actions that have been rejected
      //resets the state to initialState, sets loading to false, and marks the operation as failed with a relevant flag (resetPasswordFailure)
      .addMatcher(isRejected(handlePasswordResetInit, handlePasswordResetFinish), () => ({
        ...initialState,
        loading: false,
        resetPasswordFailure: true,
      }));
  },
});

export const { reset } = PasswordResetSlice.actions;

// Reducer
export default PasswordResetSlice.reducer;
