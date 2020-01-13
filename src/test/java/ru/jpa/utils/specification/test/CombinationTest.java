package ru.jpa.utils.specification.test;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.jpa.utils.specification.Fixtures.PROJECT_COUNT;
import static ru.jpa.utils.specification.Fixtures.SAME_TEXT;
import static ru.jpa.utils.specification.Fixtures.USER_COUNT;
import static ru.jpa.utils.specification.Fixtures.projects;
import static ru.jpa.utils.specification.Fixtures.relations;
import static ru.jpa.utils.specification.Fixtures.tasks;
import static ru.jpa.utils.specification.Fixtures.users;
import static ru.jpa.utils.specification.SpecificationUtils.PREDICATE;

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
import ru.jpa.utils.specification.entity.User;
import ru.jpa.utils.specification.entity.User_;
import ru.jpa.utils.specification.repository.ProjectRepository;
import ru.jpa.utils.specification.repository.TaskRepository;
import ru.jpa.utils.specification.repository.UserRepository;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CombinationTest {

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
  public void likeAndBetweenTest() {
    // given
    int lower = PROJECT_COUNT / 4;
    int upper = PROJECT_COUNT - PROJECT_COUNT / 4;

    Set<User> expected = users.stream()
        .filter(user -> user.getText().contains(SAME_TEXT)
            && user.getProjects().stream().anyMatch(project -> project.getNumber() >= lower)
            && user.getProjects().stream().anyMatch(project -> project.getNumber() <= upper)
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.like(User_.text, SAME_TEXT)
            .and(PREDICATE.between(User_.projects, Project_.number, lower, upper))
        );

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

  @Test
  public void inOrGreaterThenTest() {
    // given
    int bound = PROJECT_COUNT - PROJECT_COUNT / 3;

    Set<String> projectTexts = users.stream().limit(USER_COUNT / 4)
        .map(User::getProjects).flatMap(Collection::stream).map(Project::getText).collect(toSet());

    Set<User> expected = users.stream()
        .filter(user -> user.getNumber() > bound
            || user.getProjects().stream().anyMatch(p -> projectTexts.contains(p.getText()))
        )
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(PREDICATE.greaterThan(User_.number, bound)
            .or(PREDICATE.in(User_.projects, Project_.text, projectTexts))
        );

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

}
