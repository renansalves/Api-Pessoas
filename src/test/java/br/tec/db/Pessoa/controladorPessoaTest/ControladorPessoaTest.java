package br.tec.db.Pessoa.controladorPessoaTest;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.tec.db.Pessoa.Servico.ServicoPessoa;
import br.tec.db.Pessoa.controlador.*;
import br.tec.db.Pessoa.dto.PessoaDto;
import br.tec.db.Pessoa.excecao.PessoaNaoEncontradaExcecao;

@WebMvcTest(ControladorPessoa.class)

public class ControladorPessoaTest {

  @MockBean
  private ServicoPessoa servicoPessoa;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private PessoaDto pessoaDto;
  private final String BASE_URL = "/pessoa";

  @BeforeEach
  void setup() {
    pessoaDto = new PessoaDto(1L, "Mock Teste", "000.000.000-00", LocalDate.of(1990, 1, 1), Arrays.asList(), null);
  }

  @Test
  void salvarPessoa_DeveRetornarStatus201_E_Localizacao() throws Exception {

    when(servicoPessoa.salvarPessoa(any(PessoaDto.class))).thenReturn(pessoaDto);

    mockMvc.perform(post(BASE_URL + "/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(pessoaDto)))

        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.nome", is("Mock Teste")));

    verify(servicoPessoa, times(1)).salvarPessoa(any(PessoaDto.class));
  }

  @Test
  void listarUmaPessoaPorId_DeveRetornarStatus200_E_Pessoa() throws Exception {

    when(servicoPessoa.listarUmaPessoaPorId(1L)).thenReturn(pessoaDto);

    mockMvc.perform(get(BASE_URL + "/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)));
  }

  @Test
  void listarUmaPessoaPorId_DeveRetornarStatus404_QuandoNaoEncontrada() throws Exception {

    when(servicoPessoa.listarUmaPessoaPorId(99L)).thenThrow(PessoaNaoEncontradaExcecao.class);

    mockMvc.perform(get(BASE_URL + "/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().isNotFound());
  }

  @Test
  void deletarPessoa_DeveRetornarStatus204_QuandoExistente() throws Exception {

    doNothing().when(servicoPessoa).deletarPessoa(1L);

    mockMvc.perform(delete(BASE_URL + "/{id}", 1L))

        .andExpect(status().isNoContent());

    verify(servicoPessoa, times(1)).deletarPessoa(1L);
  }

  @Test
  void deletarPessoa_DeveRetornarStatus404_QuandoNaoEncontrada() throws Exception {

    doThrow(PessoaNaoEncontradaExcecao.class).when(servicoPessoa).deletarPessoa(99L);

    mockMvc.perform(delete(BASE_URL + "/{id}", 99L))

        .andExpect(status().isNotFound());
  }

  @Test
  void atualizarPessoa_DeveRetornarStatus200_E_PessoaAtualizada() throws Exception {

    PessoaDto pessoaAtualizada = new PessoaDto(1L, "Atualizado", "000.000.000-00", LocalDate.of(1990, 1, 1),
        Arrays.asList(), null);

    when(servicoPessoa.atualizarPessoa(eq(1L), any(PessoaDto.class))).thenReturn(pessoaAtualizada);

    mockMvc.perform(put(BASE_URL + "/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(pessoaDto)))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome", is("Atualizado")));

    verify(servicoPessoa, times(1)).atualizarPessoa(eq(1L), any(PessoaDto.class));
  }

  @Test
  void atualizarPessoa_DeveRetornarStatus404_QuandoNaoEncontrada() throws Exception {

    when(servicoPessoa.atualizarPessoa(eq(99L), any(PessoaDto.class))).thenThrow(PessoaNaoEncontradaExcecao.class);

    mockMvc.perform(put(BASE_URL + "/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(pessoaDto)))

        .andExpect(status().isNotFound());
  }
}
