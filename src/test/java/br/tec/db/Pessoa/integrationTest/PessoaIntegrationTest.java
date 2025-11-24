package br.tec.db.Pessoa.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PessoaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/pessoa";

    private Long pessoaIdCriada;

    @BeforeEach
    void setUp() throws Exception {
        // Limpa e prepara o banco antes de cada teste
        criarPessoaParaTeste();
    }

    private void criarPessoaParaTeste() throws Exception {
        String pessoaJson = """
            {
                "nome": "João Silva Teste",
                "cpf": "123.456.789-00",
                "dataNascimento": "1985-05-20",
                "enderecos": []
            }
            """;

        MvcResult result = mockMvc.perform(post(BASE_URL + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pessoaJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Extrai o ID da pessoa criada
        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        pessoaIdCriada = jsonNode.get("id").asLong();
    }

    @Test
    void criarPessoa_DeveRetornarStatus201() throws Exception {
        String novaPessoaJson = """
            {
                "nome": "Maria Oliveira",
                "cpf": "987.654.321-00",
                "dataNascimento": "1990-08-15",
                "enderecos": []
            }
            """;

        mockMvc.perform(post(BASE_URL + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaPessoaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Maria Oliveira"))
                .andExpect(jsonPath("$.cpf").value("987.654.321-00"))
                .andExpect(header().exists("Location"));
    }

    @Test
    void listarPessoas_DeveRetornarStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("João Silva Teste"));
    }

    @Test
    void buscarPessoaPorId_DeveRetornarStatus200() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", pessoaIdCriada)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pessoaIdCriada))
                .andExpect(jsonPath("$.nome").value("João Silva Teste"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"));
    }

    @Test
    void buscarPessoaPorId_DeveRetornarStatus404_QuandoNaoEncontrada() throws Exception {
        Long idNaoExistente = 9999L;

        mockMvc.perform(get(BASE_URL + "/{id}", idNaoExistente)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarPessoa_DeveRetornarStatus200() throws Exception {
        String pessoaAtualizadaJson = """
            {
                "nome": "João Silva Atualizado",
                "cpf": "123.456.789-00",
                "dataNascimento": "1985-05-20",
                "enderecos": []
            }
            """;

        mockMvc.perform(put(BASE_URL + "/{id}", pessoaIdCriada)
                .contentType(MediaType.APPLICATION_JSON)
                .content(pessoaAtualizadaJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pessoaIdCriada))
                .andExpect(jsonPath("$.nome").value("João Silva Atualizado"));
    }

    @Test
    void atualizarPessoa_DeveRetornarStatus404_QuandoNaoEncontrada() throws Exception {
        Long idNaoExistente = 9999L;
        String pessoaJson = """
            {
                "nome": "Pessoa Inexistente",
                "cpf": "111.222.333-44",
                "dataNascimento": "2000-01-01",
                "enderecos": []
            }
            """;

        mockMvc.perform(put(BASE_URL + "/{id}", idNaoExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(pessoaJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarPessoa_DeveRetornarStatus204() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", pessoaIdCriada))
                .andExpect(status().isNoContent());

        // Verifica se realmente foi deletado
        mockMvc.perform(get(BASE_URL + "/{id}", pessoaIdCriada))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarPessoa_DeveRetornarStatus404_QuandoNaoEncontrada() throws Exception {
        Long idNaoExistente = 9999L;

        mockMvc.perform(delete(BASE_URL + "/{id}", idNaoExistente))
                .andExpect(status().isNotFound());
    }

    @Test
    void fluxoCompleto_CriarListarAtualizarDeletar() throws Exception {
        // 1. Criar nova pessoa
        String novaPessoaJson = """
            {
                "nome": "Carlos Santos",
                "cpf": "555.666.777-88",
                "dataNascimento": "1978-12-10",
                "enderecos": []
            }
            """;

        MvcResult createResult = mockMvc.perform(post(BASE_URL + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaPessoaJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Extrai ID da pessoa criada
        String createResponse = createResult.getResponse().getContentAsString();
        JsonNode createJson = objectMapper.readTree(createResponse);
        Long novoPessoaId = createJson.get("id").asLong();

        // 2. Listar pessoas (deve conter a nova pessoa)
        mockMvc.perform(get(BASE_URL + "/")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.id == " + novoPessoaId + ")]").exists());

        // 3. Buscar pessoa específica
        mockMvc.perform(get(BASE_URL + "/{id}", novoPessoaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Santos"));

        // 4. Atualizar pessoa
        String atualizacaoJson = """
            {
                "nome": "Carlos Santos Atualizado",
                "cpf": "555.666.777-88",
                "dataNascimento": "1978-12-10",
                "enderecos": []
            }
            """;

        mockMvc.perform(put(BASE_URL + "/{id}", novoPessoaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(atualizacaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Santos Atualizado"));

        // 5. Deletar pessoa
        mockMvc.perform(delete(BASE_URL + "/{id}", novoPessoaId))
                .andExpect(status().isNoContent());

        // 6. Verificar que foi deletada
        mockMvc.perform(get(BASE_URL + "/{id}", novoPessoaId))
                .andExpect(status().isNotFound());
    }

}
