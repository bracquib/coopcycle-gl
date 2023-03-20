package bracquib.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link bracquib.coopcycle.domain.Commande} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommandeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String creationDate;

    private String deliveryDate;

    @NotNull(message = "must not be null")
    private String status;

    private ClientDTO client;

    private RestaurantDTO restaurant;

    private LivreurDTO livreur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(o instanceof CommandeDTO)) {
            return false;
        }

        CommandeDTO commandeDTO = (CommandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", client=" + getClient() +
            ", restaurant=" + getRestaurant() +
            ", livreur=" + getLivreur() +
            "}";
    }
}
