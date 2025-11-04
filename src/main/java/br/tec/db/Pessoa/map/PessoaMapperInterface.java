package br.tec.db.Pessoa.map;

import java.time.LocalDate;
import java.time.Period;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.tec.db.Pessoa.dto.PessoaDto;
import br.tec.db.Pessoa.model.Pessoa;

@Mapper(componentModel = "spring", uses = { EnderecoMapperInterface.class })
public interface PessoaMapperInterface {


  @Named("calcularIdade")
  public static Integer calcularIdade(LocalDate dataNascimento) {
    return Period.between(dataNascimento, LocalDate.now()).getYears();
  }

  @Mapping(target = "idade", source = "dataNascimento", qualifiedByName = "calcularIdade")
  PessoaDto toDto(Pessoa pessoa);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "idade", ignore = true) 
  br.tec.db.Pessoa.model.Pessoa toEntity(PessoaDto pessoaDto);
}
