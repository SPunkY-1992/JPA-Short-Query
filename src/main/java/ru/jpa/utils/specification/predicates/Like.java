package ru.jpa.utils.specification.predicates;

import static ru.jpa.utils.specification.join.Joiner.join;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface Like {

  /**
   * example of usage: like(User_.text, "Johnny")
   */
  default <T, S extends T, A>
  Specification<S> like(SingularAttribute<T, A> attribute, String value) {
    return (root, cq, cb) ->
        value == null
            ? cb.disjunction()
            : cb.like(
                cb.lower(root.get(attribute.getName()).as(String.class)),
                "%" + value.toLowerCase() + "%"
            );
  }

  /**
   * example of usage: like(User_.projects, Project_.name, partOfName)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A>
  Specification<S> like(J1 join1, SingularAttribute<TJ1, A> a, String value) {
    return (root, cq, cb) ->
        value == null
            ? cb.disjunction()
            : cq.distinct(true)
                .where(cb.like(
                    cb.lower(join(root, join1).get(a.getName()).as(String.class)),
                    "%" + value.toLowerCase() + "%"
                ))
                .getRestriction();
  }

  /**
   * example of usage: like(User_.projects, Project_.tasks, Task_.name, partOfName)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A>
  Specification<S> like(J1 j1, J2 j2, SingularAttribute<TJ2, A> a, String value) {
    return (root, cq, cb) ->
        value == null
            ? cb.disjunction()
            : cq.distinct(true)
                .where(cb.like(
                    cb.lower(join(root, j1, j2).get(a.getName()).as(String.class)),
                    "%" + value.toLowerCase() + "%"
                ))
                .getRestriction();
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A>
  Specification<S> like(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> a, String value) {
    return (root, cq, cb) ->
        value == null
            ? cb.disjunction()
            : cq.distinct(true)
                .where(cb.like(
                    cb.lower(join(root, j1, j2, j3).get(a.getName()).as(String.class)),
                    "%" + value.toLowerCase() + "%"
                ))
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
  Specification<S> like(J1 j1, J2 j2, J3 j3, J4 j4, SingularAttribute<TJ4, A> a, String value) {
    return (root, cq, cb) ->
        value == null
            ? cb.disjunction()
            : cq.distinct(true)
                .where(cb.like(
                    cb.lower(join(root, j1, j2, j3, j4).get(a.getName()).as(String.class)),
                    "%" + value.toLowerCase() + "%"
                ))
                .getRestriction();
  }

}
