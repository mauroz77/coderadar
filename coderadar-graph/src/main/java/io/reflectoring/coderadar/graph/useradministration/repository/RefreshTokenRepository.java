package io.reflectoring.coderadar.graph.useradministration.repository;

import io.reflectoring.coderadar.graph.useradministration.domain.RefreshTokenEntity;
import io.reflectoring.coderadar.graph.useradministration.domain.UserEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends Neo4jRepository<RefreshTokenEntity, Long> {

  @Query("MATCH (r:RefreshTokenEntity {token: {0}}) RETURN r")
  RefreshTokenEntity findByToken(@NonNull String token);

  @Query("MATCH (r:RefreshTokenEntity {token: {0}})<-[:HAS]-(u) RETURN u")
  UserEntity findUserByToken(@NonNull String token);

  @Query("MATCH (u)-[:HAS]->(r) WHERE ID(u) = {0} DETACH DELETE r")
  void deleteByUser(@NonNull Long userId);
}
