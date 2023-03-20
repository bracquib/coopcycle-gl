package bracquib.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bracquib.coopcycle.domain.Panier} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PanierDTO implements Serializable {

    private Long id;

    private ClientDTO client;

    private CommandeDTO commande;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public CommandeDTO getCommande() {
        return commande;
    }

    public void setCommande(CommandeDTO commande) {
        this.commande = commande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PanierDTO)) {
            return false;
        }

        PanierDTO panierDTO = (PanierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, panierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PanierDTO{" +
            "id=" + getId() +
            ", client=" + getClient() +
            ", commande=" + getCommande() +
            "}";
    }
}
