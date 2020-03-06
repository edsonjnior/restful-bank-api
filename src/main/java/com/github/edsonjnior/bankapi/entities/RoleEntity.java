package com.github.edsonjnior.bankapi.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RoleEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public RoleEntity(Long id){
        this.id = id;
    }

    public RoleEntity(Long id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
