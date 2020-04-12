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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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
public class IsEqualsTest {

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
  public void isEqualsNumberTest() {
    // given
    int number = users.get(USER_COUNT / 2).getNumber();
    Set<User> expected = users.stream().filter(user -> user.getNumber() == number).collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.isEquals(User_.number, number));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void isEqualsTextTest() {
    // given
    String text = users.get(USER_COUNT / 2).getText();
    Set<User> expected = users.stream()
        .filter(user -> user.getText().equals(text))
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.isEquals(User_.text, text));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void isEqualsTypeTest() {
    // given
    Type type = users.get(USER_COUNT / 2).getType();
    Set<User> expected = users.stream().filter(user -> user.getType() == type).collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.isEquals(User_.type, type));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void isEqualsTimeTest() {
    // given
    LocalTime time = users.get(USER_COUNT / 2).getTime();
    Set<User> expected = users.stream()
        .filter(user -> user.getTime().equals(time))
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.isEquals(User_.time, time));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void isEqualsDateTest() {
    // given
    LocalDate date = users.get(USER_COUNT / 2).getDate();
    Set<User> expected = users.stream()
        .filter(user -> user.getDate().equals(date))
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.isEquals(User_.date, date));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void isEqualsLocalDateTimeTest() {
    // given
    LocalDateTime time = users.get(USER_COUNT / 2).getLocalDateTime();
    Set<User> expected = users.stream()
        .filter(user -> user.getLocalDateTime().equals(time))
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.isEquals(User_.localDateTime, time));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void isEqualsZonedDateTimeTest() {
    // given
    ZonedDateTime time = users.get(USER_COUNT / 2).getZonedDateTime();
    Set<User> expected = users.stream()
        .filter(user -> user.getZonedDateTime().toInstant().equals(time.toInstant()))
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.isEquals(User_.zonedDateTime, time));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void isEqualsWithJoinTest() {
    // given
    Project project = users.get(USER_COUNT / 3).getProjects().iterator().next();
    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream()
            .anyMatch(p -> p.getNumber().equals(project.getNumber()))
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.isEquals(User_.projects, Project_.number, project.getNumber()));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void isEqualsWithJoinJoinTest() {
    // given
    Task task = users.get(USER_COUNT / 4)
        .getProjects().iterator().next().getTasks().iterator().next();

    Set<User> expected = users.stream()
        .filter(user -> user.getProjects().stream()
            .anyMatch(project -> project.getTasks().stream()
                .anyMatch(t -> t.getText().equals(task.getText()))
            )
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.isEquals(User_.projects, Project_.tasks, Task_.text, task.getText()));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void isEqualsNullTest() {
    // given
    Set<User> expected = users.stream().limit(USER_COUNT / 5)
        .peek(user -> user.setText(null))
        .map(userRepository::save)
        .collect(toSet());

    // when
    List<User> result = userRepository.findAll(PREDICATE.isEquals(User_.text, null));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

}
