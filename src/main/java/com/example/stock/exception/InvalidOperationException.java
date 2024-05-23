package com.example.stock.exception;

import lombok.Getter;

public class InvalidOperationException extends RuntimeException {




  public InvalidOperationException(String message) {
    super(message);
  }

  public InvalidOperationException(String message, Throwable cause) {
    super(message, cause);
  }


}
