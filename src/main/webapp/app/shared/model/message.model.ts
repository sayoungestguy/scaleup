import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IMessage {
  id?: number;
  content?: string;
  sentAt?: dayjs.Dayjs;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  senderProfile?: IUserProfile | null;
  receiverProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IMessage> = {
  isDeleted: false,
};
