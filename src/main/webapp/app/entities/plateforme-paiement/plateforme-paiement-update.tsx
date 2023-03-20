import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICommande } from 'app/shared/model/commande.model';
import { getEntities as getCommandes } from 'app/entities/commande/commande.reducer';
import { IPlateformePaiement } from 'app/shared/model/plateforme-paiement.model';
import { TypePaiement } from 'app/shared/model/enumerations/type-paiement.model';
import { getEntity, updateEntity, createEntity, reset } from './plateforme-paiement.reducer';

export const PlateformePaiementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const commandes = useAppSelector(state => state.commande.entities);
  const plateformePaiementEntity = useAppSelector(state => state.plateformePaiement.entity);
  const loading = useAppSelector(state => state.plateformePaiement.loading);
  const updating = useAppSelector(state => state.plateformePaiement.updating);
  const updateSuccess = useAppSelector(state => state.plateformePaiement.updateSuccess);
  const typePaiementValues = Object.keys(TypePaiement);

  const handleClose = () => {
    navigate('/plateforme-paiement');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCommandes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...plateformePaiementEntity,
      ...values,
      commande: commandes.find(it => it.id.toString() === values.commande.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          paymentType: 'CB',
          ...plateformePaiementEntity,
          commande: plateformePaiementEntity?.commande?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopcycleApp.plateformePaiement.home.createOrEditLabel" data-cy="PlateformePaiementCreateUpdateHeading">
            <Translate contentKey="coopcycleApp.plateformePaiement.home.createOrEditLabel">Create or edit a PlateformePaiement</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="plateforme-paiement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coopcycleApp.plateformePaiement.amount')}
                id="plateforme-paiement-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.plateformePaiement.paymentType')}
                id="plateforme-paiement-paymentType"
                name="paymentType"
                data-cy="paymentType"
                type="select"
              >
                {typePaiementValues.map(typePaiement => (
                  <option value={typePaiement} key={typePaiement}>
                    {translate('coopcycleApp.TypePaiement.' + typePaiement)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopcycleApp.plateformePaiement.description')}
                id="plateforme-paiement-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="plateforme-paiement-commande"
                name="commande"
                data-cy="commande"
                label={translate('coopcycleApp.plateformePaiement.commande')}
                type="select"
              >
                <option value="" key="0" />
                {commandes
                  ? commandes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.creationDate}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/plateforme-paiement" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PlateformePaiementUpdate;
