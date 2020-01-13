package ru.jpa.utils.specification.test.predicate;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.jpa.utils.specification.Fixtures.USER_COUNT;
import static ru.jpa.utils.specification.Fixtures.projects;
import static ru.jpa.utils.specification.Fixtures.relations;
import static ru.jpa.utils.specification.Fixtures.tasks;
import static ru.jpa.utils.specification.Fixtures.users;
import static ru.jpa.utils.specification.SpecificationUtils.PREDICATE;

import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.jpa.utils.specification.entity.Project;
import ru.jpa.utils.specification.entity.Project_;
import ru.jpa.utils.specification.entity.Task;
import ru.jpa.utils.specification.entity.User;
import ru.jpa.utils.specification.entity.User_;
import ru.jpa.utils.specification.repository.ProjectRepository;
import ru.jpa.utils.specification.repository.TaskRepository;
import ru.jpa.utils.specification.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HaveTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private TaskRepository taskRepository;

  private List<User> users;

  @Before
  public void init() {
    users = userRepository.saveAll(
        relations(
            userRepository.saveAll(users()),
            projectRepository.saveAll(projects()),
            taskRepository.saveAll(tasks())
        )
    );
  }

  @Test
  public void haveTest() {
    // given
    Project project = users.get(USER_COUNT / 2).getProjects().iterator().next();
    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream().anyMatch(p -> p.equals(project)))
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.have(User_.projects, project));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void haveWithJoinTest() {
    // given
    Task task = users.get(USER_COUNT / 3).getProjects().iterator().next().getTasks().iterator().next();
    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream()
            .anyMatch(project -> project.getTasks().stream()
                .anyMatch(t -> t.equals(task))
            )
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.have(User_.projects, Project_.tasks, task));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void haveWithJoinJoinTest() {
    // given
    Project project = users.get(USER_COUNT / 4).getProjects().iterator().next();
    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream()
            .anyMatch(p -> p.getUsers().stream()
                .anyMatch(users -> users.getProjects().stream()
                    .anyMatch(pr -> pr.equals(project))
                )
            )
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.have(User_.projects, Project_.users, User_.projects, project));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void haveNullTest() {
    // given
    User user = users.get(USER_COUNT / 5);
    user.getProjects().add(null);
    userRepository.save(user);

    // when
    List<User> result = userRepository.findAll(PREDICATE.have(User_.projects, null));

    // then
    assertTrue(result.isEmpty());
  }

}
