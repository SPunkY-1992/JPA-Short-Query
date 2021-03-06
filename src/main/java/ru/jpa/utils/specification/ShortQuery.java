package ru.jpa.utils.specification;

import java.util.Collection;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

public interface ShortQuery {

  Predicate PREDICATE = new Predicate() {
  };

  static <T> Specification<T> any() {
    return (root, cq, cb) -> cb.conjunction();
  }

  static <T> Specification<T> neither() {
    return (root, cq, cb) -> cb.disjunction();
  }

  static <T> Specification<T> distinct(Specification<T> specification) {
    return (root, cq, cb) -> cq
        .distinct(true)
        .where(specification.toPredicate(root, cq, cb))
        .getRestriction();
  }

  /**
   * example of usage: orderBy(User_.number, Direction.ASC)
   */
  static <T, S extends T, A>
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
   * example of usage: search(Sets.newHashSet(query.split(" ")), Set.of(User_.text, User_.number)
   *
   * @param attributes entity fields that can contain searchable values
   * @param values searchable values
   * @param <T> any entity
   * @return if attributes or words is empty than return neither, else: (p1 contain w1 or p2 contain
   * w1 or ...) and (p1 contain w2 or p2 contain w2 or ...) and ...
   */
  static <T, S extends T> Specification<S> search(
      Collection<SingularAttribute> attributes,
      Collection<String> values
  ) {
    return (root, cq, cb) ->
        values.stream()
            .map(String::toLowerCase)
            .map(value -> attributes.stream()
                .map(attribute -> cb.like(
                    cb.lower(root.get(attribute.getName()).as(String.class)),
                    "%" + value + "%"
                ))
                .reduce(cb::or).orElse(cb.disjunction())
            )
            .reduce(cb::and).orElse(cb.disjunction());
  }

}
