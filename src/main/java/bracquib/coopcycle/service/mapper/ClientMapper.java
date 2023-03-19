package bracquib.coopcycle.service.mapper;

import bracquib.coopcycle.domain.Client;
import bracquib.coopcycle.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {}
