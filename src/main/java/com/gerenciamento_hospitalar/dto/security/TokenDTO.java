package com.gerenciamento_hospitalar.dto.security;

import java.util.Date;

public record TokenDTO(String username,
                       Boolean authenticated,
                       Date created,
                       Date expiration,
                       String acessToken,
                       String refreshToken) {
}
