package ru.jpa.utils.specification.test.predicate;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.jpa.utils.specification.Fixtures.USER_COUNT;
import static ru.jpa.utils.specification.Fixtures.projects;
import static ru.jpa.utils.specification.Fixtures.relations;
import static ru.jpa.utils.specification.Fixtures.tasks;
import static ru.jpa.utils.specification.Fixtures.users;
import static ru.jpa.utils.specification.ShortQuery.PREDICATE;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
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
import ru.jpa.utils.specification.entity.Task_;
import ru.jpa.utils.specification.entity.User;
import ru.jpa.utils.specification.entity.User.Type;
import ru.jpa.utils.specification.entity.User_;
import ru.jpa.utils.specification.repository.ProjectRepository;
import ru.jpa.utils.specification.repository.TaskRepository;
import ru.jpa.utils.specification.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InTest {

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
  public void inNumberTest() {
    // given
    Set<Integer> numbers = users.stream().limit(USER_COUNT / 2)
        .map(User::getNumber)
        .collect(toSet());
    Set<User> expected = users.stream()
        .filter(user -> numbers.contains(user.getNumber()))
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.in(User_.number, numbers));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void inTextTest() {
    // given
    Set<String> texts = users.stream().limit(USER_COUNT / 2).map(User::getText).collect(toSet());
    Set<User> expected = users.stream()
        .filter(user -> texts.contains(user.getText()))
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.in(User_.text, texts));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void inTimeTest() {
    // given
    Set<LocalTime> times = users.stream().limit(USER_COUNT / 2)
        .map(user -> user.getTime())
        .collect(toSet());

    Set<User> expected = users.stream()
        .filter(user -> times.contains(user.getTime()))
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.in(User_.time, times));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void inTypeTest() {
    // given
    Set<Type> types = users.stream().limit(USER_COUNT / 2).map(User::getType).collect(toSet());
    Set<User> expected = users.stream()
        .filter(user -> types.contains(user.getType()))
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.in(User_.type, types));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void inWithJoinTest() {
    // given
    Set<Integer> projectsNumbers = users.stream().limit(USER_COUNT / 3)
        .map(User::getProjects).flatMap(Collection::stream)
        .map(Project::getNumber).collect(toSet());

    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream()
            .map(Project::getNumber)
            .anyMatch(projectsNumbers::contains)
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.in(User_.projects, Project_.number, projectsNumbers));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void inWithJoinJoinTest() {
    // given
    Set<String> tasksTexts = users.stream().limit(USER_COUNT / 4)
        .map(User::getProjects).flatMap(Collection::stream)
        .map(Project::getTasks).flatMap(Collection::stream)
        .map(Task::getText).collect(toSet());

    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream()
            .anyMatch(project -> project.getTasks().stream()
                .map(Task::getText)
                .anyMatch(tasksTexts::contains)
            )
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.in(User_.projects, Project_.tasks, Task_.text, tasksTexts));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void inNullTest() {
    // given
    User user = users.get(USER_COUNT / 5);
    user.setText(null);
    userRepository.save(user);

    List<User> result0 = userRepository
        .findAll(PREDICATE.in(User_.text, Arrays.asList(new String[]{null})));

    List<User> result1 = userRepository
        .findAll(PREDICATE.in(User_.text, null));

    // then
    assertTrue(result0.isEmpty());
    assertTrue(result1.isEmpty());
  }

}
