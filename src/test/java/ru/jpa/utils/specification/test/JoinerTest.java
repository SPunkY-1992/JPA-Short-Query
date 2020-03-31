package ru.jpa.utils.specification.test;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.jpa.utils.specification.Fixtures.PROJECT_COUNT;
import static ru.jpa.utils.specification.Fixtures.USER_COUNT;
import static ru.jpa.utils.specification.Fixtures.projects;
import static ru.jpa.utils.specification.Fixtures.relations;
import static ru.jpa.utils.specification.Fixtures.tasks;
import static ru.jpa.utils.specification.Fixtures.users;
import static ru.jpa.utils.specification.SpecificationUtils.distinct;
import static ru.jpa.utils.specification.join.Joiner.join;

import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.jpa.utils.specification.entity.Project_;
import ru.jpa.utils.specification.entity.Task;
import ru.jpa.utils.specification.entity.Task_;
import ru.jpa.utils.specification.entity.User;
import ru.jpa.utils.specification.entity.User_;
import ru.jpa.utils.specification.repository.ProjectRepository;
import ru.jpa.utils.specification.repository.TaskRepository;
import ru.jpa.utils.specification.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JoinerTest {

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

  /**
   * import static ru.jpa.utils.specification.SpecificationUtils.distinct;
   * import static ru.jpa.utils.specification.join.Joiner.join;
   */
  @Test
  public void greaterThanWithJoinTest() {
    // given
    int bound = PROJECT_COUNT / 3;
    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream()
            .anyMatch(project -> project.getNumber() > bound)
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(distinct((root, cq, cb) ->
            cb.greaterThan(join(root, User_.projects).get(Project_.number), bound)
        ));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  /**
   * import static ru.jpa.utils.specification.SpecificationUtils.distinct;
   * import static ru.jpa.utils.specification.join.Joiner.join;
   */
  @Test
  public void isEqualsWithJoinJoinTest() {
    // given
    Task task = users.get(USER_COUNT / 4).getProjects().iterator().next().getTasks().iterator().next();
    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream()
            .anyMatch(project -> project.getTasks().stream()
                .anyMatch(t -> t.getText().equals(task.getText()))
            )
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(distinct((root, cq, cb) ->
            cb.equal(join(root, User_.projects, Project_.tasks).get(Task_.text), task.getText())
        ));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

}
