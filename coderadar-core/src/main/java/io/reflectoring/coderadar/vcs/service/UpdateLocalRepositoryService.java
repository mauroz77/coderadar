package io.reflectoring.coderadar.vcs.service;

import io.reflectoring.coderadar.projectadministration.domain.Branch;
import io.reflectoring.coderadar.vcs.UnableToUpdateRepositoryException;
import io.reflectoring.coderadar.vcs.port.driven.UpdateLocalRepositoryPort;
import io.reflectoring.coderadar.vcs.port.driver.update.UpdateLocalRepositoryUseCase;
import io.reflectoring.coderadar.vcs.port.driver.update.UpdateRepositoryCommand;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UpdateLocalRepositoryService implements UpdateLocalRepositoryUseCase {

  private final UpdateLocalRepositoryPort updateLocalRepositoryPort;

  public UpdateLocalRepositoryService(UpdateLocalRepositoryPort updateLocalRepositoryPort) {
    this.updateLocalRepositoryPort = updateLocalRepositoryPort;
  }

  @Override
  public List<Branch> updateRepository(UpdateRepositoryCommand command)
      throws UnableToUpdateRepositoryException {
    return updateLocalRepositoryPort.updateRepository(command);
  }
}
