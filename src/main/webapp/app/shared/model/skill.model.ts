import dayjs from 'dayjs';

export interface ISkill {
  id?: number;
  skillName?: string;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<ISkill> = {};
