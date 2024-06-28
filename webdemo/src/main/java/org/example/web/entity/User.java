package org.example.web.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    private Long id;

    private String username;

    private String password;

    private Integer authority;
    public User() {}

    public User(Long id, String username, String password, Integer authority) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.authority = authority;
    }
}
