package ru.jpa.utils.specification.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.jpa.utils.specification.entity.Project;

@Repository
public interface ProjectRepository
    extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {

}
