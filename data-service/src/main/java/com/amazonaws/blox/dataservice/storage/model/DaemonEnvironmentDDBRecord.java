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

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@DynamoDBTable(tableName = "DaemonEnvironments")
@Data
@Builder
//Required for builder because an empty constructor exists
@AllArgsConstructor
public class DaemonEnvironmentDDBRecord {

  public static final String ACCOUNT_ID_HASH_KEY = "accountId";
  public static final String ENVIRONMENT_NAME_SORT_KEY = "environmentName";

  @Deprecated
  public DaemonEnvironmentDDBRecord() {
    // Needed by dynamodb mapper
  }

  @DynamoDBHashKey(attributeName = ACCOUNT_ID_HASH_KEY)
  private String accountId;

  @DynamoDBRangeKey(attributeName = ENVIRONMENT_NAME_SORT_KEY)
  private String environmentName;

  @DynamoDBVersionAttribute private Long version;

  @DynamoDBTypeConverted(converter = InstantDDBConverter.class)
  private Instant createdTime;

  @DynamoDBTypeConverted(converter = InstantDDBConverter.class)
  private Instant lastUpdatedTime;

  private String roleArn;
  private String clusterArn;
  //encoded name:value
  private Set<String> attributes;
  private String taskDefinitionArn;

  private String status;
  private String health;
}
