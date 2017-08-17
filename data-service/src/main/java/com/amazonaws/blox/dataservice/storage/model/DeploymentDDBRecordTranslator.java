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
package com.amazonaws.blox.dataservice.storage.model;

import com.amazonaws.blox.dataservice.model.Deployment;

public class DeploymentDDBRecordTranslator {

  public static DeploymentDDBRecord toDeploymentDDBRecord(final Deployment deployment) {
    return DeploymentDDBRecord.builder()
        .deploymentId(deployment.getDeploymentId())
        .deploymentStatus(deployment.getDeploymentStatus())
        .environmentName(deployment.getEnvironmentName())
        .deploymentType(deployment.getDeploymentType())
        .createdTime(deployment.getCreatedTime())
        .lastUpdatedTime(deployment.getLastUpdatedTime())
        .startTime(deployment.getStartTime())
        .endTime(deployment.getEndTime())
        .build();
  }

  public static DeploymentDDBRecord updateDeploymentDDBRecord(
      final Deployment deployment, final DeploymentDDBRecord record) {
    record.setDeploymentStatus(deployment.getDeploymentStatus());
    record.setEndTime(deployment.getEndTime());
    record.setLastUpdatedTime(deployment.getLastUpdatedTime());
    return record;
  }

  public static Deployment fromDeploymentDDBRecord(final DeploymentDDBRecord record) {
    return Deployment.builder()
        .deploymentId(record.getDeploymentId())
        .deploymentStatus(record.getDeploymentStatus())
        .environmentName(record.getEnvironmentName())
        .deploymentType(record.getDeploymentType())
        .createdTime(record.getCreatedTime())
        .lastUpdatedTime(record.getLastUpdatedTime())
        .startTime(record.getStartTime())
        .endTime(record.getEndTime())
        .build();
  }
}
