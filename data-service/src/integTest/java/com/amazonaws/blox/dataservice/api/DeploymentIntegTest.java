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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.amazonaws.blox.dataservice.Application;
import com.amazonaws.blox.dataservice.model.DeploymentStatus;
import com.amazonaws.blox.dataservice.model.DeploymentType;
import com.amazonaws.blox.dataservicemodel.v1.client.DataService;
import com.amazonaws.blox.dataservicemodel.v1.exception.DeploymentDoesNotExist;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentExistsException;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentNotFoundException;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentVersionOutdatedException;
import com.amazonaws.blox.dataservicemodel.v1.exception.InvalidParameterException;
import com.amazonaws.blox.dataservicemodel.v1.exception.ServiceException;
import com.amazonaws.blox.dataservicemodel.v1.model.CreateEnvironmentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.CreateEnvironmentResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.GetDeploymentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.GetDeploymentResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.InstanceGroup;
import com.amazonaws.blox.dataservicemodel.v1.model.StartDeploymentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.StartDeploymentResponse;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public class DeploymentIntegTest {

  @Autowired private DataService dataService;

  @Test
  public void createAndGetDeployment()
      throws EnvironmentExistsException, EnvironmentNotFoundException,
          EnvironmentVersionOutdatedException, InvalidParameterException, ServiceException,
          DeploymentDoesNotExist {

    final CreateEnvironmentRequest createEnvironmentRequest =
        CreateEnvironmentRequest.builder()
            .accountId("1234")
            .environmentType("Daemon")
            .instanceGroup(InstanceGroup.builder().clusterArn("cluster1").build())
            .name("env" + UUID.randomUUID())
            .roleArn("role1")
            .taskDefinition("task1")
            .build();

    final CreateEnvironmentResponse createEnvironmentResponse =
        dataService.createEnvironment(createEnvironmentRequest);

    final StartDeploymentRequest startDeploymentRequest =
        StartDeploymentRequest.builder()
            .environmentName(createEnvironmentResponse.getName())
            .accountId(createEnvironmentResponse.getAccountId())
            .environmentVersion(createEnvironmentResponse.getEnvironmentVersion())
            .build();
    final StartDeploymentResponse startDeploymentResponse =
        dataService.startDeployment(startDeploymentRequest);

    final GetDeploymentRequest getDeploymentRequest =
        GetDeploymentRequest.builder()
            .deploymentId(startDeploymentResponse.getDeploymentId())
            .build();
    final GetDeploymentResponse getDeploymentResponse =
        dataService.getDeployment(getDeploymentRequest);

    assertEquals(
        startDeploymentResponse.getDeploymentId(), getDeploymentResponse.getDeploymentId());
    assertEquals(
        startDeploymentResponse.getEnvironmentName(), getDeploymentResponse.getEnvironmentName());
    assertEquals(
        startDeploymentResponse.getEnvironmentVersion(),
        getDeploymentResponse.getEnvironmentVersion());
    assertEquals(createEnvironmentResponse.getAccountId(), getDeploymentResponse.getAccountId());
    assertEquals(DeploymentStatus.Pending.name(), getDeploymentResponse.getDeploymentStatus());
    assertEquals(DeploymentType.UserInitiated.name(), getDeploymentResponse.getDeploymentType());
    assertNotNull(getDeploymentResponse.getCreatedTime());
  }
}
