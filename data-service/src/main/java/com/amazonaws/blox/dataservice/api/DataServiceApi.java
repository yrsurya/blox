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
package com.amazonaws.blox.dataservice.api;

import com.amazonaws.blox.dataservice.exception.StorageException;
import com.amazonaws.blox.dataservice.handler.EnvironmentHandler;
import com.amazonaws.blox.dataservice.model.Environment;
import com.amazonaws.blox.dataservice.model.EnvironmentTranslator;
import com.amazonaws.blox.dataservicemodel.v1.client.DataService;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentExistsException;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentNotFoundException;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentVersionNotFoundException;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentVersionOutdatedException;
import com.amazonaws.blox.dataservicemodel.v1.exception.InvalidParameterException;
import com.amazonaws.blox.dataservicemodel.v1.exception.ServiceException;
import com.amazonaws.blox.dataservicemodel.v1.model.CreateEnvironmentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.CreateEnvironmentResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.GetEnvironmentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.GetEnvironmentResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.StartDeploymentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.StartDeploymentResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class DataServiceApi implements DataService {

  @NonNull private EnvironmentHandler environmentHandler;

  @Override
  public CreateEnvironmentResponse createEnvironment(final CreateEnvironmentRequest request)
      throws EnvironmentExistsException, InvalidParameterException, ServiceException {

    //TODO: validate request object

    try {
      final Environment environment = EnvironmentTranslator.fromCreateEnvironmnetRequest(request);
      final Environment createdEnvironment = environmentHandler.createEnvironment(environment);
      return EnvironmentTranslator.toCreateEnvironmentResponse(createdEnvironment);

    } catch (final StorageException | IllegalArgumentException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public GetEnvironmentResponse getEnvironment(GetEnvironmentRequest request)
      throws EnvironmentNotFoundException, InvalidParameterException, ServiceException {

    //TODO: validate request object

    try {
      final Environment environment = EnvironmentTranslator.fromGetEnvironmentRequest(request);
      final Environment getEnvironment = environmentHandler.getEnvironment(environment);
      return EnvironmentTranslator.toGetEnvironmentResponse(getEnvironment);

    } catch (final StorageException | IllegalArgumentException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public StartDeploymentResponse startDeployment(StartDeploymentRequest request)
      throws EnvironmentNotFoundException, EnvironmentVersionNotFoundException,
          EnvironmentVersionOutdatedException, InvalidParameterException, ServiceException {

    throw new UnsupportedOperationException("");
  }
}
