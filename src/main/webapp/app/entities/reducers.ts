import codeTables from 'app/entities/code-tables/code-tables.reducer';
import userProfile from 'app/entities/user-profile/user-profile.reducer';
import skill from 'app/entities/skill/skill.reducer';
import message from 'app/entities/message/message.reducer';
import activity from 'app/entities/activity/activity.reducer';
import activityInvite from 'app/entities/activity-invite/activity-invite.reducer';
import userSkill from 'app/entities/user-skill/user-skill.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  codeTables,
  userProfile,
  skill,
  message,
  activity,
  activityInvite,
  userSkill,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
