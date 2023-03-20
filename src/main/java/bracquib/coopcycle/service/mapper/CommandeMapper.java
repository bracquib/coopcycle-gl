package bracquib.coopcycle.service.mapper;

import bracquib.coopcycle.domain.Client;
import bracquib.coopcycle.domain.Commande;
import bracquib.coopcycle.domain.Livreur;
import bracquib.coopcycle.domain.Restaurant;
import bracquib.coopcycle.service.dto.ClientDTO;
import bracquib.coopcycle.service.dto.CommandeDTO;
import bracquib.coopcycle.service.dto.LivreurDTO;
import bracquib.coopcycle.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commande} and its DTO {@link CommandeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommandeMapper extends EntityMapper<CommandeDTO, Commande> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientName")
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantName")
    @Mapping(target = "livreur", source = "livreur", qualifiedByName = "livreurName")
    CommandeDTO toDto(Commande s);

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
