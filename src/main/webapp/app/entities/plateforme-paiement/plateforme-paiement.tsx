import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPlateformePaiement } from 'app/shared/model/plateforme-paiement.model';
import { getEntities } from './plateforme-paiement.reducer';

export const PlateformePaiement = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const plateformePaiementList = useAppSelector(state => state.plateformePaiement.entities);
  const loading = useAppSelector(state => state.plateformePaiement.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="plateforme-paiement-heading" data-cy="PlateformePaiementHeading">
        <Translate contentKey="coopcycleApp.plateformePaiement.home.title">Plateforme Paiements</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.plateformePaiement.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/plateforme-paiement/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.plateformePaiement.home.createLabel">Create new Plateforme Paiement</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {plateformePaiementList && plateformePaiementList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.plateformePaiement.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.plateformePaiement.amount">Amount</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.plateformePaiement.paymentType">Payment Type</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.plateformePaiement.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.plateformePaiement.commande">Commande</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {plateformePaiementList.map((plateformePaiement, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/plateforme-paiement/${plateformePaiement.id}`} color="link" size="sm">
                      {plateformePaiement.id}
                    </Button>
                  </td>
                  <td>{plateformePaiement.amount}</td>
                  <td>
                    <Translate contentKey={`coopcycleApp.TypePaiement.${plateformePaiement.paymentType}`} />
                  </td>
                  <td>{plateformePaiement.description}</td>
                  <td>
                    {plateformePaiement.commande ? (
                      <Link to={`/commande/${plateformePaiement.commande.id}`}>{plateformePaiement.commande.creationDate}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/plateforme-paiement/${plateformePaiement.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/plateforme-paiement/${plateformePaiement.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/plateforme-paiement/${plateformePaiement.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="coopcycleApp.plateformePaiement.home.notFound">No Plateforme Paiements found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PlateformePaiement;
