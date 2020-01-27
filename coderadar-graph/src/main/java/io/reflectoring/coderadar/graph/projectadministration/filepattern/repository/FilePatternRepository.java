package io.reflectoring.coderadar.graph.projectadministration.filepattern.repository;

import io.reflectoring.coderadar.graph.projectadministration.domain.FilePatternEntity;
import java.util.List;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface FilePatternRepository extends Neo4jRepository<FilePatternEntity, Long> {
  @Query("MATCH (p)-[:HAS]->(fp) WHERE ID(p) = {0} RETURN fp")
  @NonNull
  List<FilePatternEntity> findByProjectId(@NonNull Long projectId);
}
