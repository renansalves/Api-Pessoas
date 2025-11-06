package br.tec.db.Pessoa.dto;

import java.time.LocalDate;
import java.util.List;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PessoaRequestDto(
    @NotBlank
    String nome,
    
	  @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF deve estar no formato xxx.xxx.xxx-xx")
    String cpf,

    @NotNull
    LocalDate dataNascimento,
    List<EnderecoDto> enderecos
) {
}
