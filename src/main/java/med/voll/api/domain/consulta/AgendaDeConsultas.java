package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.exception.ValidacaoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {

    private ConsultaRepository consultaRepository;
    private MedicoRepository medicoRepository;
    private PacienteRepository pacienteRepository;

    private List<ValidadorAgendamentoDeConsulta> validadores;
    private List<ValidadorCancelamentoConsulta> validadoresCancelamento;

    public AgendaDeConsultas(ConsultaRepository consultaRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository, List<ValidadorAgendamentoDeConsulta> validadores, List<ValidadorCancelamentoConsulta> validadoresCancelamento) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.validadores = validadores;
        this.validadoresCancelamento = validadoresCancelamento;
    }

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados){

        if(!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidacaoException("Id do paciente não existe");
        }
        if(dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("Id do médico não existe");
        }

        validadores.forEach(v -> v.validar(dados));

        var medico = escolherMedico(dados);
        if(medico == null){
            throw new ValidacaoException("Não existe médico disponivel nessa data");
        }
        var paciente  = pacienteRepository.getReferenceById(dados.idPaciente());
        var consulta = new Consulta(null,medico, paciente, dados.data() ,null,null);
        consultaRepository.save(consulta);
        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if(dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }
        if(dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatório quando médico não for escolhido");
        }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(),dados.data());

    }

    public void cancelar(DadosCancelamentoConsulta dados){
        if(dados.idConsulta() == null){
            throw new ValidacaoException("Id de consulta não é obrigatório");
        }else if(!consultaRepository.existsById(dados.idConsulta())){
            throw new ValidacaoException("Id da consulta não existe");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }
}
