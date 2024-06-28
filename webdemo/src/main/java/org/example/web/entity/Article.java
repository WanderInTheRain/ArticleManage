package org.example.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long authorid;

    private String title;
    private String content;

    private String key;

    private Long categoryid;

    private Long share;
    private Long stars;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    // getters and setters
}
