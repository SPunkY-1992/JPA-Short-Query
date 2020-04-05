package ru.jpa.utils.specification.predicates;

import static ru.jpa.utils.specification.ShortQuery.distinct;
import static ru.jpa.utils.specification.join.Joiner.join;
import static ru.jpa.utils.specification.predicates.TypeConverter.dateTimeWithMicroseconds;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface IsEquals {

  private static <A> Predicate isEquals(Path<A> path, A value, CriteriaBuilder cb) {
    if (value == null) {
      return cb.isNull(path);
    }
    if (value instanceof LocalDateTime) {
      return cb.equal(
          cb.function("TO_TIMESTAMP", String.class, path, cb.literal("YYYY-MM-DD HH24:MI:SS.FF 6")),
          dateTimeWithMicroseconds((LocalDateTime) value)
      );
    }
    if (value instanceof ZonedDateTime) {
      return cb.equal(
          cb.function("TO_TIMESTAMP", Timestamp.class, path, cb.literal("YYYY-MM-DD HH24:MI:SS.FF 6")),
          dateTimeWithMicroseconds((ZonedDateTime) value)
      );
    }
    return cb.equal(path, value);
  }

  /**
   * example of usage:
   * isEquals(User_.text, "some text")
   * not(isEquals(User_.number, null))
   */
  default <S extends T, T, A>
  Specification<S> isEquals(SingularAttribute<T, A> attribute, A value) {
    return (root, cq, cb) -> isEquals(root.get(attribute), value, cb);
  }

  /**
   * example of usage: isEquals(User_.projects, Project_.id, projectId)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A>
  Specification<S> isEquals(J1 join1, SingularAttribute<TJ1, A> attribute, A value) {
    return distinct((root, cq, cb) -> isEquals(join(root, join1).get(attribute), value, cb));
  }

  /**
   * example of usage: isEquals(User_.projects, Project_.tasks, Task_.id, taskId)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A>
  Specification<S> isEquals(J1 join1, J2 join2, SingularAttribute<TJ2, A> attribute, A value) {
    return distinct((root, cq, cb) -> isEquals(join(root, join1, join2).get(attribute), value, cb));
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A>
  Specification<S> isEquals(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> attribute, A value) {
    return distinct((root, cq, cb) -> isEquals(join(root, j1, j2, j3).get(attribute), value, cb));
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>,
      TJ4, J4 extends Bindable<TJ4> & Attribute<TJ3, ?>, A>
  Specification<S> isEquals(J1 j1, J2 j2, J3 j3, J4 j4, SingularAttribute<TJ4, A> a, A value) {
    return distinct((root, cq, cb) -> isEquals(join(root, j1, j2, j3, j4).get(a), value, cb));
  }

}
