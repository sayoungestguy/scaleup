import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getCodeTableById } from './code-tables.reducer';

export const CodeTablesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getCodeTableById(id));
  }, []);

  const codeTablesEntity = useAppSelector(state => state.codeTables.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="codeTablesDetailsHeading">Code Tables</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{codeTablesEntity.id}</dd>
          <dt>
            <span id="category">Category</span>
          </dt>
          <dd>{codeTablesEntity.category}</dd>
          <dt>
            <span id="codeKey">Code Key</span>
          </dt>
          <dd>{codeTablesEntity.codeKey}</dd>
          <dt>
            <span id="codeValue">Code Value</span>
          </dt>
          <dd>{codeTablesEntity.codeValue}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{codeTablesEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {codeTablesEntity.createdDate ? <TextFormat value={codeTablesEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">Last Modified By</span>
          </dt>
          <dd>{codeTablesEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">Last Modified Date</span>
          </dt>
          <dd>
            {codeTablesEntity.lastModifiedDate ? (
              <TextFormat value={codeTablesEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/code-tables" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/code-tables/${codeTablesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CodeTablesDetail;
