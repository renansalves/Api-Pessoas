package br.tec.db.Pessoa.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.tec.db.Pessoa.dto.PessoaDto;
import br.tec.db.Pessoa.handler.NotFoundException;
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

  public PessoaDto salvarPessoa(PessoaDto pessoaDto) {
    Pessoa pessoaEntidade = pessoaMapper.toEntity(pessoaDto);
    Pessoa entidadeSalva = repositorioPessoa.save(pessoaEntidade);

    return pessoaMapper.toDto(entidadeSalva);
  }

  public PessoaDto listarUmaPessoaPorId(Long id) {
    Pessoa pessoa = retornaPessoaEncontrada(id);

    return pessoaMapper.toDto(pessoa);
  }

  public List<PessoaDto> listarPessoas() {
    return repositorioPessoa.findAll().stream()
        .map(pessoaMapper::toDto)
        .collect(Collectors.toList());
  }

  public PessoaDto atualizarPessoa(Long id, PessoaDto pessoaDto) {
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
    return pessoaMapper.toDto(entidadeAtualizada);
  }

  public void deletarPessoa(Long id) {
    Pessoa pessoa = retornaPessoaEncontrada(id);

    repositorioPessoa.delete(pessoa);
  }

  public int calculaIdadePessoa(Long id) {
    Pessoa pessoa = retornaPessoaEncontrada(id);
    LocalDate hoje = LocalDate.now();
    Period idade = Period.between(pessoa.getDataNascimento(), hoje);

    return idade.getYears();

  }
  public Pessoa retornaPessoaEncontrada(Long id){
    return repositorioPessoa.findById(id)
        .orElseThrow(() -> new NotFoundException("Pessoa não encontrada com id: " + id));
  }
}
