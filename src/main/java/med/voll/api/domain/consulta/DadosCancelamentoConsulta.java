package med.voll.api.domain.consulta;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
        @NotNull
        @JsonAlias("consulta_id")
        Long idConsulta,
        @NotNull
        @JsonAlias("motivo_cancelamento")
        MotivoCancelamento motivo
) {
}
