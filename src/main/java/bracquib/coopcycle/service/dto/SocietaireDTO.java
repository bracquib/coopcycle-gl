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
    private String client;

    private String restaurant;

    private String livreur;

    private ClientDTO client;

    private RestaurantDTO restaurant;

    private LivreurDTO livreur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getLivreur() {
        return livreur;
    }

    public void setLivreur(String livreur) {
        this.livreur = livreur;
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
            ", client='" + getClient() + "'" +
            ", restaurant='" + getRestaurant() + "'" +
            ", livreur='" + getLivreur() + "'" +
            ", client=" + getClient() +
            ", restaurant=" + getRestaurant() +
            ", livreur=" + getLivreur() +
            "}";
    }
}
