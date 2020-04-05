package ru.jpa.utils.specification;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ru.jpa.utils.specification.entity.Project;
import ru.jpa.utils.specification.entity.Task;
import ru.jpa.utils.specification.entity.User;
import ru.jpa.utils.specification.entity.User.Type;

public class Fixtures {

  public static final int USER_COUNT = 111;
  public static final int USER_PROJECT_COUNT = 7;
  public static final int PROJECT_COUNT = USER_COUNT + (USER_PROJECT_COUNT - 1);
  public static final int PROJECT_TASK_COUNT = 4;
  public static final int TASK_COUNT = PROJECT_COUNT * PROJECT_TASK_COUNT;
  public static final String SAME_TEXT = "SAME TEXT";

  /**
   * user >-< project --< task
   *
   * USER_COUNT = 5
   * USER_PROJECT_COUNT = 4
   * PROJECT_TASK_COUNT = 3
   *
   * user >-- user_project --< project --< task
   *    0      0 : 0 1 2 3           0  :  0 1 2
   *    1      1 : 1 2 3 4           1  :  3 4 5
   *    2      2 : 2 3 4 5           2  :  6 7 8
   *    3      3 : 3 4 5 6           3  :  9 10 11
   *    4      4 : 4 5 6 7           4  :  12 13 14
   *  ...    ... : ...               5  :  15 16 17
   *                                 6  :  18 19 20
   *                                 7  :  21 22 23
   *                                ... : ...
   */
  public static List<User> relations(List<User> users, List<Project> projects, List<Task> tasks) {

    // users with projects
    for (int u = 0; u < USER_COUNT; u++) {
      System.out.print(u + " :");
      for (int u_p = 0; u_p < USER_PROJECT_COUNT; u_p++) {
        users.get(u).addProject(projects.get(u + u_p));
        System.out.print(" " + (u + u_p));
      }
      System.out.println();
    }
    System.out.println();
    // projects with tasks
    for (int p = 0; p < PROJECT_COUNT; p++) {
      System.out.print(p + " :");
      for (int p_t = 0; p_t < PROJECT_TASK_COUNT; p_t++) {
        projects.get(p).addTask(tasks.get(p * PROJECT_TASK_COUNT + p_t));
        System.out.print(" " + (p * PROJECT_TASK_COUNT + p_t));
      }
      System.out.println();
    }

    return users;
  }

  public static List<User> users() {
    List<User> users = new ArrayList<>(USER_COUNT);
    ZonedDateTime zonedDateTime = ZonedDateTime.now();

    for (int u = 0; u < USER_COUNT; u++) {
      User user = new User();
      user.setNumber(u);
      user.setText("user " + (u % 2 == 0 ? u : SAME_TEXT));
      user.setType(Type.values()[u % Type.values().length]);

      zonedDateTime = zonedDateTime
          .plusDays(u % 5 == 0 ? 1 : 0)
          .withZoneSameInstant(u % 4 == 0 ? ZoneIdLoop.get() : ZoneIdLoop.next())
          .plusSeconds(u % 3 == 0 ? 1 : 0)
          .plusNanos((u % 2) * 10000);

      user.setTime(zonedDateTime.toLocalTime());
      user.setDate(zonedDateTime.toLocalDate());
      user.setLocalDateTime(zonedDateTime.toLocalDateTime());
      user.setZonedDateTime(zonedDateTime);

      users.add(user);
    }
    return users;
  }

  public static List<Project> projects() {
    List<Project> projects = new ArrayList<>(PROJECT_COUNT);
    for (int p = 0; p < PROJECT_COUNT; p++) {
      projects.add(new Project(p, "project" + (p % 2 == 0 ? p : SAME_TEXT)));
    }
    return projects;
  }

  public static List<Task> tasks() {
    List<Task> tasks = new ArrayList<>(TASK_COUNT);
    for (int t = 0; t < TASK_COUNT; t++) {
      tasks.add(new Task(t, SAME_TEXT + "task" + (t % 2 == 0 ? t : SAME_TEXT)));
    }
    return tasks;
  }

  static class ZoneIdLoop {

    private static Iterator<String> timeZoneIdIterator = ZoneId.getAvailableZoneIds().iterator();
    private static ZoneId zoneId = next();

    static ZoneId get() {
      return zoneId;
    }

    static ZoneId next() {
      if (!timeZoneIdIterator.hasNext()) {
        timeZoneIdIterator = ZoneId.getAvailableZoneIds().iterator();
      }
      zoneId = ZoneId.of(timeZoneIdIterator.next());
      return zoneId;
    }

  }

}
