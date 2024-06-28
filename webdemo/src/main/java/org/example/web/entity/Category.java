package org.example.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryid;
    private Long parentid;

    private String name;

    public Category() {
        this.categoryid = -1L;
        this.parentid = -1L;
        this.name = new String();
    }

    public Category(Long categoryid, Long parentid, String name) {
        this.categoryid = categoryid;
        this.parentid = parentid;
        this.name = name;
    }
}
