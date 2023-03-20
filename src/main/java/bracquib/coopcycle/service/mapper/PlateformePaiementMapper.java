package bracquib.coopcycle.service.mapper;

import bracquib.coopcycle.domain.Commande;
import bracquib.coopcycle.domain.PlateformePaiement;
import bracquib.coopcycle.service.dto.CommandeDTO;
import bracquib.coopcycle.service.dto.PlateformePaiementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlateformePaiement} and its DTO {@link PlateformePaiementDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlateformePaiementMapper extends EntityMapper<PlateformePaiementDTO, PlateformePaiement> {
    @Mapping(target = "commande", source = "commande", qualifiedByName = "commandeCreationDate")
    PlateformePaiementDTO toDto(PlateformePaiement s);

    @Named("commandeCreationDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "creationDate", source = "creationDate")
    CommandeDTO toDtoCommandeCreationDate(Commande commande);
}
