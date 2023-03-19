package bracquib.coopcycle.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LivreurMapperTest {

    private LivreurMapper livreurMapper;

    @BeforeEach
    public void setUp() {
        livreurMapper = new LivreurMapperImpl();
    }
}
