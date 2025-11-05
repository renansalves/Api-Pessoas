
package br.tec.db.Pessoa.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.tec.db.Pessoa.dto.PessoaDto;
import br.tec.db.Pessoa.map.PessoaMapperInterface;
import br.tec.db.Pessoa.model.Endereco;
import br.tec.db.Pessoa.model.Pessoa;


public class PessoaBuilder {

  private  PessoaMapperInterface pessoaMapper;
  private String nome = "Joao Pedro";
  private LocalDate dataNascimento = LocalDate.of(1994,2,1);
	private String cpf = "012.333.444-00";

  private List<Endereco> enderecos = List.of(
        new Endereco(1L, "Zero Hora", 2020, "Algarve", "Alvorada", "Rio Grande Do Sul", "94858-00")
    );
  public static PessoaBuilder umUsuario() {
      return new PessoaBuilder();
  }


  public PessoaBuilder comNome(String nome) {
      this.nome = nome;
      return this;
  }

  public PessoaBuilder comCpf(String cpf) {
      this.cpf = cpf;
      return this;
  }

  public PessoaBuilder comDataNascimento(LocalDate dataNascimento) {
      this.dataNascimento = dataNascimento;
      return this;
  }
    
  public PessoaBuilder comEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
        return this;
    }

  public PessoaBuilder comEnderecoAdicional(Endereco novoEndereco) {
        List<Endereco> novaLista = new ArrayList<>(this.enderecos);
        novaLista.add(novoEndereco);
        this.enderecos = novaLista;
        return this;
    }
  public Pessoa construir(){
    return new Pessoa(nome,dataNascimento, cpf, enderecos);
  }

  
}
