import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { ICodeTables } from 'app/shared/model/code-tables.model';

export interface INotification {
  id?: number;
  notificationRefId?: string | null;
  content?: string | null;
  isRead?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  userProfile?: IUserProfile | null;
  type?: ICodeTables | null;
}

export const defaultValue: Readonly<INotification> = {
  isRead: false,
};
