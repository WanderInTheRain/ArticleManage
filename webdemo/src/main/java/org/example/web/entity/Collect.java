package org.example.web.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "collect")
public class Collect {
  @Id
  private Long userid;

  private Long articleid;
}
