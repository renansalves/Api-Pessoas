package br.tec.db.Pessoa.map; 

import org.mapstruct.Mapper;

import br.tec.db.Pessoa.dto.EnderecoDto;
import br.tec.db.Pessoa.modelo.Endereco;

@Mapper(componentModel = "spring")
public interface EnderecoMapperInterface {


  EnderecoDto toDto(Endereco endereco);

  Endereco toEntity(EnderecoDto enderecoDto);
}
