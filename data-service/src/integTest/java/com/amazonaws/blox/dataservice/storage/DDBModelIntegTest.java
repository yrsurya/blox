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
package com.amazonaws.blox.dataservice.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.blox.dataservice.model.Deployment;
import com.amazonaws.blox.dataservice.model.DeploymentStatus;
import com.amazonaws.blox.dataservice.model.DeploymentType;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class DDBModelIntegTest {

  private static final String CREDENTIALS_PROFILE_NAME = "daemon_prototype";
  private DeploymentDDBStore deploymentDDBStore;

  @Before
  public void setup() {
    AmazonDynamoDB dynamoDBClient =
        AmazonDynamoDBClient.builder()
            .withCredentials(new ProfileCredentialsProvider(CREDENTIALS_PROFILE_NAME))
            .build();
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
    deploymentDDBStore = new DeploymentDDBStore(dynamoDBMapper);
  }

  @Test
  public void testSparseDeploymentStatusIndex() {
    deploymentDDBStore.deleteAllDeployments();

    Deployment deployment = createDeployment();
    deploymentDDBStore.createDeployment(deployment);

    List<Deployment> getByStatus =
        deploymentDDBStore.getDeploymentsByStatus(DeploymentStatus.Pending);
    assertEquals(1, getByStatus.size());
    assertEquals(deployment, getByStatus.get(0));

    final Deployment secondDeployment = createDeployment();
    deploymentDDBStore.createDeployment(secondDeployment);

    getByStatus = deploymentDDBStore.getDeploymentsByStatus(DeploymentStatus.Pending);
    assertEquals(2, getByStatus.size());
    assertNotEquals(getByStatus.get(0).getDeploymentId(), getByStatus.get(1).getDeploymentId());

    // removing status should remove item from the index since it's indexed on status
    deployment = deploymentDDBStore.getDeploymentById(deployment.getDeploymentId());
    deployment.setDeploymentStatus(null);
    deploymentDDBStore.updateDeployment(deployment);
    deployment = deploymentDDBStore.getDeploymentById(deployment.getDeploymentId());
    assertNull(deployment.getDeploymentStatus());

    List<Deployment> statusShouldBeRemoved = deploymentDDBStore.getAllDeploymentsInIndex();
    assertEquals(1, statusShouldBeRemoved.size());
    assertEquals(secondDeployment, statusShouldBeRemoved.get(0));
  }

  private Deployment createDeployment() {
    return Deployment.builder()
        .deploymentId(UUID.randomUUID().toString())
        .environmentName("test")
        .createdTime(Instant.now())
        .deploymentType(DeploymentType.UserInitiated)
        .lastUpdatedTime(Instant.now())
        .deploymentStatus(DeploymentStatus.Pending)
        .build();
  }
}
