package br.tec.db.Pessoa.servicoTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.tec.db.Pessoa.Servico.ServicoPessoa;
import br.tec.db.Pessoa.dto.EnderecoDto;
import br.tec.db.Pessoa.dto.PessoaDto;
import br.tec.db.Pessoa.excecao.PessoaNaoEncontradaExcecao;
import br.tec.db.Pessoa.map.EnderecoMapperInterface;
import br.tec.db.Pessoa.map.PessoaMapperInterface;
import br.tec.db.Pessoa.modelo.Pessoa;
import br.tec.db.Pessoa.repositorio.RepositorioPessoa;

@ExtendWith(MockitoExtension.class)

public class ServicoPessoaTest {

  @InjectMocks
  private ServicoPessoa servicoPessoa;

  @Mock
  private RepositorioPessoa repositorioPessoa;
  @Mock
  private PessoaMapperInterface pessoaMapper;
  @Mock
  private EnderecoMapperInterface enderecoMapper;

  private Pessoa pessoaEntidade;
  private PessoaDto pessoaDto;
  private EnderecoDto enderecoDto;

  @BeforeEach
  void setup() {
    enderecoDto = new EnderecoDto("Rua Teste", 100, "Bairro Teste", "Cidade Teste", "SP", "00000000");

    pessoaEntidade = new Pessoa();
    pessoaEntidade.setId(1L);
    pessoaEntidade.setNome("Teste Unitario");
    pessoaEntidade.setCpf("123.456.789-00");
    pessoaEntidade.setDataNascimento(LocalDate.of(1990, 1, 1));

    pessoaDto = new PessoaDto(1L, "Teste Unitario", "123.456.789-00", LocalDate.of(1990, 1, 1),
        Arrays.asList(enderecoDto), null);
  }

  @Test
  void salvarPessoa_DeveRetornarPessoaSalva() {

    when(pessoaMapper.toEntity(any(PessoaDto.class))).thenReturn(pessoaEntidade);
    when(repositorioPessoa.save(any(Pessoa.class))).thenReturn(pessoaEntidade);
    when(pessoaMapper.toDto(any(Pessoa.class))).thenReturn(pessoaDto);

    PessoaDto resultado = servicoPessoa.salvarPessoa(pessoaDto);

    assertNotNull(resultado);
    assertEquals(1L, resultado.id());
    verify(repositorioPessoa, times(1)).save(pessoaEntidade);
    verify(pessoaMapper, times(1)).toEntity(pessoaDto);
    verify(pessoaMapper, times(1)).toDto(pessoaEntidade);
  }

  @Test
  void listarUmaPessoaPorId_DeveRetornarPessoaExistente() {

    when(repositorioPessoa.findById(1L)).thenReturn(Optional.of(pessoaEntidade));
    when(pessoaMapper.toDto(any(Pessoa.class))).thenReturn(pessoaDto);

    PessoaDto resultado = servicoPessoa.listarUmaPessoaPorId(1L);

    assertNotNull(resultado);
    assertEquals("Teste Unitario", resultado.nome());
    verify(repositorioPessoa, times(1)).findById(1L);
  }

  @Test
  void listarUmaPessoaPorId_DeveLancarExcecaoQuandoNaoEncontrada() {

    when(repositorioPessoa.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(PessoaNaoEncontradaExcecao.class, () -> servicoPessoa.listarUmaPessoaPorId(99L));
    verify(repositorioPessoa, times(1)).findById(99L);
  }

  @Test
  void deletarPessoa_DeveDeletarPessoaExistente() {

    when(repositorioPessoa.findById(1L)).thenReturn(Optional.of(pessoaEntidade));
    doNothing().when(repositorioPessoa).delete(any(Pessoa.class));

    assertDoesNotThrow(() -> servicoPessoa.deletarPessoa(1L));

    verify(repositorioPessoa, times(1)).findById(1L);
    verify(repositorioPessoa, times(1)).delete(pessoaEntidade);
  }
}
