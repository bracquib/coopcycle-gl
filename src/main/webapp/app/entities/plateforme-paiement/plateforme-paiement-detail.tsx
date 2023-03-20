import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './plateforme-paiement.reducer';

export const PlateformePaiementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const plateformePaiementEntity = useAppSelector(state => state.plateformePaiement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="plateformePaiementDetailsHeading">
          <Translate contentKey="coopcycleApp.plateformePaiement.detail.title">PlateformePaiement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{plateformePaiementEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="coopcycleApp.plateformePaiement.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{plateformePaiementEntity.amount}</dd>
          <dt>
            <span id="paymentType">
              <Translate contentKey="coopcycleApp.plateformePaiement.paymentType">Payment Type</Translate>
            </span>
          </dt>
          <dd>{plateformePaiementEntity.paymentType}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopcycleApp.plateformePaiement.description">Description</Translate>
            </span>
          </dt>
          <dd>{plateformePaiementEntity.description}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.plateformePaiement.commande">Commande</Translate>
          </dt>
          <dd>{plateformePaiementEntity.commande ? plateformePaiementEntity.commande.creationDate : ''}</dd>
        </dl>
        <Button tag={Link} to="/plateforme-paiement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/plateforme-paiement/${plateformePaiementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlateformePaiementDetail;
