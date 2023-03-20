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
    @Mapping(target = "client", source = "client", qualifiedByName = "clientName")
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantName")
    @Mapping(target = "livreur", source = "livreur", qualifiedByName = "livreurName")
    SocietaireDTO toDto(Societaire s);

    @Named("clientName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ClientDTO toDtoClientName(Client client);

    @Named("restaurantName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RestaurantDTO toDtoRestaurantName(Restaurant restaurant);

    @Named("livreurName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LivreurDTO toDtoLivreurName(Livreur livreur);
}
