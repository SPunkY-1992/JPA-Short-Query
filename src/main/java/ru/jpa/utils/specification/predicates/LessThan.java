package ru.jpa.utils.specification.predicates;

import static ru.jpa.utils.specification.ShortQuery.distinct;
import static ru.jpa.utils.specification.join.Joiner.join;
import static ru.jpa.utils.specification.predicates.TypeConverter.dateTimeWithMicroseconds;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface LessThan {

  private static <A extends Comparable<? super A>>
  Predicate lessThan(Path<A> path, A value, CriteriaBuilder cb) {
    if (value == null) {
      return cb.disjunction();
    }
    if (value instanceof LocalDateTime) {
      return cb.lessThan(
          cb.function("TO_TIMESTAMP", String.class, path, cb.literal("YYYY-MM-DD HH24:MI:SS.FF 6")),
          dateTimeWithMicroseconds((LocalDateTime) value)
      );
    }
    if (value instanceof ZonedDateTime) {
      return cb.lessThan(
          cb.function("TO_TIMESTAMP", String.class, path, cb.literal("YYYY-MM-DD HH24:MI:SS.FF 6")),
          dateTimeWithMicroseconds((ZonedDateTime) value)
      );
    }
    return cb.lessThan(path, value);
  }

  /**
   * example of usage: lessThan(User_.number, 666)
   */
  default <T, S extends T, A extends Comparable<? super A>>
  Specification<S> lessThan(SingularAttribute<T, A> attribute, A value) {
    return (root, cq, cb) -> lessThan(root.get(attribute), value, cb);
  }

  /**
   * example of usage: lessThan(User_.projects, Project_.number, 666)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A extends Comparable<? super A>>
  Specification<S> lessThan(J1 join1, SingularAttribute<TJ1, A> attribute, A value) {
    return distinct((root, cq, cb) -> lessThan(join(root, join1).get(attribute), value, cb));
  }

  /**
   * example of usage: lessThan(User_.projects, Project_.tasks, Task_.number, 666)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A extends Comparable<? super A>>
  Specification<S> lessThan(J1 join1, J2 join2, SingularAttribute<TJ2, A> attribute, A value) {
    return distinct((root, cq, cb) -> lessThan(join(root, join1, join2).get(attribute), value, cb));
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A extends Comparable<? super A>>
  Specification<S> lessThan(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> attribute, A value) {
    return distinct((root, cq, cb) -> lessThan(join(root, j1, j2, j3).get(attribute), value, cb));
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>,
      TJ4, J4 extends Bindable<TJ4> & Attribute<TJ3, ?>, A extends Comparable<? super A>>
  Specification<S> lessThan(J1 j1, J2 j2, J3 j3, J4 j4, SingularAttribute<TJ4, A> a, A value) {
    return distinct((root, cq, cb) -> lessThan(join(root, j1, j2, j3, j4).get(a), value, cb));
  }

}
