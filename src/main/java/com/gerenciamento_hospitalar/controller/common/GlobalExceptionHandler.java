package com.gerenciamento_hospitalar.controller.common;

import com.gerenciamento_hospitalar.dto.ErroCampo;
import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta RegistroNaoEncontradoExceptionHandle(RegistroNaoEncontradoException e) {
        return new ErroResposta(e.getMessage(), HttpStatus.NOT_FOUND.value(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta MethodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();

        List<ErroCampo> erroCampos = fieldErrors
                .stream()
                .map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErroResposta("Erro de validação", HttpStatus.UNPROCESSABLE_ENTITY.value(), erroCampos);
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta RegistroDuplicadoExceptionHandle(RegistroDuplicadoException e) {
        return new ErroResposta(e.getMessage(), HttpStatus.CONFLICT.value(), List.of());
    }

    @ExceptionHandler(ConsultasConflitantesException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta ConsultasConflitantesExceptionHandle(ConsultasConflitantesException e) {
        return new ErroResposta(e.getMessage(), HttpStatus.CONFLICT.value(), List.of());
    }

    @ExceptionHandler(HoraForaDoPadraoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta HoraForaDoPadraoExceptionHandle(HoraForaDoPadraoException e) {
        return new ErroResposta(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value(), List.of());
    }

    @ExceptionHandler(DelecaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta DelecaoNaoPermitidaExceptionHandle(DelecaoNaoPermitidaException e) {
        return new ErroResposta(e.getMessage(), HttpStatus.CONFLICT.value(), List.of());
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErroResposta InvalidJwtAuthenticationExceptionHandle(InvalidJwtAuthenticationException e) {
        return new ErroResposta(e.getMessage(), HttpStatus.FORBIDDEN.value(), List.of());
    }


}
