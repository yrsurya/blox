/*
 * Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may
 * not use this file except in compliance with the License. A copy of the
 * License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.blox.dataservice.handler;

import com.amazonaws.blox.dataservice.exception.StorageException;
import com.amazonaws.blox.dataservice.model.Deployment;
import com.amazonaws.blox.dataservice.model.DeploymentStatus;
import com.amazonaws.blox.dataservice.model.Environment;
import com.amazonaws.blox.dataservice.storage.DeploymentStore;
import com.amazonaws.blox.dataservicemodel.v1.exception.DeploymentDoesNotExist;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentNotFoundException;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentVersionOutdatedException;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeploymentHandler {

  @NonNull private DeploymentStore deploymentStore;

  @NonNull private EnvironmentHandler environmentHandler;

  public Deployment createDeployment(final Deployment deployment)
      throws EnvironmentNotFoundException, EnvironmentVersionOutdatedException, StorageException {

    //TODO: ensure environment version is latest
    //throw new EnvironmentVersionOutdated
    //somehow ensure version doesn't change. transaction?

    final Environment environment =
        environmentHandler.getLatestEnvironmentVersion(
            Environment.builder()
                .name(deployment.getEnvironmentName())
                .accountId(deployment.getAccountId())
                .environmentVersion(deployment.getEnvironmentVersion())
                .build());

    if (!deployment.getEnvironmentVersion().equals(environment.getEnvironmentVersion())) {
      throw new EnvironmentVersionOutdatedException(
          String.format(
              "Environment version %s specified in the deployment is outdated. The latest environment version is %s",
              deployment.getEnvironmentVersion(), environment.getEnvironmentVersion()));
    }

    deployment.setDeploymentId(UUID.randomUUID().toString());
    deployment.setCreatedTime(Instant.now());
    deployment.setDeploymentStatus(DeploymentStatus.Pending);

    return deploymentStore.createDeployment(deployment);
  }

  public Deployment getDeployment(final Deployment deployment)
      throws DeploymentDoesNotExist, StorageException {

    Validate.notNull(deployment.getDeploymentId());
    return deploymentStore.getDeploymentById(deployment.getDeploymentId());
  }
}
