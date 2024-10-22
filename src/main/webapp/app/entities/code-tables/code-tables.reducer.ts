import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ICodeTables, defaultValue } from 'app/shared/model/code-tables.model';

const initialState: EntityState<ICodeTables> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/code-tables';

// Actions

export const getCodeTables = createAsyncThunk(
  'codeTables/fetch_entity_list',
  async ({ page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
    return axios.get<ICodeTables[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getCodeTableById = createAsyncThunk(
  'codeTables/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ICodeTables>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createCodeTable = createAsyncThunk(
  'codeTables/create_entity',
  async (entity: ICodeTables, thunkAPI) => {
    const result = await axios.post<ICodeTables>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getCodeTables({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'codeTables/update_entity',
  async (entity: ICodeTables, thunkAPI) => {
    const result = await axios.put<ICodeTables>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getCodeTables({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'codeTables/partial_update_entity',
  async (entity: ICodeTables, thunkAPI) => {
    const result = await axios.patch<ICodeTables>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getCodeTables({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'codeTables/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<ICodeTables>(requestUrl);
    thunkAPI.dispatch(getCodeTables({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const CodeTablesSlice = createEntitySlice({
  name: 'codeTables',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getCodeTableById.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getCodeTables), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createCodeTable, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getCodeTables, getCodeTableById), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createCodeTable, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = CodeTablesSlice.actions;

// Reducer
export default CodeTablesSlice.reducer;
