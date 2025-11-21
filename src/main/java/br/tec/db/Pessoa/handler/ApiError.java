package br.tec.db.Pessoa.handler;

import java.time.LocalDateTime;

public class ApiError {

  private int status;
  private String message;
  private LocalDateTime tiemstamp;



public ApiError(String message, int status, LocalDateTime tiemstamp) {
	this.message = message;
	this.status = status;
	this.tiemstamp = tiemstamp;
}

public int getStatus() {
	return status;
}

public String getMessage() {
	return message;
}

public LocalDateTime getTiemstamp() {
	return tiemstamp;
}

}

