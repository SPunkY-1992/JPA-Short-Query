package ru.jpa.utils.specification.test.predicate;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.jpa.utils.specification.Fixtures.PROJECT_COUNT;
import static ru.jpa.utils.specification.Fixtures.TASK_COUNT;
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
import ru.jpa.utils.specification.entity.Project_;
import ru.jpa.utils.specification.entity.Task_;
import ru.jpa.utils.specification.entity.User;
import ru.jpa.utils.specification.entity.User_;
import ru.jpa.utils.specification.repository.ProjectRepository;
import ru.jpa.utils.specification.repository.TaskRepository;
import ru.jpa.utils.specification.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LessThanOrEqualToTest {

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
  public void lessThanOrEqualToTest() {
    // given
    int bound = USER_COUNT / 2;
    Set<User> expected = users.stream().filter(user -> user.getNumber() <= bound).collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.lessThanOrEqualTo(User_.number, bound));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void lessThanOrEqualToWithJoinTest() {
    // given
    int bound = PROJECT_COUNT / 3;
    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream()
            .anyMatch(project -> project.getNumber() <= bound)
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.lessThanOrEqualTo(User_.projects, Project_.number, bound));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void lessThanOrEqualToWithJoinJoinTest() {
    // given
    int bound = TASK_COUNT / 4;
    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream()
            .anyMatch(project -> project.getTasks().stream()
                .anyMatch(task -> task.getNumber() <= bound)
            )
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.lessThanOrEqualTo(User_.projects, Project_.tasks, Task_.number, bound));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void lessThanOrEqualToNullTest() {
    // given
    int bound = USER_COUNT / 5;
    List<User> expected = userRepository.saveAll(users.stream()
        .filter(user -> user.getNumber() <= bound)
        .peek(user -> user.setNumber(null))
        .collect(toSet())
    );

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.lessThanOrEqualTo(User_.number, null));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

}
