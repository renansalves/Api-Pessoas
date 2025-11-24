package br.tec.db.Pessoa.handler;

public class PessoaNaoEncontradaException extends RuntimeException {

      public PessoaNaoEncontradaException(String message) {
        super(message); 
    }

}
