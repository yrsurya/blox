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
import com.amazonaws.blox.dataservice.model.Environment;
import com.amazonaws.blox.dataservice.model.EnvironmentHealth;
import com.amazonaws.blox.dataservice.model.EnvironmentStatus;
import com.amazonaws.blox.dataservice.storage.EnvironmentStore;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.Validate;

@Component
@AllArgsConstructor
public class EnvironmentHandler {

  @NonNull private EnvironmentStore environmentStore;

  public Environment createEnvironment(final Environment environment) throws StorageException {

    //TODO: validate required fields

    environment.setStatus(EnvironmentStatus.Inactive);
    environment.setHealth(EnvironmentHealth.Healthy);
    environment.setCreatedTime(Instant.now());
    //TODO: how do we version?
    environment.setEnvironmentVersion("1");

    switch (environment.getType()) {
      case Daemon:
        return environmentStore.createDaemonEnvironment(environment);
      default:
        throw new IllegalArgumentException(
            String.format(
                "Environment of type %s is not supported", environment.getType().toString()));
    }
  }

  public Environment getEnvironment(final Environment environment) throws StorageException {

    Validate.notBlank(environment.getName());
    Validate.notBlank(environment.getAccountId());

    return environmentStore.getDaemonEnvironment(environment.getName(), environment.getAccountId());
  }
}
