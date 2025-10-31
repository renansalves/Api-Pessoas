package br.tec.db.Pessoa.Servico;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.tec.db.Pessoa.dto.PessoaDto;
import br.tec.db.Pessoa.excecao.PessoaNaoEncontradaExcecao;
import br.tec.db.Pessoa.map.EnderecoMapperInterface;
import br.tec.db.Pessoa.map.PessoaMapperInterface;
import br.tec.db.Pessoa.modelo.Endereco;
import br.tec.db.Pessoa.modelo.Pessoa;
import br.tec.db.Pessoa.repositorio.RepositorioPessoa;
import lombok.RequiredArgsConstructor;

/**
 * ServicoPessoa
 */
@Service
@RequiredArgsConstructor
public class ServicoPessoa {

  private final EnderecoMapperInterface enderecoMapper;
  private final PessoaMapperInterface pessoaMapper;
  private final RepositorioPessoa repositorioPessoa;

  public PessoaDto salvarPessoa(PessoaDto pessoaDto) {
    Pessoa pessoaEntidade = pessoaMapper.toEntity(pessoaDto);
    Pessoa entidadeSalva = repositorioPessoa.save(pessoaEntidade);

    return pessoaMapper.toDto(entidadeSalva);
  }

  public PessoaDto listarUmaPessoaPorId(Long id) {
    Pessoa pessoa = repositorioPessoa.findById(id)
        .orElseThrow(() -> new PessoaNaoEncontradaExcecao("Pessoa não encontrada com id: " + id));

    return pessoaMapper.toDto(pessoa);
  }

  public List<PessoaDto> listarPessoas() {
    return repositorioPessoa.findAll().stream()
        .map(pessoaMapper::toDto)
        .collect(Collectors.toList());
  }

  public PessoaDto atualizarPessoa(Long id, PessoaDto pessoaDto) {
    Pessoa entidadePessoa = repositorioPessoa.findById(id)
        .orElseThrow(() -> new PessoaNaoEncontradaExcecao("Pessoa não encontrada para atualização com id: " + id));

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
    Pessoa pessoa = repositorioPessoa.findById(id)
        .orElseThrow(() -> new PessoaNaoEncontradaExcecao("Pessoa não encontrada para deleção com id: " + id));

    repositorioPessoa.delete(pessoa);
  }

  public int calculaIdadePessoa(Long id) {
    Pessoa pessoa = repositorioPessoa.findById(id)
        .orElseThrow(() -> new PessoaNaoEncontradaExcecao("Pessoa não encontrada com id: " + id));

    LocalDate hoje = LocalDate.now();
    Period idade = Period.between(pessoa.getDataNascimento(), hoje);

    return idade.getYears();

  }
}
