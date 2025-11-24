package br.tec.db.Pessoa.serviceTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.tec.db.Pessoa.builder.PessoaBuilder;
import br.tec.db.Pessoa.dto.PessoaRequestDto;
import br.tec.db.Pessoa.dto.PessoaResponseDto;
import br.tec.db.Pessoa.handler.PessoaNaoEncontradaException; 
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
    private Pessoa pessoaEntity;
    private PessoaResponseDto pessoaResponse;
    private PessoaRequestDto pessoaRequest;

    @BeforeEach
    void setup() {
        pessoaBuilder = new PessoaBuilder();
        this.pessoaEntity = pessoaBuilder.criarPessoa(); 
        this.pessoaRequest = pessoaBuilder.criarPessoaRequestDto();
        this.pessoaResponse = pessoaBuilder.criarPessoaResponseDto();
    }

    @Test
    void deveCriarUmaPessoaComSucesso() {
        when(pessoaMapper.toEntity(pessoaRequest)).thenReturn(pessoaEntity); 
        when(repositorioPessoa.save(any(Pessoa.class))).thenReturn(pessoaEntity);
        when(pessoaMapper.responseToDto(pessoaEntity)).thenReturn(pessoaBuilder.criarPessoaResponseDto()); 

        PessoaResponseDto resultado = servicoPessoa.salvarPessoa(pessoaRequest); 

        assertNotNull(resultado);
        verify(pessoaMapper, times(1)).toEntity(pessoaRequest);
        verify(repositorioPessoa, times(1)).save(pessoaEntity); 
        verify(pessoaMapper, times(1)).responseToDto(pessoaEntity);
    }

    @Test
    void listarUmaPessoaPorId_DeveRetornarPessoaExistente() {
        Pessoa pessoa = pessoaBuilder.criarPessoa();
        when(repositorioPessoa.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaMapper.responseToDto(pessoa)).thenReturn(pessoaResponse);

        PessoaResponseDto resultado = servicoPessoa.listarUmaPessoaPorId(1L);

        assertNotNull(resultado);
        assertEquals("Joao Pedro", resultado.nome());
        verify(repositorioPessoa, times(1)).findById(1L);
        verify(pessoaMapper, times(1)).responseToDto(pessoa);
    }

    @Test
    void listarUmaPessoaPorId_DeveLancarExcecaoQuandoNaoEncontrada() {
        Long idNaoExistente = 99L;
        when(repositorioPessoa.findById(idNaoExistente)).thenReturn(Optional.empty());

        assertThrows(PessoaNaoEncontradaException.class, 
                () -> servicoPessoa.listarUmaPessoaPorId(idNaoExistente));
        
        verify(repositorioPessoa, times(1)).findById(idNaoExistente);
    }

  @Test
  void deletarPessoa_DeveDeletarPessoaExistente() {
      Pessoa pessoa = pessoaBuilder.criarPessoa();
      when(repositorioPessoa.findById(1L)).thenReturn(Optional.of(pessoa));
      doNothing().when(repositorioPessoa).delete(pessoa);

      assertDoesNotThrow(() -> servicoPessoa.deletarPessoa(1L));

      verify(repositorioPessoa, times(1)).findById(1L);
      verify(repositorioPessoa, times(1)).delete(pessoa);
  }

  @Test
  void deletarPessoa_DeveLancarExcecaoQuandoNaoEncontrada() {
      // Arrange - ID que não existe
      Long idNaoExistente = 99L;
      when(repositorioPessoa.findById(idNaoExistente)).thenReturn(Optional.empty());

      // Act & Assert
      assertThrows(PessoaNaoEncontradaException.class, 
              () -> servicoPessoa.deletarPessoa(idNaoExistente));
  }

  @Test
  void atualizarPessoa_DeveAtualizarPessoaExistente() {
      Pessoa pessoaExistente = PessoaBuilder.umUsuario().comId(1L).criarPessoa();
      PessoaRequestDto requestDto = PessoaBuilder.umUsuario()
          .comNome("Novo Nome")
          .comCpf("111.222.333-44")
          .criarPessoaRequestDto();
      
      Pessoa pessoaAtualizada = PessoaBuilder.umUsuario()
          .comNome("Novo Nome")
          .comCpf("111.222.333-44")
          .criarPessoa();
      
      PessoaResponseDto responseDto = PessoaBuilder.umUsuario()
          .comNome("Novo Nome")
          .comCpf("111.222.333-44")
          .criarPessoaResponseDto();

      when(repositorioPessoa.findById(1L)).thenReturn(Optional.of(pessoaExistente));
      when(repositorioPessoa.save(any(Pessoa.class))).thenReturn(pessoaAtualizada);
      when(pessoaMapper.responseToDto(pessoaAtualizada)).thenReturn(responseDto);

      PessoaResponseDto resultado = servicoPessoa.atualizarPessoa(1L, requestDto);

      assertEquals("Novo Nome", resultado.nome());
  }
    @Test
    void atualizarPessoa_DeveLancarExcecaoQuandoNaoEncontrada() {
        Long idNaoExistente = 99L;
        PessoaRequestDto requestDto = new PessoaRequestDto("Nome", "000.000.000-00", null, null);
        when(repositorioPessoa.findById(idNaoExistente)).thenReturn(Optional.empty());

        assertThrows(PessoaNaoEncontradaException.class, 
                () -> servicoPessoa.atualizarPessoa(idNaoExistente, requestDto));
        
        verify(repositorioPessoa, times(1)).findById(idNaoExistente);
        verify(repositorioPessoa, times(0)).save(any(Pessoa.class));
    }
}
