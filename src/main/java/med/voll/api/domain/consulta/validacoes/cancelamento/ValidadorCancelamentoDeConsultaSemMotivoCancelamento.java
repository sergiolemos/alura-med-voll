package med.voll.api.domain.consulta.validacoes.cancelamento;

import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.infra.exception.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorCancelamentoDeConsultaSemMotivoCancelamento implements ValidadorCancelamentoConsulta{

    public void validar(DadosCancelamentoConsulta dados){
        if(dados.motivo() == null){
            throw new ValidacaoException("Motivo do cancelamento é obrigatório");
        }
    }
}
