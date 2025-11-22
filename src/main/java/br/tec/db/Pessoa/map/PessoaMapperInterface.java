package br.tec.db.Pessoa.map;

import java.time.LocalDate;
import java.time.Period;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import br.tec.db.Pessoa.dto.PessoaRequestDto;
import br.tec.db.Pessoa.dto.PessoaResponseDto;
import br.tec.db.Pessoa.model.Endereco;
import br.tec.db.Pessoa.model.Pessoa;

@Mapper(componentModel = "spring", uses = { EnderecoMapperInterface.class })
public interface PessoaMapperInterface {

  @Named("calcularIdade")
  public static Integer calcularIdade(LocalDate dataNascimento) {
    return Period.between(dataNascimento, LocalDate.now()).getYears();
  }
  @Mapping(target= "id",ignore = true)
  Pessoa toEntity(PessoaRequestDto pessoaDto);

  PessoaRequestDto requestToDto(Pessoa pessoa);

  @Mapping(target = "idade", source = "dataNascimento", qualifiedByName = "calcularIdade")
  PessoaResponseDto responseToDto(Pessoa pessoa);

  @AfterMapping
  default void setPessoaNosEnderecos(@MappingTarget Pessoa pessoa){

    if (pessoa.getEnderecos() != null){
      for (Endereco endereco : pessoa.getEnderecos()){
        endereco.setPessoa(pessoa);
      }
    }
  }
}
