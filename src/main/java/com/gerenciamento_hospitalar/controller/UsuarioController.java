package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UsuarioController implements com.gerenciamento_hospitalar.controller.docs.UsuarioControllerDocs {

    private final AuthenticationService authenticationService;

    @PostMapping("/singin")
    @Override
    public ResponseEntity<?> singIn(@RequestBody CadastroUsuarioDTO usuarioDTO) {
        TokenDTO authenticate = authenticationService.authenticate(usuarioDTO);

        if(authenticate == null) throw new UsernameNotFoundException("Username not found!");

        return ResponseEntity.ok(authenticate);
    }

    @PutMapping("/refresh/{username}")
    @Override
    public ResponseEntity<?> refresh(@PathVariable("username") String username,
                                     @RequestHeader("Authorization") String refreshToken) {

        TokenDTO authenticate = authenticationService.refreshToken(username, refreshToken);

        if(authenticate == null) throw new UsernameNotFoundException("Username not found!");

        return ResponseEntity.ok(authenticate);
    }

    @PostMapping("/createUser")
    @Override
    public ResponseEntity<CadastroUsuarioDTO> createUser(@RequestBody CadastroUsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(authenticationService.cadastroUsuario(usuarioDTO));
    }

}

