package br.tec.db.Pessoa.dto;

import java.time.LocalDate;
import java.util.List;

public record PessoaRequestDto(
    String nome,
    String cpf,
    LocalDate dataNascimento,
    List<EnderecoDto> enderecos
) {
}
