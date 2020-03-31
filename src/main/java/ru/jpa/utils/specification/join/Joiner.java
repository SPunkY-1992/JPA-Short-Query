package ru.jpa.utils.specification.join;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;

public interface Joiner {

  static <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>>
  Join<S, TJ1> join(Root<S> root, J1 join1) {
    return RawJoiner.join(root, join1);
  }

  static <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>>
  Join<S, TJ2> join(Root<S> root, J1 j1, J2 j2) {
    return RawJoiner.join(root, j1, j2);
  }

  static <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>>
  Join<S, TJ3> join(Root<S> root, J1 j1, J2 j2, J3 j3) {
    return RawJoiner.join(root, j1, j2, j3);
  }

  static <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>,
      TJ4, J4 extends Bindable<TJ4> & Attribute<TJ3, ?>>
  Join<S, TJ4> join(Root<S> root, J1 j1, J2 j2, J3 j3, J4 j4) {
    return RawJoiner.join(root, j1, j2, j3, j4);
  }

  static <T, S extends T,
      TJ1, J1 extends Bindable<TJ1> & Attribute<T, ?>,
      TJ2, J2 extends Bindable<TJ2> & Attribute<TJ1, ?>,
      TJ3, J3 extends Bindable<TJ3> & Attribute<TJ2, ?>,
      TJ4, J4 extends Bindable<TJ4> & Attribute<TJ3, ?>,
      TJ5, J5 extends Bindable<TJ5> & Attribute<TJ4, ?>>
  Join<S, TJ5> join(Root<S> root, J1 j1, J2 j2, J3 j3, J4 j4, J5 j5) {
    return RawJoiner.join(root, j1, j2, j3, j4, j5);
  }

}
