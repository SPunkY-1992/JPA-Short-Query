package ru.jpa.utils.specification.predicates;

import static ru.jpa.utils.specification.join.Joiner.join;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface LessThanOrEqualTo {

  /**
   * example of usage: lessThanOrEqualTo(User_.number, 666)
   */
  default <T, S extends T, A extends Comparable<? super A>>
  Specification<S> lessThanOrEqualTo(SingularAttribute<T, A> attribute, A value) {
    return (root, cq, cb) ->
        value == null
            ? cb.isNull(root.get(attribute))
            : cb.lessThanOrEqualTo(root.get(attribute), value);
  }

  /**
   * example of usage: lessThanOrEqualTo(User_.projects, Project_.number, 666)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A extends Comparable<? super A>>
  Specification<S> lessThanOrEqualTo(J1 join1, SingularAttribute<TJ1, A> attribute, A value) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(
            value == null
                ? cb.isNull(join(root, join1).get(attribute))
                : cb.lessThanOrEqualTo(join(root, join1).get(attribute), value)
        )
        .getRestriction();
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A extends Comparable<? super A>>
  Specification<S> lessThanOrEqualTo(J1 j1, J2 j2, SingularAttribute<TJ2, A> attribute, A value) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(
            value == null
                ? cb.isNull(join(root, j1, j2).get(attribute))
                : cb.lessThanOrEqualTo(join(root, j1, j2).get(attribute), value)
        )
        .getRestriction();
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A extends Comparable<? super A>>
  Specification<S> lessThanOrEqualTo(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> a, A value) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(
            value == null
                ? cb.isNull(join(root, j1, j2, j3).get(a))
                : cb.lessThanOrEqualTo(join(root, j1, j2, j3).get(a), value)
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
      TJ4, J4 extends Bindable<TJ4> & Attribute<TJ3, ?>, A extends Comparable<? super A>>
  Specification<S> lessThanOrEqualTo(J1 j1, J2 j2, J3 j3, J4 j4, SingularAttribute<TJ4, A> a, A v) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(
            v == null
                ? cb.isNull(join(root, j1, j2, j3, j4).get(a))
                : cb.lessThanOrEqualTo(join(root, j1, j2, j3, j4).get(a), v)
        )
        .getRestriction();
  }

}
