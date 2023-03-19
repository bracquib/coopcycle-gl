import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILivreur } from 'app/shared/model/livreur.model';
import { getEntities } from './livreur.reducer';

export const Livreur = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const livreurList = useAppSelector(state => state.livreur.entities);
  const loading = useAppSelector(state => state.livreur.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="livreur-heading" data-cy="LivreurHeading">
        <Translate contentKey="coopcycleApp.livreur.home.title">Livreurs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.livreur.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/livreur/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.livreur.home.createLabel">Create new Livreur</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {livreurList && livreurList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.surname">Surname</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.phonenumber">Phonenumber</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.vehicleType">Vehicle Type</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.livreur.commandState">Command State</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {livreurList.map((livreur, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/livreur/${livreur.id}`} color="link" size="sm">
                      {livreur.id}
                    </Button>
                  </td>
                  <td>{livreur.name}</td>
                  <td>{livreur.surname}</td>
                  <td>{livreur.email}</td>
                  <td>{livreur.phonenumber}</td>
                  <td>{livreur.vehicleType}</td>
                  <td>{livreur.commandState}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/livreur/${livreur.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/livreur/${livreur.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/livreur/${livreur.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="coopcycleApp.livreur.home.notFound">No Livreurs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Livreur;
