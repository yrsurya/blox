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
package com.amazonaws.blox.dataservice.model;

import com.amazonaws.blox.dataservicemodel.v1.model.CreateEnvironmentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.CreateEnvironmentResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.GetEnvironmentRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.GetEnvironmentResponse;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO: use object mapping library
public class EnvironmentTranslator {

  public static Environment fromCreateEnvironmnetRequest(final CreateEnvironmentRequest request) {
    return Environment.builder()
        .accountId(request.getAccountId())
        .name(request.getName())
        .taskDefinitionArn(request.getTaskDefinition())
        .roleArn(request.getRoleArn())
        .instanceGroup(
            InstanceGroup.builder()
                .clusterArn(request.getInstanceGroup().getClusterArn())
                .attributes(
                    Optional.ofNullable(request.getInstanceGroup().getAttributes())
                        .orElse(Collections.emptySet())
                        .stream()
                        .map(
                            attribute ->
                                Attribute.builder()
                                    .name(attribute.getName())
                                    .value(attribute.getValue())
                                    .build())
                        .collect(Collectors.toSet()))
                .build())
        .type(toEnvironmentType(request.getEnvironmentType()))
        .build();
  }

  private static EnvironmentType toEnvironmentType(final String requestEnvironmentType) {
    if (requestEnvironmentType.equalsIgnoreCase(EnvironmentType.Daemon.name())) {
      return EnvironmentType.Daemon;
    } else {
      throw new IllegalArgumentException(
          String.format("Environment type %s cannot be translated", requestEnvironmentType));
    }
  }

  public static CreateEnvironmentResponse toCreateEnvironmentResponse(
      final Environment environment) {
    return CreateEnvironmentResponse.builder()
        .environmentVersion(environment.getEnvironmentVersion())
        .name(environment.getName())
        .accountId(environment.getAccountId())
        .taskDefinition(environment.getTaskDefinitionArn())
        .roleArn(environment.getRoleArn())
        .instanceGroup(
            com.amazonaws.blox.dataservicemodel.v1.model.InstanceGroup.builder()
                .clusterArn(environment.getInstanceGroup().getClusterArn())
                .attributes(
                    Optional.ofNullable(environment.getInstanceGroup().getAttributes())
                        .orElse(Collections.emptySet())
                        .stream()
                        .map(
                            attribute ->
                                com.amazonaws.blox.dataservicemodel.v1.model.Attribute.builder()
                                    .name(attribute.getName())
                                    .value(attribute.getValue())
                                    .build())
                        .collect(Collectors.toSet()))
                .build())
        .type(environment.getType().name())
        .status(environment.getStatus().name())
        .health(environment.getHealth().name())
        .createdTime(environment.getCreatedTime())
        .lastUpdatedTime(environment.getLastUpdatedTime())
        .build();
  }

  public static Environment fromGetEnvironmentRequest(final GetEnvironmentRequest request) {
    return Environment.builder().name(request.getName()).accountId(request.getAccountId()).build();
  }

  public static GetEnvironmentResponse toGetEnvironmentResponse(final Environment environment) {
    return GetEnvironmentResponse.builder()
        .environmentVersion(environment.getEnvironmentVersion())
        .name(environment.getName())
        .accountId(environment.getAccountId())
        .taskDefinition(environment.getTaskDefinitionArn())
        .roleArn(environment.getRoleArn())
        .instanceGroup(
            com.amazonaws.blox.dataservicemodel.v1.model.InstanceGroup.builder()
                .clusterArn(environment.getInstanceGroup().getClusterArn())
                .attributes(
                    Optional.ofNullable(environment.getInstanceGroup().getAttributes())
                        .orElse(Collections.emptySet())
                        .stream()
                        .map(
                            attribute ->
                                com.amazonaws.blox.dataservicemodel.v1.model.Attribute.builder()
                                    .name(attribute.getName())
                                    .value(attribute.getValue())
                                    .build())
                        .collect(Collectors.toSet()))
                .build())
        .environmentType(environment.getType().name())
        .status(environment.getStatus().name())
        .health(environment.getHealth().name())
        .createdTime(environment.getCreatedTime())
        .lastUpdatedTime(environment.getLastUpdatedTime())
        .build();
  }
}
