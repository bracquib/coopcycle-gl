import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPanier } from 'app/shared/model/panier.model';
import { getEntities } from './panier.reducer';

export const Panier = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const panierList = useAppSelector(state => state.panier.entities);
  const loading = useAppSelector(state => state.panier.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="panier-heading" data-cy="PanierHeading">
        <Translate contentKey="coopcycleApp.panier.home.title">Paniers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.panier.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/panier/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.panier.home.createLabel">Create new Panier</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {panierList && panierList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.panier.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.panier.client">Client</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.panier.commande">Commande</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {panierList.map((panier, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/panier/${panier.id}`} color="link" size="sm">
                      {panier.id}
                    </Button>
                  </td>
                  <td>{panier.client ? <Link to={`/client/${panier.client.id}`}>{panier.client.name}</Link> : ''}</td>
                  <td>{panier.commande ? <Link to={`/commande/${panier.commande.id}`}>{panier.commande.creationDate}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/panier/${panier.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/panier/${panier.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/panier/${panier.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="coopcycleApp.panier.home.notFound">No Paniers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Panier;
