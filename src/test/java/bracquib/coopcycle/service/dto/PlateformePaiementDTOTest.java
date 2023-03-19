package bracquib.coopcycle.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import bracquib.coopcycle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlateformePaiementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlateformePaiementDTO.class);
        PlateformePaiementDTO plateformePaiementDTO1 = new PlateformePaiementDTO();
        plateformePaiementDTO1.setId(1L);
        PlateformePaiementDTO plateformePaiementDTO2 = new PlateformePaiementDTO();
        assertThat(plateformePaiementDTO1).isNotEqualTo(plateformePaiementDTO2);
        plateformePaiementDTO2.setId(plateformePaiementDTO1.getId());
        assertThat(plateformePaiementDTO1).isEqualTo(plateformePaiementDTO2);
        plateformePaiementDTO2.setId(2L);
        assertThat(plateformePaiementDTO1).isNotEqualTo(plateformePaiementDTO2);
        plateformePaiementDTO1.setId(null);
        assertThat(plateformePaiementDTO1).isNotEqualTo(plateformePaiementDTO2);
    }
}
