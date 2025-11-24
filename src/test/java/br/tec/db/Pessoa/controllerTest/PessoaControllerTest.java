package br.tec.db.Pessoa.controllerTest;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.tec.db.Pessoa.builder.PessoaBuilder;
import br.tec.db.Pessoa.controller.PessoaController;
import br.tec.db.Pessoa.dto.PessoaRequestDto;
import br.tec.db.Pessoa.dto.PessoaResponseDto;
import br.tec.db.Pessoa.handler.PessoaNaoEncontradaException;
import br.tec.db.Pessoa.service.PessoaService;

@WebMvcTest(PessoaController.class)
public class PessoaControllerTest {

  @MockBean
  private PessoaService servicoPessoa;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private final String BASE_URL = "/pessoa";


  @Test
  void deveCriarPessoal_E_DeveRetornarStatus201_E_Localizacao() throws Exception {

    PessoaRequestDto requestDto =  PessoaBuilder.umUsuario().criarPessoaRequestDto();
    PessoaResponseDto responseDto = PessoaBuilder.umUsuario().criarPessoaResponseDto();

    when(servicoPessoa.salvarPessoa(eq(requestDto))).thenReturn(responseDto);

    mockMvc.perform(post(BASE_URL + "/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestDto)))

        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.nome", is("Joao Pedro")));

    verify(servicoPessoa, times(1)).salvarPessoa(eq(requestDto));
  }



  @Test
  void listarUmaPessoaPorId_DeveRetornarStatus200_E_Pessoa() throws Exception {
    PessoaResponseDto responseDto = PessoaBuilder.umUsuario().criarPessoaResponseDto();

    when(servicoPessoa.listarUmaPessoaPorId(anyLong())).thenReturn(responseDto);
  
    mockMvc.perform(get(BASE_URL +"/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Joao Pedro"));
   }

  @Test
  void listarUmaPessoaPorId_DeveRetornarStatus404_QuandoNaoEncontrada() throws Exception {
      // Arrange
      when(servicoPessoa.listarUmaPessoaPorId(99L))
          .thenThrow(new PessoaNaoEncontradaException("Pessoa não encontrada."));

      // Act & Assert
      mockMvc.perform(get(BASE_URL + "/{id}", 99L)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());

      verify(servicoPessoa, times(1)).listarUmaPessoaPorId(99L);
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

      doThrow(new PessoaNaoEncontradaException("Pessoa não encontrada"))
          .when(servicoPessoa).deletarPessoa(99L);

      mockMvc.perform(delete(BASE_URL + "/{id}", 99L))
          .andExpect(status().isNotFound());

      verify(servicoPessoa, times(1)).deletarPessoa(99L);
  }


@Test
void atualizarPessoa_DeveRetornarStatus200_E_PessoaAtualizada() throws Exception {
    PessoaRequestDto pessoaAtualizada = new PessoaRequestDto("Atualizado", "000.000.000-00", LocalDate.of(1990, 1, 1), Arrays.asList());
    PessoaResponseDto responseDto = new PessoaResponseDto(1L, "Atualizado", "000.000.000-00", 33, LocalDate.of(1990, 1, 1), Arrays.asList());

    when(servicoPessoa.atualizarPessoa(eq(1L), any(PessoaRequestDto.class))).thenReturn(responseDto);

    mockMvc.perform(put(BASE_URL + "/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pessoaAtualizada)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome", is("Atualizado")));

    verify(servicoPessoa, times(1)).atualizarPessoa(eq(1L), any(PessoaRequestDto.class));
}

  @Test
  void atualizarPessoa_DeveRetornarStatus404_QuandoNaoEncontrada() throws Exception {

    PessoaRequestDto pessoaNaoEncontrada = new PessoaRequestDto("Pessoa", "000.000.000-00", LocalDate.of(1990, 1, 1), Arrays.asList());

    when(servicoPessoa.atualizarPessoa(eq(99L), any(PessoaRequestDto.class))).thenThrow(new PessoaNaoEncontradaException("Pessoa não encontrada"));
    
    mockMvc.perform(put(BASE_URL + "/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(pessoaNaoEncontrada)))
        .andExpect(status().isNotFound());
  }
}
