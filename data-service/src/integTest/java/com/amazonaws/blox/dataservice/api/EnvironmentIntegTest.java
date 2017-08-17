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

import com.amazonaws.blox.dataservice.Application;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentExistsException;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentNotFoundException;
import com.amazonaws.blox.dataservicemodel.v1.exception.InvalidParameterException;
import com.amazonaws.blox.dataservicemodel.v1.exception.ServiceException;
import com.amazonaws.blox.dataservicemodel.v1.model.CreateEnvironmentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.CreateEnvironmentResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.GetEnvironmentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.GetEnvironmentResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.InstanceGroup;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public class EnvironmentIntegTest {

  @Autowired private DataServiceApi dataServiceApi;

  @Test
  public void createAndGetEnvironment()
      throws EnvironmentExistsException, InvalidParameterException, ServiceException,
          EnvironmentNotFoundException {
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
        dataServiceApi.createEnvironment(createEnvironmentRequest);
    final GetEnvironmentRequest getEnvironmentRequest =
        GetEnvironmentRequest.builder()
            .name(createEnvironmentResponse.getName())
            .accountId(createEnvironmentResponse.getAccountId())
            .build();

    final GetEnvironmentResponse getEnvironmentResponse =
        dataServiceApi.getEnvironment(getEnvironmentRequest);
    assertInstanceGroup(
        getEnvironmentResponse.getInstanceGroup(), createEnvironmentRequest.getInstanceGroup());
    assertEquals(getEnvironmentResponse.getName(), createEnvironmentRequest.getName());
    assertEquals(getEnvironmentResponse.getRoleArn(), createEnvironmentRequest.getRoleArn());
    assertEquals(getEnvironmentResponse.getAccountId(), createEnvironmentRequest.getAccountId());
    assertEquals(
        getEnvironmentResponse.getEnvironmentType(), createEnvironmentRequest.getEnvironmentType());
    assertEquals(
        getEnvironmentResponse.getTaskDefinition(), createEnvironmentRequest.getTaskDefinition());
  }

  private void assertInstanceGroup(final InstanceGroup expected, final InstanceGroup result) {
    assertEquals(expected.getClusterArn(), result.getClusterArn());
    if (expected.getAttributes() == null) {
      if (result.getAttributes() != null && !result.getAttributes().isEmpty()) {
        assertEquals(expected.getAttributes(), result.getAttributes());
      } else {
        Assert.fail("Expected attributes to be null or empty");
      }
    }
  }
}
