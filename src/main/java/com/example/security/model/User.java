package com.example.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

@Table(name="users")
@Entity
@Data
public class User {
    @Id
    Long id;

    String username;
    String password;
    String role;
}
