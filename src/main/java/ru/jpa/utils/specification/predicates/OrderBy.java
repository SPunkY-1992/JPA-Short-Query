package ru.jpa.utils.specification.predicates;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;

import static ru.jpa.utils.specification.SpecificationUtils.any;

public interface OrderBy {

  /**
   * example of usage: orderBy(System_.name, Sort.Direction.ASC)
   */
  default <T, S extends T, A>
  Specification<S> orderBy(SingularAttribute<T, A> attribute, Direction direction) {
    return direction == null
        ? any()
        : (root, cq, cb) -> cq
            .orderBy(direction == Direction.ASC
                ? cb.asc(root.get(attribute))
                : cb.desc(root.get(attribute))
            )
            .getRestriction();
  }

  /**
   * example of usage: orderBy(User_.projects, Project_.name, Sort.Direction.ASC)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A>
  Specification<S> orderBy(J1 join1, SingularAttribute<TJ1, A> attribute, Direction direction) {
    return direction == null
        ? any()
        : (root, cq, cb) -> cq
            .orderBy(direction == Direction.ASC
                ? cb.asc(RawJoiner.<S, TJ1>join(root, join1).get(attribute))
                : cb.desc(RawJoiner.<S, TJ1>join(root, join1).get(attribute))
            )
            .getRestriction();
  }

  /**
   * example of usage: orderBy(User_.projects, Project_.subProjects, SubProject_.name, DESC)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A>
  Specification<S> orderBy(J1 j1, J2 j2, SingularAttribute<TJ2, A> attribute, Direction direction) {
    return direction == null
        ? any()
        : (root, cq, cb) -> cq
            .orderBy(direction == Direction.ASC
                ? cb.asc(RawJoiner.<S, TJ2>join(root, j1, j2).get(attribute))
                : cb.desc(RawJoiner.<S, TJ2>join(root, j1, j2).get(attribute))
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
  Specification<S> orderBy(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> a, Direction direction) {
    return direction == null
        ? any()
        : (root, cq, cb) -> cq
            .orderBy(direction == Direction.ASC
                ? cb.asc(RawJoiner.<S, TJ3>join(root, j1, j2, j3).get(a))
                : cb.desc(RawJoiner.<S, TJ3>join(root, j1, j2, j3).get(a))
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
  Specification<S> orderBy(J1 j1, J2 j2, J3 j3, J4 j4, SingularAttribute<TJ4, A> a, Direction d) {
    return d == null
        ? any()
        : (root, cq, cb) -> cq
            .orderBy(d == Direction.ASC
                ? cb.asc(RawJoiner.<S, TJ4>join(root, j1, j2, j3, j4).get(a))
                : cb.desc(RawJoiner.<S, TJ4>join(root, j1, j2, j3, j4).get(a))
            )
            .getRestriction();
  }

}
