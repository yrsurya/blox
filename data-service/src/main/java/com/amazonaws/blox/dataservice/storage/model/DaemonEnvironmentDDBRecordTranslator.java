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

import com.amazonaws.blox.dataservice.model.Environment;
import com.amazonaws.blox.dataservice.model.EnvironmentType;
import com.amazonaws.blox.dataservice.model.InstanceGroup;

//TODO: use object mapping library
public class DaemonEnvironmentDDBRecordTranslator {

  public static DaemonEnvironmentDDBRecord toDaemonEnvironmentDDBRecord(
      final Environment environment) {
    return DaemonEnvironmentDDBRecord.builder()
        .environmentNameAccountId(
            DaemonEnvironmentDDBRecord.createEnvironmentNameAccountId(
                environment.getName(), environment.getAccountId()))
        .environmentVersion(environment.getEnvironmentVersion())
        .taskDefinitionArn(environment.getTaskDefinitionArn())
        .roleArn(environment.getRoleArn())
        .clusterArn(environment.getInstanceGroup().getClusterArn())
        //TODO
        //.attributes(environment.getInstanceGroup().getAttributes())
        .status(environment.getStatus())
        .health(environment.getHealth())
        .createdTime(environment.getCreatedTime())
        .lastUpdatedTime(environment.getLastUpdatedTime())
        .build();
  }

  public static Environment fromDaemonEnvironmentDDBRecord(
      final DaemonEnvironmentDDBRecord record) {
    return Environment.builder()
        .type(EnvironmentType.Daemon)
        .accountId(record.getAccountId())
        .name(record.getEnvironmentName())
        .environmentVersion(record.getEnvironmentVersion())
        .taskDefinitionArn(record.getTaskDefinitionArn())
        .roleArn(record.getRoleArn())
        .instanceGroup(
            InstanceGroup.builder()
                .clusterArn(record.getClusterArn())
                //TODO
                .attributes(null)
                .build())
        .status(record.getStatus())
        .health(record.getHealth())
        .createdTime(record.getCreatedTime())
        .lastUpdatedTime(record.getLastUpdatedTime())
        .build();
  }
}
