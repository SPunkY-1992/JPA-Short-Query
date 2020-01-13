package ru.jpa.utils.specification;

import ru.jpa.utils.specification.predicates.*;

public interface Predicate extends IsEquals, In, Have, Like, OrderBy,
    LessThan, LessThanOrEqualTo, GreaterThan, GreaterThanOrEqualTo {

}
