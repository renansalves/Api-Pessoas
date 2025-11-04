package br.tec.db.Pessoa.map; 

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.tec.db.Pessoa.dto.EnderecoDto;
import br.tec.db.Pessoa.model.Endereco;

@Mapper(componentModel = "spring")
public interface EnderecoMapperInterface {


  EnderecoDto toDto(Endereco endereco);

  @Mapping(target = "id", ignore = true)
  Endereco toEntity(EnderecoDto enderecoDto);
}
