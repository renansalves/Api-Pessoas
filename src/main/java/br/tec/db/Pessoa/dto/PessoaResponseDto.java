
package br.tec.db.Pessoa.dto;

import java.time.LocalDate;
import java.util.List;

public record PessoaResponseDto(
    Long id,
    String nome,
    String cpf,
    int idade,
    LocalDate dataNascimento,
    List<EnderecoDto> enderecos
    ) {
}
