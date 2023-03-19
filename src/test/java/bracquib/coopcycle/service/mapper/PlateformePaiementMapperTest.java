package bracquib.coopcycle.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlateformePaiementMapperTest {

    private PlateformePaiementMapper plateformePaiementMapper;

    @BeforeEach
    public void setUp() {
        plateformePaiementMapper = new PlateformePaiementMapperImpl();
    }
}
