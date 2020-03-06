package com.github.edsonjnior.bankapi.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    public UserEntity() {
    }

    public UserEntity(String name, String email, String password, Set<RoleEntity> roles){
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
