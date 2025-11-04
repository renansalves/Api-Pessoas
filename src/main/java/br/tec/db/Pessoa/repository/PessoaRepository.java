package br.tec.db.Pessoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.tec.db.Pessoa.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
