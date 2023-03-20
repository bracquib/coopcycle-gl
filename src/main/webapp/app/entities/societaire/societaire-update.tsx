import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { IRestaurant } from 'app/shared/model/restaurant.model';
import { getEntities as getRestaurants } from 'app/entities/restaurant/restaurant.reducer';
import { ILivreur } from 'app/shared/model/livreur.model';
import { getEntities as getLivreurs } from 'app/entities/livreur/livreur.reducer';
import { ISocietaire } from 'app/shared/model/societaire.model';
import { getEntity, updateEntity, createEntity, reset } from './societaire.reducer';

export const SocietaireUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clients = useAppSelector(state => state.client.entities);
  const restaurants = useAppSelector(state => state.restaurant.entities);
  const livreurs = useAppSelector(state => state.livreur.entities);
  const societaireEntity = useAppSelector(state => state.societaire.entity);
  const loading = useAppSelector(state => state.societaire.loading);
  const updating = useAppSelector(state => state.societaire.updating);
  const updateSuccess = useAppSelector(state => state.societaire.updateSuccess);

  const handleClose = () => {
    navigate('/societaire');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClients({}));
    dispatch(getRestaurants({}));
    dispatch(getLivreurs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...societaireEntity,
      ...values,
      client: clients.find(it => it.id.toString() === values.client.toString()),
      restaurant: restaurants.find(it => it.id.toString() === values.restaurant.toString()),
      livreur: livreurs.find(it => it.id.toString() === values.livreur.toString()),
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
          ...societaireEntity,
          client: societaireEntity?.client?.id,
          restaurant: societaireEntity?.restaurant?.id,
          livreur: societaireEntity?.livreur?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopcycleApp.societaire.home.createOrEditLabel" data-cy="SocietaireCreateUpdateHeading">
            <Translate contentKey="coopcycleApp.societaire.home.createOrEditLabel">Create or edit a Societaire</Translate>
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
                  id="societaire-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coopcycleApp.societaire.nameclient')}
                id="societaire-nameclient"
                name="nameclient"
                data-cy="nameclient"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.societaire.namerestaurant')}
                id="societaire-namerestaurant"
                name="namerestaurant"
                data-cy="namerestaurant"
                type="text"
              />
              <ValidatedField
                label={translate('coopcycleApp.societaire.namelivreur')}
                id="societaire-namelivreur"
                name="namelivreur"
                data-cy="namelivreur"
                type="text"
              />
              <ValidatedField
                id="societaire-client"
                name="client"
                data-cy="client"
                label={translate('coopcycleApp.societaire.client')}
                type="select"
              >
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="societaire-restaurant"
                name="restaurant"
                data-cy="restaurant"
                label={translate('coopcycleApp.societaire.restaurant')}
                type="select"
              >
                <option value="" key="0" />
                {restaurants
                  ? restaurants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="societaire-livreur"
                name="livreur"
                data-cy="livreur"
                label={translate('coopcycleApp.societaire.livreur')}
                type="select"
              >
                <option value="" key="0" />
                {livreurs
                  ? livreurs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/societaire" replace color="info">
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

export default SocietaireUpdate;
