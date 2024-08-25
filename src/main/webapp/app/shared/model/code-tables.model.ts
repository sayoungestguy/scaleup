import dayjs from 'dayjs';

export interface ICodeTables {
  id?: number;
  category?: string | null;
  codeKey?: string | null;
  codeValue?: string | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<ICodeTables> = {};
