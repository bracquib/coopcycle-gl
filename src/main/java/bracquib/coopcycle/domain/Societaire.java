package bracquib.coopcycle.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Societaire.
 */
@Table("societaire")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Societaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nameclient")
    private String nameclient;

    @Column("namerestaurant")
    private String namerestaurant;

    @Column("namelivreur")
    private String namelivreur;

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

    public Societaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameclient() {
        return this.nameclient;
    }

    public Societaire nameclient(String nameclient) {
        this.setNameclient(nameclient);
        return this;
    }

    public void setNameclient(String nameclient) {
        this.nameclient = nameclient;
    }

    public String getNamerestaurant() {
        return this.namerestaurant;
    }

    public Societaire namerestaurant(String namerestaurant) {
        this.setNamerestaurant(namerestaurant);
        return this;
    }

    public void setNamerestaurant(String namerestaurant) {
        this.namerestaurant = namerestaurant;
    }

    public String getNamelivreur() {
        return this.namelivreur;
    }

    public Societaire namelivreur(String namelivreur) {
        this.setNamelivreur(namelivreur);
        return this;
    }

    public void setNamelivreur(String namelivreur) {
        this.namelivreur = namelivreur;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
        this.clientId = client != null ? client.getId() : null;
    }

    public Societaire client(Client client) {
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

    public Societaire restaurant(Restaurant restaurant) {
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

    public Societaire livreur(Livreur livreur) {
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
        if (!(o instanceof Societaire)) {
            return false;
        }
        return id != null && id.equals(((Societaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Societaire{" +
            "id=" + getId() +
            ", nameclient='" + getNameclient() + "'" +
            ", namerestaurant='" + getNamerestaurant() + "'" +
            ", namelivreur='" + getNamelivreur() + "'" +
            "}";
    }
}
