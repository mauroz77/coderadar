package io.reflectoring.coderadar.rest.dependencymap;

import io.reflectoring.coderadar.dependencymap.port.driver.GetCompareTreeUseCase;
import io.reflectoring.coderadar.rest.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class DependencyCompareTreeController implements AbstractBaseController {

  private final GetCompareTreeUseCase getCompareTreeUseCase;

  @Autowired
  public DependencyCompareTreeController(GetCompareTreeUseCase getCompareTreeUseCase) {
    this.getCompareTreeUseCase = getCompareTreeUseCase;
  }

  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/analyzers/{projectId}/structureMap/{commitName}/{secondCommit}")
  public ResponseEntity<Object> getDependencyTree(
      @PathVariable("projectId") Long projectId,
      @PathVariable("commitName") String commitName,
      @PathVariable("secondCommit") String secondCommit) {
    return ResponseEntity.ok(
        getCompareTreeUseCase.getDependencyTree(projectId, commitName, secondCommit));
  }
}
