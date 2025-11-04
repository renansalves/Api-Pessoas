package br.tec.db.Pessoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.tec.db.Pessoa.model.Endereco;

public interface RepositorioEndereco extends JpaRepository<Endereco, Long> {

}
