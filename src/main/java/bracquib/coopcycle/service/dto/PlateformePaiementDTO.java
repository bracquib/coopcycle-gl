package bracquib.coopcycle.service.dto;

import bracquib.coopcycle.domain.enumeration.TypePaiement;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link bracquib.coopcycle.domain.PlateformePaiement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlateformePaiementDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String amount;

    @NotNull(message = "must not be null")
    private TypePaiement paymentType;

    private String description;

    private CommandeDTO commande;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public TypePaiement getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(TypePaiement paymentType) {
        this.paymentType = paymentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof PlateformePaiementDTO)) {
            return false;
        }

        PlateformePaiementDTO plateformePaiementDTO = (PlateformePaiementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, plateformePaiementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlateformePaiementDTO{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", description='" + getDescription() + "'" +
            ", commande=" + getCommande() +
            "}";
    }
}
