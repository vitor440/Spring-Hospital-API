package com.gerenciamento_hospitalar.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "username")
    private String username;

    @Column(name = "senha")
    private String senha;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired;

    @Column(name = "enabled")
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
    joinColumns = {@JoinColumn(name = "usuario_id")},
    inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> permissions;



    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        for (Role permission : this.permissions) {
            roles.add(permission.getRole());
        }

        return roles;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.permissions;
//    }
//
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions.stream()
                .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.getRole()))
                .toList();
    }


    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
