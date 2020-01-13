package ru.jpa.utils.specification.predicates;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.metamodel.*;

interface RawJoiner {

  @SuppressWarnings("unchecked")
  static <S, T> Join<S, T> join(final From<S, ?> from, Attribute<?, ?>... attributes) {
    From<S, ?> join = from;
    for (Attribute attribute : attributes) {
      join = join(join, attribute);
    }
    return (Join<S, T>) join;
  }

  static <S, X, Y> Join<X, Y> join(final From<S, X> from, Attribute<X, Y> attribute) {
    if (attribute instanceof SingularAttribute) {
      return from.join((SingularAttribute<X, Y>) attribute);
    }
    if (attribute instanceof CollectionAttribute) {
      return from.join((CollectionAttribute<X, Y>) attribute);
    }
    if (attribute instanceof SetAttribute) {
      return from.join((SetAttribute<X, Y>) attribute);
    }
    if (attribute instanceof ListAttribute) {
      return from.join((ListAttribute<X, Y>) attribute);
    }
    if (attribute instanceof MapAttribute) {
      return from.join((MapAttribute<X, ?, Y>) attribute);
    }
    throw new RuntimeException("Unexpected attribute type: " + attribute.getClass().getName());
  }

}
