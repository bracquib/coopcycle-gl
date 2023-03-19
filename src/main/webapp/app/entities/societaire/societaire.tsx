import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISocietaire } from 'app/shared/model/societaire.model';
import { getEntities } from './societaire.reducer';

export const Societaire = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const societaireList = useAppSelector(state => state.societaire.entities);
  const loading = useAppSelector(state => state.societaire.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="societaire-heading" data-cy="SocietaireHeading">
        <Translate contentKey="coopcycleApp.societaire.home.title">Societaires</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.societaire.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/societaire/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.societaire.home.createLabel">Create new Societaire</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {societaireList && societaireList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.societaire.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.societaire.client">Client</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.societaire.restaurant">Restaurant</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.societaire.livreur">Livreur</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.societaire.client">Client</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.societaire.restaurant">Restaurant</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.societaire.livreur">Livreur</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {societaireList.map((societaire, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/societaire/${societaire.id}`} color="link" size="sm">
                      {societaire.id}
                    </Button>
                  </td>
                  <td>{societaire.client}</td>
                  <td>{societaire.restaurant}</td>
                  <td>{societaire.livreur}</td>
                  <td>{societaire.client ? <Link to={`/client/${societaire.client.id}`}>{societaire.client.id}</Link> : ''}</td>
                  <td>
                    {societaire.restaurant ? <Link to={`/restaurant/${societaire.restaurant.id}`}>{societaire.restaurant.id}</Link> : ''}
                  </td>
                  <td>{societaire.livreur ? <Link to={`/livreur/${societaire.livreur.id}`}>{societaire.livreur.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/societaire/${societaire.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/societaire/${societaire.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/societaire/${societaire.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="coopcycleApp.societaire.home.notFound">No Societaires found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Societaire;