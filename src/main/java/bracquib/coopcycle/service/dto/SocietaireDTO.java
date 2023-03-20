package bracquib.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link bracquib.coopcycle.domain.Societaire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocietaireDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nameclient;

    private String namerestaurant;

    private String namelivreur;

    private ClientDTO client;

    private RestaurantDTO restaurant;

    private LivreurDTO livreur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameclient() {
        return nameclient;
    }

    public void setNameclient(String nameclient) {
        this.nameclient = nameclient;
    }

    public String getNamerestaurant() {
        return namerestaurant;
    }

    public void setNamerestaurant(String namerestaurant) {
        this.namerestaurant = namerestaurant;
    }

    public String getNamelivreur() {
        return namelivreur;
    }

    public void setNamelivreur(String namelivreur) {
        this.namelivreur = namelivreur;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }

    public LivreurDTO getLivreur() {
        return livreur;
    }

    public void setLivreur(LivreurDTO livreur) {
        this.livreur = livreur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocietaireDTO)) {
            return false;
        }

        SocietaireDTO societaireDTO = (SocietaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, societaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocietaireDTO{" +
            "id=" + getId() +
            ", nameclient='" + getNameclient() + "'" +
            ", namerestaurant='" + getNamerestaurant() + "'" +
            ", namelivreur='" + getNamelivreur() + "'" +
            ", client=" + getClient() +
            ", restaurant=" + getRestaurant() +
            ", livreur=" + getLivreur() +
            "}";
    }
}
