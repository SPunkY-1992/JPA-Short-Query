package ru.jpa.utils.specification.predicates;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.PluralAttribute;
import java.util.Collection;

public interface Have {

  /**
   * example of usage: have(User_.projects, project)
   */
  default <T, S extends T, A>
  Specification<S> have(CollectionAttribute<T, A> attribute, A value) {
    return (root, cq, cb) -> cb.isMember(value, root.get(attribute.getName()));
  }

  /**
   * example of usage: have(User_.projects, Project_.subProjects, subProject)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A>
  Specification<S> have(J1 join1, PluralAttribute<TJ1, Collection<A>, A> attribute, A value) {
    return (root, cq, cb) ->
        cb.isMember(value, RawJoiner.<S, TJ1>join(root, join1).get(attribute.getName()));
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A>
  Specification<S> have(J1 j1, J2 j2, PluralAttribute<TJ2, Collection<A>, A> attribute, A value) {
    return (root, cq, cb) ->
        cb.isMember(value, RawJoiner.<S, TJ2>join(root, j1, j2).get(attribute.getName()));
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A>
  Specification<S> have(J1 j1, J2 j2, J3 j3, PluralAttribute<TJ3, Collection<A>, A> a, A value) {
    return (root, cq, cb) ->
        cb.isMember(value, RawJoiner.<S, TJ3>join(root, j1, j2, j3).get(a.getName()));
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>,
      TJ4, J4 extends Bindable<TJ4> & Attribute<TJ3, ?>, A>
  Specification<S> have(J1 j1, J2 j2, J3 j3, J4 j4, PluralAttribute<TJ4, Collection<A>, A> a, A v) {
    return (root, cq, cb) ->
        cb.isMember(v, RawJoiner.<S, TJ4>join(root, j1, j2, j3, j4).get(a.getName()));
  }

}
