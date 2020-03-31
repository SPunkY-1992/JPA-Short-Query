# jpa-utils

### when you have:

    @Repository
    public interface UsualRepository
        extends JpaRepository<UsualEntity, UUID>, JpaSpecificationExecutor<UsualEntity> {

    }

### then you can:

    List<User> result = userRepository.findAll(PREDICATE.greaterThan(User_.number, bound));

### or:

    List<User> result = userRepository
        .findAll(PREDICATE.isEquals(User_.projects, Project_.tasks, Task_.text, "task text"));

### and:

    List<User> result = userRepository
        .findAll(PREDICATE.have(User_.projects, Project_.tasks, task));

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
      <artifactId>jpa-utils</artifactId>
      <version>0.0.8</version>
    </dependency>
