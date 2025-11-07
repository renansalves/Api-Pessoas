package br.tec.db.Pessoa.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.tec.db.Pessoa.service.PessoaService;
import br.tec.db.Pessoa.dto.PessoaRequestDto;
import br.tec.db.Pessoa.dto.PessoaResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("pessoa")
@Tag(name = "Pessoa", description = "Operações relacionadas ao cadastro de Pessoas")
public class PessoaController {

  @Autowired
  private PessoaService servicoPessoa;

  @Operation(summary = "Cria uma nova Pessoa", description = "Salva uma nova Pessoa, incluindo seus Endereços.", responses = {
      @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
  })
  @PostMapping("/")
  public ResponseEntity<PessoaResponseDto> salvarPessoa(@RequestBody PessoaRequestDto pessoaDto) {

    PessoaResponseDto pessoaSalva = servicoPessoa.salvarPessoa(pessoaDto);

    URI recurso = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("{id}")
        .buildAndExpand(pessoaSalva.id())
        .toUri();

    return ResponseEntity.created(recurso).body(pessoaSalva);
  }

  @Operation(summary = "Lista todas as pessoas", description = "Lista todas as pessoas cadastradas", responses = {
      @ApiResponse(responseCode = "200", description = "Retorna a lista de pessoas que estão cadastradas na base"),
  })
  @GetMapping("/")
  public ResponseEntity<List<PessoaResponseDto>> listarPessoas() {
    List<PessoaResponseDto> pessoas = servicoPessoa.listarPessoas();
    return ResponseEntity.ok(pessoas);
  }

  @Operation(summary = "Busca uma pessoa pelo Id", description = "Lista uma pessoa por Id cadastrado", responses = {
      @ApiResponse(responseCode = "201", description = "Pessoa encontrada"),
      @ApiResponse(responseCode = "404", description = "Pessoa não foi encontrada")
  })
  @GetMapping("{id}")
  public ResponseEntity<PessoaResponseDto> BuscarPessoa(@PathVariable("id") Long id) {
    PessoaResponseDto pessoaDto = servicoPessoa.listarUmaPessoaPorId(id);
    return ResponseEntity.ok(pessoaDto);
  }

  @Operation(summary = "Atualiza uma pessoa", description = "Atualiza uma pessoa que existe na base de dados", responses = {
      @ApiResponse(responseCode = "200", description = "Pessoa encotrada na base"),
      @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
  })
  @PutMapping("{id}")
  public ResponseEntity<PessoaResponseDto> atualizarPessoa(@PathVariable("id") Long id, @RequestBody PessoaRequestDto pessoaDto) {
    PessoaResponseDto pessoaAtualizada = servicoPessoa.atualizarPessoa(id, pessoaDto);

    return ResponseEntity.ok(pessoaAtualizada);
  }

  @Operation(summary = "Remove uma pessoa da base", description = "Remove a pessoa da base de dados pelo id informado", responses = {
      @ApiResponse(responseCode = "201", description = "Pessoa excluida com sucesso"),
      @ApiResponse(responseCode = "404", description = "Pessoa não encontrada para exclusão")
  })
  @DeleteMapping("{id}")
  public ResponseEntity<Void> deletarPessoa(@PathVariable("id") Long id) {
    servicoPessoa.deletarPessoa(id);

    return ResponseEntity.noContent().build();
  }

}
