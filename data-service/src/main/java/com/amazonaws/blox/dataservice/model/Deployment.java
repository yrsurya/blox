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

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Data
@Builder
public class Deployment {

  @NonNull private String deploymentId;
  @NonNull private String environmentName;
  @NonNull private DeploymentType deploymentType;
  @NonNull private Instant createdTime;
  @NonNull private Instant lastUpdatedTime;
  private DeploymentStatus deploymentStatus;
  private Instant startTime;
  private Instant endTime;
}