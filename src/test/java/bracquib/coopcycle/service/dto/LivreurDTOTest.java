package bracquib.coopcycle.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import bracquib.coopcycle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivreurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivreurDTO.class);
        LivreurDTO livreurDTO1 = new LivreurDTO();
        livreurDTO1.setId(1L);
        LivreurDTO livreurDTO2 = new LivreurDTO();
        assertThat(livreurDTO1).isNotEqualTo(livreurDTO2);
        livreurDTO2.setId(livreurDTO1.getId());
        assertThat(livreurDTO1).isEqualTo(livreurDTO2);
        livreurDTO2.setId(2L);
        assertThat(livreurDTO1).isNotEqualTo(livreurDTO2);
        livreurDTO1.setId(null);
        assertThat(livreurDTO1).isNotEqualTo(livreurDTO2);
    }
}
