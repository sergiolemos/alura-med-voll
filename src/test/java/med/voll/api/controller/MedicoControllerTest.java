package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.MedicoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;
    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;
    @Autowired
    private MedicoRepository medicoRepository;

    @Test
    @DisplayName("cadastro de medico com retorno 400")
    @WithMockUser
    void cadastrarMedicoCenario1() throws Exception {
        var response = mvc.perform(MockMvcRequestBuilders.post("/medicos"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("cadastro de medico com retorno 200")
    @WithMockUser
    void cadastrarMedicoCenarioSucesso() throws Exception {
        var dadosDetalhamento = new DadosDetalhamentoMedico(23L, "Medico 7", "medico7@mail.com", "86999530200", "7777", true, Especialidade.CARDIOLOGIA, new Endereco(dadosEndereco()));

        var response = mvc.perform(MockMvcRequestBuilders.post("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroMedicoJson.write(new DadosCadastroMedico(
                                "Medico 7", "medico7@mail.com", "86999530200", "7777", Especialidade.CARDIOLOGIA, dadosEndereco()
                        )).getJson())
                )
                .andReturn().getResponse();
        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}