import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { ISkill } from 'app/shared/model/skill.model';

export interface IActivity {
  id?: number;
  activityTime?: dayjs.Dayjs;
  duration?: number | null;
  venue?: string | null;
  details?: string | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  creatorProfile?: IUserProfile | null;
  skill?: ISkill | null;
}

export const defaultValue: Readonly<IActivity> = {};
