package org.udemy.javafxudemy.model.exceptions;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    private Map<String, String> errors = new HashMap<>();

    public ValidationException(String msg){
        super(msg);
    }

    public void addError(String fieldName, String errorMessage){
        errors.put(fieldName, errorMessage);
    }

    public Map<String, String> getErros() {
        return errors;
    }

    public void setErros(Map<String, String> erros) {
        this.errors = erros;
    }


}
