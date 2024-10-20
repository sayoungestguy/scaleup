import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IActivityInvite, defaultValue } from 'app/shared/model/activity-invite.model';

const initialState: EntityState<IActivityInvite> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/activity-invites';

// Actions

export const getAllActivityInvites = createAsyncThunk(
  'activityInvite/fetch_entity_list',
  async ({ query, page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?${query ? `${query}&` : ''}${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;

    return axios.get<IActivityInvite[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getActivityInviteById = createAsyncThunk(
  'activityInvite/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IActivityInvite>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getActivityById = createAsyncThunk(
  'activityInvite/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IActivityInvite>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getInviteeProfileById = createAsyncThunk(
  'activityInvite/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IActivityInvite>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getStatusById = createAsyncThunk(
  'activityInvite/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IActivityInvite>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'activityInvite/create_entity',
  async (entity: IActivityInvite, thunkAPI) => {
    const result = await axios.post<IActivityInvite>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getAllActivityInvites({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'activityInvite/update_entity',
  async (entity: IActivityInvite, thunkAPI) => {
    const result = await axios.put<IActivityInvite>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getAllActivityInvites({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'activityInvite/partial_update_entity',
  async (entity: IActivityInvite, thunkAPI) => {
    const result = await axios.patch<IActivityInvite>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getAllActivityInvites({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'activityInvite/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IActivityInvite>(requestUrl);
    thunkAPI.dispatch(getAllActivityInvites({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const ActivityInviteSlice = createEntitySlice({
  name: 'activityInvite',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getActivityInviteById.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getAllActivityInvites), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getAllActivityInvites, getActivityInviteById), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = ActivityInviteSlice.actions;

// Reducer
export default ActivityInviteSlice.reducer;
