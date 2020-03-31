package ru.jpa.utils.specification.predicates;

import static ru.jpa.utils.specification.join.Joiner.join;

import java.util.Collection;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.PluralAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface Have {

  /**
   * example of usage: have(User_.projects, project)
   */
  default <T, S extends T, A>
  Specification<S> have(PluralAttribute<T, ? extends Collection<A>, A> attribute, A value) {
    return (root, cq, cb) -> cb.isMember(value, root.get(attribute.getName()));
  }

  /**
   * example of usage: have(User_.projects, Project_.tasks, task)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A>
  Specification<S> have(J1 join1, PluralAttribute<TJ1, ? extends Collection<A>, A> attribute, A value) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(cb.isMember(value, join(root, join1).get(attribute.getName())))
        .getRestriction();
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A>
  Specification<S> have(J1 j1, J2 j2, PluralAttribute<TJ2, ? extends Collection<A>, A> attribute, A value) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(cb.isMember(value, join(root, j1, j2).get(attribute.getName())))
        .getRestriction();
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A>
  Specification<S> have(J1 j1, J2 j2, J3 j3, PluralAttribute<TJ3, ? extends Collection<A>, A> a, A value) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(cb.isMember(value, join(root, j1, j2, j3).get(a.getName())))
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
  Specification<S> have(J1 j1, J2 j2, J3 j3, J4 j4, PluralAttribute<TJ4, ? extends Collection<A>, A> a, A v) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(cb.isMember(v, join(root, j1, j2, j3, j4).get(a.getName())))
        .getRestriction();
  }

}
