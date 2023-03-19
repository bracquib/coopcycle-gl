package bracquib.coopcycle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import bracquib.coopcycle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlateformePaiementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlateformePaiement.class);
        PlateformePaiement plateformePaiement1 = new PlateformePaiement();
        plateformePaiement1.setId(1L);
        PlateformePaiement plateformePaiement2 = new PlateformePaiement();
        plateformePaiement2.setId(plateformePaiement1.getId());
        assertThat(plateformePaiement1).isEqualTo(plateformePaiement2);
        plateformePaiement2.setId(2L);
        assertThat(plateformePaiement1).isNotEqualTo(plateformePaiement2);
        plateformePaiement1.setId(null);
        assertThat(plateformePaiement1).isNotEqualTo(plateformePaiement2);
    }
}
