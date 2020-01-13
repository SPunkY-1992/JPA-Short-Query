package ru.jpa.utils.specification.predicates;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;

public interface GreaterThanOrEqualTo {

  /**
   * example of usage: greaterThanOrEqualTo(User_.number, 666)
   */
  default <T, S extends T, A extends Comparable<? super A>>
  Specification<S> greaterThanOrEqualTo(SingularAttribute<T, A> attribute, A value) {
    return (root, cq, cb) ->
        value == null
            ? cb.isNull(root.get(attribute))
            : cb.greaterThanOrEqualTo(root.get(attribute), value);
  }

  /**
   * example of usage: greaterThanOrEqualTo(User_.projects, Project_.number, 666)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A extends Comparable<? super A>>
  Specification<S> greaterThanOrEqualTo(J1 join1, SingularAttribute<TJ1, A> attribute, A value) {
    return (root, cq, cb) ->
        value == null
            ? cb.isNull(RawJoiner.<S, TJ1>join(root, join1).get(attribute))
            : cb.greaterThanOrEqualTo(RawJoiner.<S, TJ1>join(root, join1).get(attribute), value);
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A extends Comparable<? super A>>
  Specification<S> greaterThanOrEqualTo(J1 j1, J2 j2, SingularAttribute<TJ2, A> a, A value) {
    return (root, cq, cb) ->
        value == null
            ? cb.isNull(RawJoiner.<S, TJ2>join(root, j1, j2).get(a))
            : cb.greaterThanOrEqualTo(RawJoiner.<S, TJ2>join(root, j1, j2).get(a), value);
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A extends Comparable<? super A>>
  Specification<S> greaterThanOrEqualTo(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> a, A value) {
    return (root, cq, cb) ->
        value == null
            ? cb.isNull(RawJoiner.<S, TJ3>join(root, j1, j2, j3).get(a))
            : cb.greaterThanOrEqualTo(RawJoiner.<S, TJ3>join(root, j1, j2, j3).get(a), value);
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>,
      TJ4, J4 extends Bindable<TJ4> & Attribute<TJ3, ?>, A extends Comparable<? super A>>
  Specification<S> greaterThanOrEqualTo(J1 j1, J2 j2, J3 j3, J4 j4, SingularAttribute<TJ4, A> a,
                                        A v) {
    return (root, cq, cb) ->
        v == null
            ? cb.isNull(RawJoiner.<S, TJ4>join(root, j1, j2, j3, j4).get(a))
            : cb.greaterThanOrEqualTo(RawJoiner.<S, TJ4>join(root, j1, j2, j3, j4).get(a), v);
  }

}
