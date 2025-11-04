package br.tec.db.Pessoa.map;

import java.time.LocalDate;
import java.time.Period;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.tec.db.Pessoa.dto.PessoaDto;
import br.tec.db.Pessoa.modelo.Pessoa;

@Mapper(componentModel = "spring", uses = { EnderecoMapperInterface.class })
public interface PessoaMapperInterface {


  @Mapping(target = "idade", source = "dataNascimento", qualifiedByName = "calcularIdade")
  PessoaDto toDto(Pessoa pessoa);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "idade", ignore = true) 
  Pessoa toEntity(PessoaDto pessoaDto);

  @Named("calcularIdade")
  public static Integer calcularIdade(LocalDate dataNascimento) {
    return Period.between(dataNascimento, LocalDate.now()).getYears();
  }
}
