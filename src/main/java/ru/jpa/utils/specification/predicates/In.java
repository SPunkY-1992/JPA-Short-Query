package ru.jpa.utils.specification.predicates;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Collection;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface In {

  /**
   * example of usage: in(User_.number, Set.of(1, 2, 3))
   */
  default <T, S extends T, A>
  Specification<S> in(SingularAttribute<T, A> attribute, Collection<A> values) {
    return (root, cq, cb) ->
        isEmpty(values)
            ? cb.disjunction()
            : root.get(attribute).in(values);
  }

  /**
   * example of usage: in(User_.projects, Project_.name, projectNames)
   */
  default <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A>
  Specification<S> in(J1 join1, SingularAttribute<TJ1, A> attribute, Collection<A> values) {
    return (root, cq, cb) ->
        isEmpty(values)
            ? cb.disjunction()
            : cq.distinct(true)
                .where(RawJoiner.<S, TJ1>join(root, join1).get(attribute).in(values))
                .getRestriction();
  }

  /**
   * example of usage: in(User_.projects, Project_.tasks, Task.text, taskTexts)
   */
  default <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A>
  Specification<S> in(J1 j1, J2 j2, SingularAttribute<TJ2, A> attribute, Collection<A> values) {
    return (root, cq, cb) ->
        isEmpty(values)
            ? cb.disjunction()
            : cq.distinct(true)
                .where(RawJoiner.<S, TJ2>join(root, j1, j2).get(attribute).in(values))
                .getRestriction();
  }

  /**
   * example of usage: same as previous
   */
  default <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A>
  Specification<S> in(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> a, Collection<A> values) {
    return (root, cq, cb) ->
        isEmpty(values)
            ? cb.disjunction()
            : cq.distinct(true)
                .where(RawJoiner.<S, TJ3>join(root, j1, j2, j3).get(a).in(values))
                .getRestriction();
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
    return (root, cq, cb) ->
        isEmpty(v)
            ? cb.disjunction()
            : cq.distinct(true)
                .where(RawJoiner.<S, TJ4>join(root, j1, j2, j3, j4).get(a).in(v))
                .getRestriction();
  }

}
