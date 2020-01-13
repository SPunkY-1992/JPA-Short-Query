# jpa-utils

### when you have:

    @Repository
    public interface UsualRepository
        extends JpaRepository<UsualEntity, UUID>, JpaSpecificationExecutor<UsualEntity> {

    }

### then you can:

    Collection<UsualEntity> found = usualRepository
        .findAll(PREDICATE.greaterThanOrEqualTo(UsualEntity_.number, YOUR_NUMBER));

### or:

    Collection<UsualEntity> found = usualRepository
        .findAll(PREDICATE.have(UsualEntity_.someList, Some_.subSome, subSome));

### and:

    List<UsualEntity> desk = usualRepository
        .findAll(
            PREDICATE.orderBy(UsualEntity_.someList, Some_.subSomeList, SubSome_.number, Direction.DESC)
        );

### even:

    private static Set<SingularAttribute> fieldsForSearch = Sets.newHashSet(
            UsualEntity_.number, UsualEntity_.name, UsualEntity_.enum
    );

    public static Specification<UsualEntity> searchByQuery(String query) {
        return isEmpty(trim(query))
                ? any()
                : search(Sets.newHashSet(query.split("\\s+")), fieldsForSearch);
    }
