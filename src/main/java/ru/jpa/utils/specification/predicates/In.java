package ru.jpa.utils.specification.predicates;

import static java.util.stream.Collectors.toSet;
import static ru.jpa.utils.specification.ShortQuery.distinct;
import static ru.jpa.utils.specification.join.Joiner.join;
import static ru.jpa.utils.specification.predicates.TypeConverter.dateTimeWithMicroseconds;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface In {

  private static <A> Predicate in(Path<A> path, Collection<A> values, CriteriaBuilder cb) {
    if (values == null || values.isEmpty()) {
      return cb.disjunction();
    }
    if (values.iterator().next() instanceof LocalDateTime) {
      return cb
          .function("TO_TIMESTAMP", String.class, path, cb.literal("YYYY-MM-DD HH24:MI:SS.FF 6"))
          .in(values.stream().map(value -> dateTimeWithMicroseconds((LocalDateTime) value)).collect(toSet()));
    }
    if (values.iterator().next() instanceof ZonedDateTime) {
      return cb
          .function("TO_TIMESTAMP", String.class, path, cb.literal("YYYY-MM-DD HH24:MI:SS.FF 6"))
          .in(values.stream().map(value -> dateTimeWithMicroseconds((ZonedDateTime) value)).collect(toSet()));
    }
    return path.in(values);
  }

  /**
   * example of usage: in(User_.number, Set.of(1, 2, 3))
   */
  default <T, S extends T, A>
  Specification<S> in(SingularAttribute<T, A> attribute, Collection<A> values) {
    return (root, cq, cb) -> in(root.get(attribute), values, cb);
  }

  /**
   * example of usage: in(User_.projects, Project_.name, projectNames)
   */
  default <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A>
  Specification<S> in(J1 join1, SingularAttribute<TJ1, A> attribute, Collection<A> values) {
    return distinct((root, cq, cb) -> in(join(root, join1).get(attribute), values, cb));
  }

  /**
   * example of usage: in(User_.projects, Project_.tasks, Task.text, taskTexts)
   */
  default <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A>
  Specification<S> in(J1 j1, J2 j2, SingularAttribute<TJ2, A> attribute, Collection<A> values) {
    return distinct((root, cq, cb) -> in(join(root, j1, j2).get(attribute), values, cb));
  }

  /**
   * example of usage: same as previous
   */
  default <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A>
  Specification<S> in(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> a, Collection<A> values) {
    return distinct((root, cq, cb) -> in(join(root, j1, j2, j3).get(a), values, cb));
  }

  /**
   * example of usage: same as previous
   */
  default <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>,
      TJ4, J4 extends Bindable<TJ4> & Attribute<TJ3, ?>, A>
  Specification<S> in(J1 j1, J2 j2, J3 j3, J4 j4, SingularAttribute<TJ4, A> a, Collection<A> v) {
    return distinct((root, cq, cb) -> in(join(root, j1, j2, j3, j4).get(a), v, cb));
  }

}
