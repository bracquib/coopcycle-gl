import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './societaire.reducer';

export const SocietaireDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const societaireEntity = useAppSelector(state => state.societaire.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="societaireDetailsHeading">
          <Translate contentKey="coopcycleApp.societaire.detail.title">Societaire</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{societaireEntity.id}</dd>
          <dt>
            <span id="client">
              <Translate contentKey="coopcycleApp.societaire.client">Client</Translate>
            </span>
          </dt>
          <dd>{societaireEntity.client}</dd>
          <dt>
            <span id="restaurant">
              <Translate contentKey="coopcycleApp.societaire.restaurant">Restaurant</Translate>
            </span>
          </dt>
          <dd>{societaireEntity.restaurant}</dd>
          <dt>
            <span id="livreur">
              <Translate contentKey="coopcycleApp.societaire.livreur">Livreur</Translate>
            </span>
          </dt>
          <dd>{societaireEntity.livreur}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.societaire.client">Client</Translate>
          </dt>
          <dd>{societaireEntity.client ? societaireEntity.client.id : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.societaire.restaurant">Restaurant</Translate>
          </dt>
          <dd>{societaireEntity.restaurant ? societaireEntity.restaurant.id : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.societaire.livreur">Livreur</Translate>
          </dt>
          <dd>{societaireEntity.livreur ? societaireEntity.livreur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/societaire" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/societaire/${societaireEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SocietaireDetail;
