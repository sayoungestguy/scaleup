import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { loadMoreDataWhenScrolled, parseHeaderForLinks } from 'react-jhipster';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IUserProfile, defaultValue } from 'app/shared/model/user-profile.model';

const initialState: EntityState<IUserProfile> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: {} as IUserProfile,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/user-profiles';

// Actions

export const getEntities = createAsyncThunk(
  'userProfile/fetch_entity_list',
  async ({ query, page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?${query}&${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
    return axios.get<IUserProfile[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getEntity = createAsyncThunk(
  'userProfile/fetch_entity',
  async ({ query }: IQueryParams) => {
    const requestUrl = ` ${apiUrl}?${query}`;
    return axios.get<IUserProfile>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getEntityByCreatedBy = createAsyncThunk(
  'userProfile/fetch_entity',
  async ({ query }: IQueryParams) => {
    const requestUrl = ` ${apiUrl}?${query}`;
    return axios.get<IUserProfile>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'userProfile/create_entity',
  async (entity: IUserProfile, thunkAPI) => {
    return axios.post<IUserProfile>(apiUrl, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'userProfile/update_entity',
  async (entity: IUserProfile, thunkAPI) => {
    return axios.put<IUserProfile>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'userProfile/partial_update_entity',
  async (entity: IUserProfile, thunkAPI) => {
    return axios.patch<IUserProfile>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'userProfile/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    return await axios.delete<IUserProfile>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

// slice

export const UserProfileSlice = createEntitySlice({
  name: 'userProfile',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(getEntityByCreatedBy.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;
        const links = parseHeaderForLinks(headers.link);

        return {
          ...state,
          loading: false,
          links,
          entities: loadMoreDataWhenScrolled(state.entities, data, links),
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity, getEntityByCreatedBy), state => {
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

export const { reset } = UserProfileSlice.actions;

// Reducer
export default UserProfileSlice.reducer;
