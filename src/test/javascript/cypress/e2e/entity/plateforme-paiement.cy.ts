import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('PlateformePaiement e2e test', () => {
  const plateformePaiementPageUrl = '/plateforme-paiement';
  const plateformePaiementPageUrlPattern = new RegExp('/plateforme-paiement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const plateformePaiementSample = { amount: 'parsing bus Account', paymentType: 'CB' };

  let plateformePaiement;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/plateforme-paiements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/plateforme-paiements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/plateforme-paiements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (plateformePaiement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/plateforme-paiements/${plateformePaiement.id}`,
      }).then(() => {
        plateformePaiement = undefined;
      });
    }
  });

  it('PlateformePaiements menu should load PlateformePaiements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('plateforme-paiement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PlateformePaiement').should('exist');
    cy.url().should('match', plateformePaiementPageUrlPattern);
  });

  describe('PlateformePaiement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(plateformePaiementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PlateformePaiement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/plateforme-paiement/new$'));
        cy.getEntityCreateUpdateHeading('PlateformePaiement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', plateformePaiementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/plateforme-paiements',
          body: plateformePaiementSample,
        }).then(({ body }) => {
          plateformePaiement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/plateforme-paiements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [plateformePaiement],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(plateformePaiementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PlateformePaiement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('plateformePaiement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', plateformePaiementPageUrlPattern);
      });

      it('edit button click should load edit PlateformePaiement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PlateformePaiement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', plateformePaiementPageUrlPattern);
      });

      it('edit button click should load edit PlateformePaiement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PlateformePaiement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', plateformePaiementPageUrlPattern);
      });

      it('last delete button click should delete instance of PlateformePaiement', () => {
        cy.intercept('GET', '/api/plateforme-paiements/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('plateformePaiement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', plateformePaiementPageUrlPattern);

        plateformePaiement = undefined;
      });
    });
  });

  describe('new PlateformePaiement page', () => {
    beforeEach(() => {
      cy.visit(`${plateformePaiementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PlateformePaiement');
    });

    it('should create an instance of PlateformePaiement', () => {
      cy.get(`[data-cy="amount"]`).type('blue Director').should('have.value', 'blue Director');

      cy.get(`[data-cy="paymentType"]`).select('Apple_Pay');

      cy.get(`[data-cy="description"]`).type('Washington').should('have.value', 'Washington');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        plateformePaiement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', plateformePaiementPageUrlPattern);
    });
  });
});
