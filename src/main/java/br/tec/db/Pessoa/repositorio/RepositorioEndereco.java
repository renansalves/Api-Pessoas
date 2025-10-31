package br.tec.db.Pessoa.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import br.tec.db.Pessoa.modelo.Endereco;

/**
 * RepositorioEndereco
 */
public interface RepositorioEndereco extends JpaRepository<Endereco, Long> {

}
