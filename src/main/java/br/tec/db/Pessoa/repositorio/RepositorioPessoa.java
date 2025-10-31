package br.tec.db.Pessoa.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import br.tec.db.Pessoa.modelo.Pessoa;

/**
 * RepositorioPessoa
 */
public interface RepositorioPessoa extends JpaRepository<Pessoa, Long> {

}
