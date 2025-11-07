package br.tec.db.Pessoa.builder;

import java.time.LocalDate;
import java.util.List;

import br.tec.db.Pessoa.dto.EnderecoDto;
import br.tec.db.Pessoa.dto.PessoaRequestDto;
import br.tec.db.Pessoa.dto.PessoaResponseDto;
import br.tec.db.Pessoa.model.Endereco;
import br.tec.db.Pessoa.model.Pessoa;

public class PessoaBuilder {

  private final Long id = 1L;
  private final String nome = "Joao Pedro";
  private final LocalDate dataNascimento = LocalDate.of(1994,2,1);
	private final String cpf = "012.333.444-00";
  private final int idade = 31;

  private final List<Endereco> enderecos = List.of(
        new Endereco(1L, "Zero Hora", 2020, "Algarve", "Alvorada", "Rio Grande Do Sul", "94858-000"),
        new Endereco(2L, "Getuli vargas", 1000, "Bela Vista", "Alvorada", "Rio Grande Do Sul", "94820-000"),
        new Endereco(3L, "PP5", 20, "Nova Americana", "Alvorada", "Rio Grande Do Sul", "94820-594")
    );
  private final List<EnderecoDto> enderecosDto = List.of(
        new EnderecoDto("Zero Hora", 2020, "Algarve", "Alvorada", "Rio Grande Do Sul", "94858-000"),
        new EnderecoDto("Getuli vargas", 1000, "Bela Vista", "Alvorada", "Rio Grande Do Sul", "94820-000"),
        new EnderecoDto("PP5", 20, "Nova Americana", "Alvorada", "Rio Grande Do Sul", "94820-594")
    );
  public static PessoaBuilder umUsuario() {
      return new PessoaBuilder();
  }

  public  Pessoa criarPessoa() {
        Pessoa pessoa = new Pessoa(
          this.nome,
          this.dataNascimento,
          this.cpf,
          this.enderecos
          );
      return pessoa;
  }

  public PessoaRequestDto criarPessoaRequestDto() {
    PessoaRequestDto dto = new PessoaRequestDto(
        this.nome,
        this.cpf,
        this.dataNascimento,
        this.enderecosDto
    ); 
        return dto;
    }

  public PessoaResponseDto criarPessoaResponseDto() {
      PessoaResponseDto dto = new PessoaResponseDto(
      this.id,
      this.nome,
      this.cpf,
      this.idade,
      this.dataNascimento,
      this.enderecosDto
      );
      return dto;
    
  }
}
