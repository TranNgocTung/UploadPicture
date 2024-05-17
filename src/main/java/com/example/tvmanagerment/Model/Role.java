package com.example.tvmanagerment.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
