package com.example.tvmanagerment.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

        @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    public Users() {
        this.roles = new HashSet<>();
    }

}
