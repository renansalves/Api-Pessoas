package br.tec.db.Pessoa.controlador;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // Importado
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Adicionado para Atualização
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.tec.db.Pessoa.Servico.ServicoPessoa;
import br.tec.db.Pessoa.dto.PessoaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ControladorPessoa
 */
@RestController
@RequestMapping("pessoa")
@Tag(name = "Pessoa", description = "Operações relacionadas ao cadastro de Pessoas") // Documenta o Controller
public class ControladorPessoa {

  @Autowired
  private ServicoPessoa servicoPessoa;

  @Operation(summary = "Cria uma nova Pessoa", description = "Salva uma nova Pessoa, incluindo seus Endereços.", responses = {
      @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
  })
  @PostMapping("/")
  public ResponseEntity<PessoaDto> salvarPessoa(@RequestBody PessoaDto pessoaDto) {

    PessoaDto pessoaSalva = servicoPessoa.salvarPessoa(pessoaDto);

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
  public ResponseEntity<List<PessoaDto>> listarPessoas() {
    List<PessoaDto> pessoas = servicoPessoa.listarPessoas();
    return ResponseEntity.ok(pessoas);
  }

  @Operation(summary = "Busca uma pessoa pelo Id", description = "Lista uma pessoa por Id cadastrado", responses = {
      @ApiResponse(responseCode = "201", description = "Pessoa encontrada"),
      @ApiResponse(responseCode = "404", description = "Pessoa não foi encontrada")
  })
  @GetMapping("{id}")
  public ResponseEntity<PessoaDto> BuscarPessoa(@PathVariable("id") Long id) {
    PessoaDto pessoaDto = servicoPessoa.listarUmaPessoaPorId(id);
    return ResponseEntity.ok(pessoaDto);
  }

  @Operation(summary = "Atualiza uma pessoa", description = "Atualiza uma pessoa que existe na base de dados", responses = {
      @ApiResponse(responseCode = "200", description = "Pessoa encotrada na base"),
      @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
  })
  @PutMapping("{id}")
  public ResponseEntity<PessoaDto> atualizarPessoa(@PathVariable("id") Long id, @RequestBody PessoaDto pessoaDto) {
    PessoaDto pessoaAtualizada = servicoPessoa.atualizarPessoa(id, pessoaDto);

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
