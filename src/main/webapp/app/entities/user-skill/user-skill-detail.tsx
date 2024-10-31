import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { getEntity, getEntities } from './user-skill.reducer';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export const UserSkillDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();
  const userSkillList = useAppSelector(state => state.userSkill.entities);

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const loading = useAppSelector(state => state.userSkill.loading);
  const totalItems = useAppSelector(state => state.userSkill.totalItems);

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        //query:`userProfileId.equals=${userSkillEntity.userProfile.id}`
        query: `userProfileId.equals=${id}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  const userSkillEntity = useAppSelector(state => state.userSkill.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userSkillDetailsHeading">User Skill</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{userSkillEntity.id}</dd>
          <dt>
            <span id="yearsOfExperience">Years Of Experience</span>
          </dt>
          <dd>{userSkillEntity.yearsOfExperience}</dd>
          <dt>User Profile</dt>
          <dd>{userSkillEntity.userProfile ? userSkillEntity.userProfile.id : ''}</dd>
          <dt>Skill</dt>
          <dd>{userSkillEntity.skill ? userSkillEntity.skill.id : ''}</dd>
          <dt>Skill Type</dt>
          <dd>{userSkillEntity.skillType ? userSkillEntity.skillType.id : ''}</dd>
        </dl>
        <div className="table-responsive">
          {userSkillList && userSkillList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('yearsOfExperience')}>
                    Years Of Experience <FontAwesomeIcon icon={getSortIconByFieldName('yearsOfExperience')} />
                  </th>
                  <th>
                    User Profile <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Skill <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Skill Type <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {userSkillList.map((userSkill, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/user-skill/${userSkill.id}`} color="link" size="sm">
                        {userSkill.id}
                      </Button>
                    </td>
                    <td>{userSkill.yearsOfExperience}</td>
                    <td>
                      {userSkill.userProfile ? (
                        <Link to={`/user-profile/${userSkill.userProfile.id}`}>{userSkill.userProfile.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>{userSkill.skill ? <Link to={`/skill/${userSkill.skill.id}`}>{userSkill.skill.id}</Link> : ''}</td>
                    <td>
                      {userSkill.skillType ? <Link to={`/code-tables/${userSkill.skillType.id}`}>{userSkill.skillType.id}</Link> : ''}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/user-skill/${userSkill.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/user-skill/${userSkill.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          onClick={() =>
                            (window.location.href = `/user-skill/${userSkill.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                          }
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && <div className="alert alert-warning">No User Skills found</div>
          )}
        </div>
        <Button tag={Link} to="/user-skill" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-skill/${userSkillEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserSkillDetail;
