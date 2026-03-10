package com.gerenciamento_hospitalar.dto;

import java.util.List;

public record ErroResposta(String mensagem, int status, List<ErroCampo> fieldErrors) {
}
