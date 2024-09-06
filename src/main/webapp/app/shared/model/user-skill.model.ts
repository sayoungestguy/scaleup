import { IUserProfile } from 'app/shared/model/user-profile.model';
import { ISkill } from 'app/shared/model/skill.model';
import { ICodeTables } from 'app/shared/model/code-tables.model';

export interface IUserSkill {
  id?: number;
  yearsOfExperience?: number;
  userProfile?: IUserProfile | null;
  skill?: ISkill | null;
  skillType?: ICodeTables | null;
}

export const defaultValue: Readonly<IUserSkill> = {};
