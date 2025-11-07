
package br.tec.db.Pessoa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	String rua;
	int numero;
	String bairro;
	String cidade;
	String estado;
	String cep;
	public Endereco(Long id, String rua, int numero, String bairro, String cidade, String estado, String cep) {
    this.id = id;
		this.rua = rua;
		this.numero = numero;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
    this.cep = cep;
	}

}
