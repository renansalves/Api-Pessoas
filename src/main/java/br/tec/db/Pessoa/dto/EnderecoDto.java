
package br.tec.db.Pessoa.dto;


public record EnderecoDto(
  String rua,
  int numero,
  String bairro,
  String cidade,
  String estado,
  String cep
){}

