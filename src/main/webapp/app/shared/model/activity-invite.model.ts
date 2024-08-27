import dayjs from 'dayjs';
import { IActivity } from 'app/shared/model/activity.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { ICodeTables } from 'app/shared/model/code-tables.model';

export interface IActivityInvite {
  id?: number;
  willParticipate?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  activity?: IActivity | null;
  inviteeProfile?: IUserProfile | null;
  status?: ICodeTables | null;
}

export const defaultValue: Readonly<IActivityInvite> = {
  willParticipate: false,
};
