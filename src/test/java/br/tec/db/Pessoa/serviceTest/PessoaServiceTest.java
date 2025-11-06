package br.tec.db.Pessoa.serviceTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.tec.db.Pessoa.builder.PessoaBuilder;
import br.tec.db.Pessoa.dto.EnderecoDto;
import br.tec.db.Pessoa.dto.PessoaRequestDto;
import br.tec.db.Pessoa.dto.PessoaResponseDto;
import br.tec.db.Pessoa.handler.NotFoundException;
import br.tec.db.Pessoa.map.EnderecoMapperInterface;
import br.tec.db.Pessoa.map.PessoaMapperInterface;
import br.tec.db.Pessoa.model.Pessoa;
import br.tec.db.Pessoa.repository.PessoaRepository;
import br.tec.db.Pessoa.service.PessoaService;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

  @InjectMocks
  private PessoaService servicoPessoa;

  @Mock
  private PessoaRepository repositorioPessoa;
  @Mock
  private PessoaMapperInterface pessoaMapper;
  @Mock
  private EnderecoMapperInterface enderecoMapper;

  private PessoaBuilder pessoaBuilder;

  private EnderecoDto enderecoDto;
  private Pessoa pessoaEntity;
  PessoaResponseDto pessoaResponse;
  PessoaRequestDto pessoaRequest;

  @BeforeEach
  void setup() {
  pessoaBuilder = new PessoaBuilder();
    
    this.pessoaEntity = pessoaBuilder.construir(); 
    this.pessoaRequest = new PessoaRequestDto(
        "Renan Alves", 
        "019.533.650-00",
        LocalDate.of(1989,06,03),
        null
    ); 
  }

  @Test
  void salvarPessoa_DeveRetornarPessoaSalva() {
    PessoaRequestDto entradaDto = this.pessoaRequest; 

    Pessoa pessoaParaSalvar = pessoaBuilder.construir(); 
    
    Pessoa pessoaSalva = pessoaBuilder.construir(); 
    
    PessoaRequestDto saidaDto = new PessoaRequestDto(
        pessoaSalva.getNome(), 
        pessoaSalva.getCpf(), 
        pessoaSalva.getDataNascimento(), 
        null
    );
    

    when(pessoaMapper.toEntity(entradaDto)).thenReturn(pessoaParaSalvar); 

    when(repositorioPessoa.save(eq(pessoaParaSalvar))).thenReturn(pessoaSalva);
    when(pessoaMapper.requestToDto(pessoaSalva)).thenReturn(saidaDto); 

    PessoaResponseDto resultado = servicoPessoa.salvarPessoa(entradaDto); 

    assertNotNull(resultado);
//    assertEquals(saidaDto.id(), resultado.id());
    
    verify(pessoaMapper, times(1)).toEntity(entradaDto);
    verify(repositorioPessoa, times(1)).save(pessoaParaSalvar); 
    verify(pessoaMapper, times(1)).requestToDto(pessoaSalva);
}

  @Test
  void listarUmaPessoaPorId_DeveRetornarPessoaExistente() {

    Pessoa pessoa = pessoaBuilder.construir();

    when(repositorioPessoa.findById(1L)).thenReturn(Optional.of(pessoa));
    when(pessoaMapper.requestToDto(pessoa)).thenReturn(pessoaRequest);

    PessoaResponseDto resultado = servicoPessoa.listarUmaPessoaPorId(1L);

    assertNotNull(resultado);
    assertEquals("Renan Alves", resultado.nome());
    verify(repositorioPessoa, times(1)).findById(1L);
  }

  @Test
  void listarUmaPessoaPorId_DeveLancarExcecaoQuandoNaoEncontrada() {

    when(repositorioPessoa.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> servicoPessoa.listarUmaPessoaPorId(99L));
    verify(repositorioPessoa, times(1)).findById(99L);
  }

  @Test
  void deletarPessoa_DeveDeletarPessoaExistente() {

    Pessoa pessoa = pessoaBuilder.construir();

    when(repositorioPessoa.findById(1L)).thenReturn(Optional.of(pessoa));
    doNothing().when(repositorioPessoa).delete(any(Pessoa.class));

    assertDoesNotThrow(() -> servicoPessoa.deletarPessoa(1L));

    verify(repositorioPessoa, times(1)).findById(1L);
    verify(repositorioPessoa, times(1)).delete(pessoa);
  }
}
