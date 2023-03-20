import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './commande.reducer';

export const CommandeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const commandeEntity = useAppSelector(state => state.commande.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commandeDetailsHeading">
          <Translate contentKey="coopcycleApp.commande.detail.title">Commande</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commandeEntity.id}</dd>
          <dt>
            <span id="creationDate">
              <Translate contentKey="coopcycleApp.commande.creationDate">Creation Date</Translate>
            </span>
          </dt>
          <dd>{commandeEntity.creationDate}</dd>
          <dt>
            <span id="deliveryDate">
              <Translate contentKey="coopcycleApp.commande.deliveryDate">Delivery Date</Translate>
            </span>
          </dt>
          <dd>{commandeEntity.deliveryDate}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopcycleApp.commande.status">Status</Translate>
            </span>
          </dt>
          <dd>{commandeEntity.status}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.commande.client">Client</Translate>
          </dt>
          <dd>{commandeEntity.client ? commandeEntity.client.name : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.commande.restaurant">Restaurant</Translate>
          </dt>
          <dd>{commandeEntity.restaurant ? commandeEntity.restaurant.name : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.commande.livreur">Livreur</Translate>
          </dt>
          <dd>{commandeEntity.livreur ? commandeEntity.livreur.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/commande" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/commande/${commandeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommandeDetail;
