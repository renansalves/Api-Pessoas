package br.tec.db.Pessoa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.tec.db.Pessoa.dto.PessoaRequestDto;
import br.tec.db.Pessoa.dto.PessoaResponseDto;
import br.tec.db.Pessoa.map.EnderecoMapperInterface;
import br.tec.db.Pessoa.map.PessoaMapperInterface;
import br.tec.db.Pessoa.model.Endereco;
import br.tec.db.Pessoa.model.Pessoa;
import br.tec.db.Pessoa.repository.PessoaRepository;


@Service
public class PessoaService {

  @Autowired
  private EnderecoMapperInterface enderecoMapper;
  @Autowired
  private PessoaMapperInterface pessoaMapper;
  @Autowired
  private PessoaRepository repositorioPessoa;

  public PessoaResponseDto salvarPessoa(PessoaRequestDto pessoaDto) {
    Pessoa pessoaEntidade = pessoaMapper.toEntity(pessoaDto);
    pessoaEntidade.validarEDefinirEnderecoPrincipal();
    Pessoa entidadeSalva = repositorioPessoa.save(pessoaEntidade);

    return pessoaMapper.responseToDto(entidadeSalva);
  }

  public PessoaResponseDto listarUmaPessoaPorId(Long id) {
    Pessoa pessoa = retornaPessoaEncontrada(id);

    return pessoaMapper.responseToDto(pessoa);
  }

  public Page<PessoaResponseDto> listarPessoas(Pageable pageable) {

    Page<Pessoa> pessoasPage= repositorioPessoa.findAll(pageable);

    return pessoasPage.map(pessoaMapper::responseToDto);
  }


  public PessoaResponseDto atualizarPessoa(Long id, PessoaRequestDto pessoaDto) {
      Pessoa entidadePessoa = retornaPessoaEncontrada(id); 

    if (pessoaDto.nome() != null)
      entidadePessoa.setNome(pessoaDto.nome());
    if (pessoaDto.cpf() != null)
      entidadePessoa.setCpf(pessoaDto.cpf());
    if (pessoaDto.dataNascimento() != null)
      entidadePessoa.setDataNascimento(pessoaDto.dataNascimento());

    if (pessoaDto.enderecos() != null) {
      List<Endereco> novosEnderecos = pessoaDto.enderecos().stream()
          .map(enderecoMapper::toEntity)
          .collect(Collectors.toList());

      entidadePessoa.getEnderecos().clear();

      entidadePessoa.getEnderecos().addAll(novosEnderecos);
    }

    Pessoa entidadeAtualizada = repositorioPessoa.save(entidadePessoa);
    return pessoaMapper.responseToDto(entidadeAtualizada);
  }

  public void deletarPessoa(Long id) {
    Pessoa pessoa = retornaPessoaEncontrada(id);

    repositorioPessoa.delete(pessoa);
  }

  public Pessoa retornaPessoaEncontrada(Long id){
    return repositorioPessoa.findById(id)
        .orElseThrow();
  }
}
