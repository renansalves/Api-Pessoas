package br.tec.db.Pessoa.excecao;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PessoaNaoEncontradaExcecao extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PessoaNaoEncontradaExcecao(String message) {
    super(message);
  }

  public PessoaNaoEncontradaExcecao(String message, Throwable cause) {
    super(message, cause);
  }
}
