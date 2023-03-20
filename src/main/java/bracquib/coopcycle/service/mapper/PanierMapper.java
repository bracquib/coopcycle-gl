package bracquib.coopcycle.service.mapper;

import bracquib.coopcycle.domain.Client;
import bracquib.coopcycle.domain.Commande;
import bracquib.coopcycle.domain.Panier;
import bracquib.coopcycle.service.dto.ClientDTO;
import bracquib.coopcycle.service.dto.CommandeDTO;
import bracquib.coopcycle.service.dto.PanierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Panier} and its DTO {@link PanierDTO}.
 */
@Mapper(componentModel = "spring")
public interface PanierMapper extends EntityMapper<PanierDTO, Panier> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientName")
    @Mapping(target = "commande", source = "commande", qualifiedByName = "commandeCreationDate")
    PanierDTO toDto(Panier s);

    @Named("clientName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ClientDTO toDtoClientName(Client client);

    @Named("commandeCreationDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "creationDate", source = "creationDate")
    CommandeDTO toDtoCommandeCreationDate(Commande commande);
}
