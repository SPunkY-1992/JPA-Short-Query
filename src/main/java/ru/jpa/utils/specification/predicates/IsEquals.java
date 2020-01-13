package ru.jpa.utils.specification.predicates;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface IsEquals {

  /**
   * example of usage:
   * isEquals(User_.text, "some text")
   * isEquals(User_.number, null)
   */
  default <S extends T, T, A>
  Specification<S> isEquals(SingularAttribute<T, A> attribute, A value) {
    return (root, cq, cb) ->
        value == null
            ? cb.isNull(root.get(attribute))
            : cb.equal(root.get(attribute), value);
  }

  /**
   * example of usage: isEquals(User_.projects, Project_.id, projectId)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A>
  Specification<S> isEquals(J1 join1, SingularAttribute<TJ1, A> attribute, A value) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(
            value == null
                ? cb.isNull((RawJoiner.<S, TJ1>join(root, join1)).get(attribute))
                : cb.equal(RawJoiner.<S, TJ1>join(root, join1).get(attribute), value)
        )
        .getRestriction();
  }

  /**
   * example of usage: isEquals(User_.projects, Project_.tasks, Task_.id, taskId)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A>
  Specification<S> isEquals(J1 join1, J2 join2, SingularAttribute<TJ2, A> attribute, A value) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(
            value == null
                ? cb.isNull(RawJoiner.<S, TJ2>join(root, join1, join2).get(attribute))
                : cb.equal(RawJoiner.<S, TJ2>join(root, join1, join2).get(attribute), value)
        )
        .getRestriction();
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A>
  Specification<S> isEquals(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> attribute, A value) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(
            value == null
                ? cb.isNull(RawJoiner.<S, TJ3>join(root, j1, j2, j3).get(attribute))
                : cb.equal(RawJoiner.<S, TJ3>join(root, j1, j2, j3).get(attribute), value)
        )
        .getRestriction();
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
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(
            value == null
                ? cb.isNull(RawJoiner.<S, TJ4>join(root, j1, j2, j3, j4).get(a))
                : cb.equal(RawJoiner.<S, TJ4>join(root, j1, j2, j3, j4).get(a), value)
        )
        .getRestriction();
  }

}
