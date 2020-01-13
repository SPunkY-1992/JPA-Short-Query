package ru.jpa.utils.specification.entity;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {

  public Task(Integer number, String text) {
    this.number = number;
    this.text = text;
  }

  @Id
  @GeneratedValue
  private UUID id;

  private Integer number;

  private String text;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  public Task setProject(Project project) {
    project.getTasks().add(this);
    this.project = project;
    return this;
  }

}
