import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IUserProfile, defaultValue } from 'app/shared/model/user-profile.model';

const initialState: EntityState<IUserProfile> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/user-profiles';

// Actions

export const getAllUserProfiles = createAsyncThunk(
  'userProfile/fetch_entity_list',
  async ({ query, page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?${query ? `${query}&` : ''}${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
    return axios.get<IUserProfile[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getUserProfileById = createAsyncThunk(
  'userProfile/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IUserProfile>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createUserProfile = createAsyncThunk(
  'userProfile/create_entity',
  async (entity: IUserProfile, thunkAPI) => {
    const result = await axios.post<IUserProfile>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getAllUserProfiles({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateUserProfile = createAsyncThunk(
  'userProfile/update_entity',
  async (entity: IUserProfile, thunkAPI) => {
    const result = await axios.put<IUserProfile>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getAllUserProfiles({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateUserProfile = createAsyncThunk(
  'userProfile/partial_update_entity',
  async (entity: IUserProfile, thunkAPI) => {
    const result = await axios.patch<IUserProfile>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getAllUserProfiles({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteUserProfile = createAsyncThunk(
  'userProfile/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IUserProfile>(requestUrl);
    thunkAPI.dispatch(getAllUserProfiles({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const UserProfileSlice = createEntitySlice({
  name: 'userProfile',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getUserProfileById.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteUserProfile.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getAllUserProfiles), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createUserProfile, updateUserProfile, partialUpdateUserProfile), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getAllUserProfiles, getUserProfileById), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createUserProfile, updateUserProfile, partialUpdateUserProfile, deleteUserProfile), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = UserProfileSlice.actions;

// Reducer
export default UserProfileSlice.reducer;
