package com.springboot.MessApplication.MessMate.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.MessApplication.MessMate.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;


    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private Subscription subscription;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
