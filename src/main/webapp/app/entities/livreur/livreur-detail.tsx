import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './livreur.reducer';

export const LivreurDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const livreurEntity = useAppSelector(state => state.livreur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="livreurDetailsHeading">
          <Translate contentKey="coopcycleApp.livreur.detail.title">Livreur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopcycleApp.livreur.name">Name</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.name}</dd>
          <dt>
            <span id="surname">
              <Translate contentKey="coopcycleApp.livreur.surname">Surname</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.surname}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="coopcycleApp.livreur.email">Email</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.email}</dd>
          <dt>
            <span id="phonenumber">
              <Translate contentKey="coopcycleApp.livreur.phonenumber">Phonenumber</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.phonenumber}</dd>
          <dt>
            <span id="vehicleType">
              <Translate contentKey="coopcycleApp.livreur.vehicleType">Vehicle Type</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.vehicleType}</dd>
          <dt>
            <span id="commandState">
              <Translate contentKey="coopcycleApp.livreur.commandState">Command State</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.commandState}</dd>
        </dl>
        <Button tag={Link} to="/livreur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/livreur/${livreurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LivreurDetail;
