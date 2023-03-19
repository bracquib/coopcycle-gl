package bracquib.coopcycle.domain;

import bracquib.coopcycle.domain.enumeration.TypePaiement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PlateformePaiement.
 */
@Table("plateforme_paiement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlateformePaiement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("amount")
    private String amount;

    @NotNull(message = "must not be null")
    @Column("payment_type")
    private TypePaiement paymentType;

    @Column("description")
    private String description;

    @Transient
    @JsonIgnoreProperties(value = { "client", "restaurant", "livreur" }, allowSetters = true)
    private Commande commande;

    @Column("commande_id")
    private Long commandeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlateformePaiement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmount() {
        return this.amount;
    }

    public PlateformePaiement amount(String amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public TypePaiement getPaymentType() {
        return this.paymentType;
    }

    public PlateformePaiement paymentType(TypePaiement paymentType) {
        this.setPaymentType(paymentType);
        return this;
    }

    public void setPaymentType(TypePaiement paymentType) {
        this.paymentType = paymentType;
    }

    public String getDescription() {
        return this.description;
    }

    public PlateformePaiement description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        this.commandeId = commande != null ? commande.getId() : null;
    }

    public PlateformePaiement commande(Commande commande) {
        this.setCommande(commande);
        return this;
    }

    public Long getCommandeId() {
        return this.commandeId;
    }

    public void setCommandeId(Long commande) {
        this.commandeId = commande;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlateformePaiement)) {
            return false;
        }
        return id != null && id.equals(((PlateformePaiement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlateformePaiement{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
