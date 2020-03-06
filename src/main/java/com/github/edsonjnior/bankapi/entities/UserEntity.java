package com.github.edsonjnior.bankapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    public UserEntity() {
    }

    public UserEntity(String name, String email, String password, Set<RoleEntity> roles){
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
