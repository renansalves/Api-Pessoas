package br.tec.db.Pessoa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String rua;
	int numero;
	String bairro;
	String cidade;
	String estado;
	String cep;
  boolean isPrincipal;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pessoa_id", nullable = false)
  private Pessoa pessoa;

}
