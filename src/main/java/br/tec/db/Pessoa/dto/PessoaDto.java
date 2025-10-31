package br.tec.db.Pessoa.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record PessoaDto(

  @Schema(accessMode = Schema.AccessMode.READ_ONLY) Long id,
  String nome,
  String cpf,
  LocalDate dataNascimento,
  List<EnderecoDto> enderecos,
  @Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer idade) {

}
