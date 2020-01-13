package ru.jpa.utils.specification.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

  public User(Integer number, String text) {
    this.text = text;
    this.number = number;
  }

  @Id
  @GeneratedValue
  private UUID id;

  private Integer number;

  private String text;

  @ManyToMany(fetch = FetchType.LAZY)
  private List<Project> projects = new ArrayList<>();

  public User addProject(Project project) {
    project.getUsers().add(this);
    this.projects.add(project);
    return this;
  }

  public User removeProject(Project project) {
    project.getUsers().remove(this);
    this.projects.remove(project);
    return this;
  }

}
