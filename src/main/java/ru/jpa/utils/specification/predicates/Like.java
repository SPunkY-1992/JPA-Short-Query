package ru.jpa.utils.specification.predicates;

import static ru.jpa.utils.specification.ShortQuery.distinct;
import static ru.jpa.utils.specification.join.Joiner.join;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface Like {

  private static <A> Predicate like(Path<A> path, String value, CriteriaBuilder cb) {
    return value == null
        ? cb.disjunction()
        : cb.like(cb.lower(path.as(String.class)), "%" + value.toLowerCase() + "%");
  }

  /**
   * example of usage: like(User_.text, "Johnny cat")
   */
  default <T, S extends T, A>
  Specification<S> like(SingularAttribute<T, A> attribute, String value) {
    return (root, cq, cb) -> like(root.get(attribute), value, cb);
  }

  /**
   * example of usage: like(User_.projects, Project_.name, partOfName)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>, A>
  Specification<S> like(J1 join1, SingularAttribute<TJ1, A> attribute, String value) {
    return distinct((root, cq, cb) -> like(join(root, join1).get(attribute), value, cb));
  }

  /**
   * example of usage: like(User_.projects, Project_.tasks, Task_.name, partOfName)
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>, A>
  Specification<S> like(J1 join1, J2 join2, SingularAttribute<TJ2, A> attribute, String value) {
    return distinct((root, cq, cb) -> like(join(root, join1, join2).get(attribute), value, cb));
  }

  /**
   * example of usage: same as previous
   */
  default <S extends T, T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>, A>
  Specification<S> like(J1 j1, J2 j2, J3 j3, SingularAttribute<TJ3, A> attribute, String value) {
    return distinct((root, cq, cb) -> like(join(root, j1, j2, j3).get(attribute), value, cb));
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
    return distinct((root, cq, cb) -> like(join(root, j1, j2, j3, j4).get(a), value, cb));
  }

}
