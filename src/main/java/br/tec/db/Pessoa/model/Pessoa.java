
package br.tec.db.Pessoa.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private int idade;

	private String nome;

	private LocalDate dataNascimento;

	@Column(nullable = false, unique = true)
	private String cpf;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "pessoa_id")
	private List<Endereco> enderecos = new ArrayList<Endereco>();

	public Pessoa(String nome, LocalDate dataNascimento, String cpf, List<Endereco> enderecos ) {
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.cpf = cpf;
		this.enderecos = enderecos;
	}

  

}
