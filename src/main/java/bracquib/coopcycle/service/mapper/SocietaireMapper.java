package bracquib.coopcycle.service.mapper;

import bracquib.coopcycle.domain.Client;
import bracquib.coopcycle.domain.Livreur;
import bracquib.coopcycle.domain.Restaurant;
import bracquib.coopcycle.domain.Societaire;
import bracquib.coopcycle.service.dto.ClientDTO;
import bracquib.coopcycle.service.dto.LivreurDTO;
import bracquib.coopcycle.service.dto.RestaurantDTO;
import bracquib.coopcycle.service.dto.SocietaireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Societaire} and its DTO {@link SocietaireDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocietaireMapper extends EntityMapper<SocietaireDTO, Societaire> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantId")
    @Mapping(target = "livreur", source = "livreur", qualifiedByName = "livreurId")
    SocietaireDTO toDto(Societaire s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("restaurantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurantDTO toDtoRestaurantId(Restaurant restaurant);

    @Named("livreurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LivreurDTO toDtoLivreurId(Livreur livreur);
}
