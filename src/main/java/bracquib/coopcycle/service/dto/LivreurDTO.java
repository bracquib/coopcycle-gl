package bracquib.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link bracquib.coopcycle.domain.Livreur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LivreurDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(min = 2, max = 50)
    private String name;

    @NotNull(message = "must not be null")
    @Size(min = 2, max = 50)
    private String surname;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
    private String email;

    @Pattern(regexp = "^([0-9]{2}[ -]?){4}[0-9]{2}$")
    private String phonenumber;

    @NotNull(message = "must not be null")
    private String vehicleType;

    @NotNull(message = "must not be null")
    private String commandState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCommandState() {
        return commandState;
    }

    public void setCommandState(String commandState) {
        this.commandState = commandState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivreurDTO)) {
            return false;
        }

        LivreurDTO livreurDTO = (LivreurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, livreurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivreurDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", phonenumber='" + getPhonenumber() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", commandState='" + getCommandState() + "'" +
            "}";
    }
}
