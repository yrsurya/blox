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

import com.amazonaws.blox.dataservice.model.DeploymentStatus;
import com.amazonaws.blox.dataservice.model.DeploymentType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@DynamoDBTable(tableName = "Deployments")
@Data
@Builder
//Required for builder because an empty constructor exists
@AllArgsConstructor
public class DeploymentDDBRecord {

  public static final String DEPLOYMENT_ID_HASH_KEY = "deploymentId";
  public static final String DEPLOYMENT_STATUS_GSI_NAME =
      "deploymentStatus-environmentName-index-copy";
  public static final String DEPLOYMENT_STATUS_INDEX_HASH_KEY = "deploymentStatus";
  public static final String DEPLOYMENT_STATUS_INDEX_SORT_KEY = "environmentName";

  @Deprecated
  public DeploymentDDBRecord() {
    // Needed by dynamodb mapper
  }

  public static DeploymentDDBRecord withHashKey(final String deploymentId) {
    return DeploymentDDBRecord.builder().deploymentId(deploymentId).build();
  }

  public static DeploymentDDBRecord withIndexHashKey(final DeploymentStatus deploymentStatus) {
    return DeploymentDDBRecord.builder().deploymentStatus(deploymentStatus).build();
  }

  @DynamoDBHashKey(attributeName = DEPLOYMENT_ID_HASH_KEY)
  private String deploymentId;

  @DynamoDBIndexHashKey(
    attributeName = DEPLOYMENT_STATUS_INDEX_HASH_KEY,
    globalSecondaryIndexName = DEPLOYMENT_STATUS_GSI_NAME
  )
  @DynamoDBTypeConvertedEnum
  private DeploymentStatus deploymentStatus;

  @DynamoDBIndexRangeKey(
    attributeName = DEPLOYMENT_STATUS_INDEX_SORT_KEY,
    globalSecondaryIndexName = DEPLOYMENT_STATUS_GSI_NAME
  )
  private String environmentName;

  @DynamoDBTypeConvertedEnum private DeploymentType deploymentType;

  @DynamoDBTypeConverted(converter = InstantDDBConverter.class)
  private Instant createdTime;

  @DynamoDBTypeConverted(converter = InstantDDBConverter.class)
  private Instant lastUpdatedTime;

  @DynamoDBTypeConverted(converter = InstantDDBConverter.class)
  private Instant startTime;

  @DynamoDBTypeConverted(converter = InstantDDBConverter.class)
  private Instant endTime;

  @DynamoDBVersionAttribute private Long version;
}
