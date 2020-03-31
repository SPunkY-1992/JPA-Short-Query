package ru.jpa.utils.specification.predicates;

import static ru.jpa.utils.specification.join.Joiner.join;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface Between {

  /**
   * example of usage: between(User_.number, 333, 666)
   * result: between lower and upper inclusive
   */
  default <T, S extends T, A extends Comparable<? super A>>
  Specification<S> between(SingularAttribute<T, A> attribute, A lower, A upper) {
    return (root, cq, cb) ->
        lower == null || upper == null
            ? cb.disjunction()
            : cb.between(root.get(attribute), lower, upper);
  }

  /**
   * example of usage: between(User_.projects, Project_.number, 333, 666)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A extends Comparable<? super A>>
  Specification<S> between(J1 join1, SingularAttribute<TJ1, A> attribute, A lower, A upper) {
    return (root, cq, cb) ->
        lower == null || upper == null
            ? cb.disjunction()
            : cq.distinct(true)
                .where(cb.between(join(root, join1).get(attribute), lower, upper))
                .getRestriction();
  }

  /**
   * example of usage: between(User_.projects, Project_.tasks, Task_.number, 333, 666)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A extends Comparable<? super A>>
  Specification<S> between(J1 join1, J2 join2, SingularAttribute<TJ2, A> attribute, A l, A u) {
    return (root, cq, cb) ->
        l == null || u == null
            ? cb.disjunction()
            : cq.distinct(true)
                .where(cb.between(join(root, join1, join2).get(attribute), l, u))
                .getRestriction();
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A extends Comparable<? super A>>
  Specification<S> between(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> attribute, A l, A u) {
    return (root, cq, cb) ->
        l == null || u == null
            ? cb.disjunction()
            : cq.distinct(true)
                .where(cb.between(join(root, j1, j2, j3).get(attribute), l, u))
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
  Specification<S> between(J1 j1, J2 j2, J3 j3, J4 j4, SingularAttribute<TJ4, A> a, A l, A u) {
    return (root, cq, cb) ->
        l == null || u == null
            ? cb.disjunction()
            : cq.distinct(true)
                .where(cb.between(join(root, j1, j2, j3, j4).get(a), l, u))
                .getRestriction();
  }

}
