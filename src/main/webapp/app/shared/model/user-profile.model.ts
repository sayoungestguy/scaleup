import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IUserProfile {
  id?: number;
  nickname?: string | null;
  jobRole?: string | null;
  aboutMe?: string | null;
  profilePicture?: string | null;
  socialLinks?: string | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  user?: IUser;
}

export const defaultValue: Readonly<IUserProfile> = {};
