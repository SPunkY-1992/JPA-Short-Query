package ru.jpa.utils.specification.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Project {

  public Project(Integer number, String text) {
    this.text = text;
    this.number = number;
  }

  @Id
  @GeneratedValue
  private UUID id;

  private Integer number;

  private String text;

  @ManyToMany(fetch = FetchType.LAZY)
  private List<User> users = new ArrayList<>();

  @OneToMany(mappedBy = Task_.PROJECT)
  private List<Task> tasks = new ArrayList<>();

  public Project addUser(User user) {
    user.getProjects().add(this);
    this.users.add(user);
    return this;
  }

  public Project removeUser(User user) {
    user.getProjects().remove(this);
    this.users.remove(user);
    return this;
  }

  public Project addTask(Task task) {
    task.setProject(this);
    this.tasks.add(task);
    return this;
  }

}
