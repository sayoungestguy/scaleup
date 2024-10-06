import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './user-skill.reducer';

// Assuming you have an API function to get skill details by id
import { getEntity as getSkillName } from 'app/entities/skill/skill.reducer';

export const UserSkill = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const userSkillList = useAppSelector(state => state.userSkill.entities);
  const loading = useAppSelector(state => state.userSkill.loading);
  const totalItems = useAppSelector(state => state.userSkill.totalItems);

  // New state to hold the fetched skill names
  const [skillNames, setSkillNames] = useState({});

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
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

  // Fetch the skillName for each skill.id
  useEffect(() => {
    const fetchSkillNames = async () => {
      const newSkillNames = { ...skillNames }; // Copy current state
      for (const userSkill of userSkillList) {
        if (userSkill.skill && !newSkillNames[userSkill.skill.id]) {
          const response = (await dispatch(getSkillName(userSkill.skill.id))) as unknown as { payload: { data: { skillName: string } } }; // Dispatch the action to fetch skill by id
          newSkillNames[userSkill.skill.id] = response.payload.data.skillName; // Update the state with the fetched skillName
        }
      }
      setSkillNames(newSkillNames); // Set the state with the updated skill names
    };

    if (userSkillList.length > 0) {
      fetchSkillNames();
    }
  }, [userSkillList]);

  //**************************************************************************** */

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="user-skill-heading" data-cy="UserSkillHeading">
        User Skills
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/user-skill/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new User Skill
          </Link>
        </div>
      </h2>
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
                    {userSkill.userProfile ? <Link to={`/user-profile/${userSkill.userProfile.id}`}>{userSkill.userProfile.id}</Link> : ''}
                  </td>
                  <td>
                    {userSkill.skill ? (
                      <Link to={`/skill/${userSkill.skill.id}`}>{skillNames[userSkill.skill.id] || 'Loading...'}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{userSkill.skillType ? <Link to={`/code-tables/${userSkill.skillType.id}`}>{userSkill.skillType.id}</Link> : ''}</td>
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
      {totalItems ? (
        <div className={userSkillList && userSkillList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default UserSkill;
