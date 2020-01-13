package ru.jpa.utils.specification;

import ru.jpa.utils.specification.predicates.*;

public interface Predicate extends IsEquals, In, Have, Like, Between, LessThan, GreaterThan,
    LessThanOrEqualTo, GreaterThanOrEqualTo {

}
