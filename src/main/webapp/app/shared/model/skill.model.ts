import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface ISkill {
  id?: number;
  skillName?: string;
  individualSkillDesc?: string | null;
  yearsOfExp?: number;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<ISkill> = {};
