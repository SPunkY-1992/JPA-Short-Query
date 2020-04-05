package ru.jpa.utils.specification.test;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.jpa.utils.specification.Fixtures.SAME_TEXT;
import static ru.jpa.utils.specification.Fixtures.USER_COUNT;
import static ru.jpa.utils.specification.Fixtures.projects;
import static ru.jpa.utils.specification.Fixtures.relations;
import static ru.jpa.utils.specification.Fixtures.tasks;
import static ru.jpa.utils.specification.Fixtures.users;
import static ru.jpa.utils.specification.ShortQuery.any;
import static ru.jpa.utils.specification.ShortQuery.neither;
import static ru.jpa.utils.specification.ShortQuery.orderBy;
import static ru.jpa.utils.specification.ShortQuery.search;

import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;
import ru.jpa.utils.specification.entity.User;
import ru.jpa.utils.specification.entity.User_;
import ru.jpa.utils.specification.repository.ProjectRepository;
import ru.jpa.utils.specification.repository.TaskRepository;
import ru.jpa.utils.specification.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UtilsTest {

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
  public void anyTest() {
    // when
    List<User> result = userRepository.findAll(any());

    // then
    Assert.assertEquals(USER_COUNT, result.size());
    assertTrue(result.containsAll(users));
  }

  @Test
  public void neitherTest() {
    // when
    List<User> result = userRepository.findAll(neither());

    // then
    assertTrue(result.isEmpty());
  }

  @Test
  public void orderByTest() {
    // when
    List<User> asc = userRepository.findAll(orderBy(User_.number, Direction.ASC));
    List<User> desc = userRepository.findAll(orderBy(User_.number, Direction.DESC));

    // then
    for (int i = 1; i < USER_COUNT; i++) {
      assertTrue(asc.get(i - 1).getNumber() <= asc.get(i).getNumber());
      assertTrue(desc.get(i - 1).getNumber() >= desc.get(i).getNumber());
    }
  }

  @Test
  public void searchTest() {
    // given
    Set<User> expected = users.stream()
        .filter(user -> user.getText().contains(SAME_TEXT) && user.getId().toString().contains("7"))
        .collect(toSet());

    // when
    List<User> result = userRepository
        .findAll(search(Set.of(User_.text, User_.id), Set.of(SAME_TEXT, "7")));

    // then
    assertEquals(expected.size(), result.size());
    assertTrue(result.containsAll(expected));
  }

}
