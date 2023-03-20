package bracquib.coopcycle.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Commande.
 */
@Table("commande")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("creation_date")
    private String creationDate;

    @Column("delivery_date")
    private String deliveryDate;

    @NotNull(message = "must not be null")
    @Column("status")
    private String status;

    @Transient
    private Client client;

    @Transient
    private Restaurant restaurant;

    @Transient
    private Livreur livreur;

    @Column("client_id")
    private Long clientId;

    @Column("restaurant_id")
    private Long restaurantId;

    @Column("livreur_id")
    private Long livreurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public Commande creationDate(String creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDeliveryDate() {
        return this.deliveryDate;
    }

    public Commande deliveryDate(String deliveryDate) {
        this.setDeliveryDate(deliveryDate);
        return this;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return this.status;
    }

    public Commande status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
        this.clientId = client != null ? client.getId() : null;
    }

    public Commande client(Client client) {
        this.setClient(client);
        return this;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.restaurantId = restaurant != null ? restaurant.getId() : null;
    }

    public Commande restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    public Livreur getLivreur() {
        return this.livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
        this.livreurId = livreur != null ? livreur.getId() : null;
    }

    public Commande livreur(Livreur livreur) {
        this.setLivreur(livreur);
        return this;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long client) {
        this.clientId = client;
    }

    public Long getRestaurantId() {
        return this.restaurantId;
    }

    public void setRestaurantId(Long restaurant) {
        this.restaurantId = restaurant;
    }

    public Long getLivreurId() {
        return this.livreurId;
    }

    public void setLivreurId(Long livreur) {
        this.livreurId = livreur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
