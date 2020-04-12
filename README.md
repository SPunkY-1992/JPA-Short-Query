# JPA Short Query

### when you have:

    @Repository
    public interface UsualRepository
        extends JpaRepository<UsualEntity, UUID>, JpaSpecificationExecutor<UsualEntity> {

    }

### then you can:

    // find users with number ≥ 2000
    List<User> result = userRepository.findAll(PREDICATE.greaterThanOrEqualTo(User_.number, 2000));

### or:

    // find users with number < 1000 who have projects with tasks whose text contains "some text"
    List<User> result = userRepository
        .findAll(PREDICATE.like(User_.projects, Project_.tasks, Task_.text, "some text")
            .and(PREDICATE.lessThan(User_.number, 1000))
            .and(orderBy(User_.zonedDateTime, ASC))
        );

### and:

    // find users who have a common project with users participating in myProject
    List<User> result = userRepository
        .findAll(PREDICATE.have(User_.projects, Project_.users, User_.projects, myProject));

### even:

    Set<SingularAttribute> fields = Set.of(UsualEntity_.number, UsualEntity_.name, UsualEntity_.enum);

    Specification<UsualEntity> searchByQuery(String query) {
        return isEmpty(trim(query))
                ? any()
                : search(Sets.newHashSet(query.split("\\s+")), fields);
    }

    List<UsualEntity> result = usualRepository.findAll(searchByQuery(query));

#
###### see tests for more usage examples

    <dependency>
      <groupId>ru.jpa.utils</groupId>
      <artifactId>short-query</artifactId>
      <version>0.1.1</version>
    </dependency>
