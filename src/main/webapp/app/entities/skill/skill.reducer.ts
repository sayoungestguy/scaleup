import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ISkill, defaultValue } from 'app/shared/model/skill.model';

const initialState: EntityState<ISkill> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/skills';

// Actions

export const getAllSkills = createAsyncThunk(
  'skill/fetch_entity_list',
  async ({ query, page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?${query ? `${query}&` : ''}${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
    return axios.get<ISkill[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getSkillById = createAsyncThunk(
  'skill/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ISkill>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'skill/create_entity',
  async (entity: ISkill, thunkAPI) => {
    const result = await axios.post<ISkill>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getAllSkills({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'skill/update_entity',
  async (entity: ISkill, thunkAPI) => {
    const result = await axios.put<ISkill>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getAllSkills({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'skill/partial_update_entity',
  async (entity: ISkill, thunkAPI) => {
    const result = await axios.patch<ISkill>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getAllSkills({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'skill/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<ISkill>(requestUrl);
    thunkAPI.dispatch(getAllSkills({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const SkillSlice = createEntitySlice({
  name: 'skill',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getSkillById.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getAllSkills), (state, action) => {
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
      .addMatcher(isPending(getAllSkills, getSkillById), state => {
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

export const { reset } = SkillSlice.actions;

// Reducer
export default SkillSlice.reducer;
