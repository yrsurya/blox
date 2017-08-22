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

import com.amazonaws.blox.dataservice.model.EnvironmentHealth;
import com.amazonaws.blox.dataservice.model.EnvironmentStatus;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@DynamoDBTable(tableName = "DaemonEnvironments")
@Data
@Builder
//Required for builder because an empty constructor exists
@AllArgsConstructor
public class DaemonEnvironmentDDBRecord {

  public static final String ENVIRONMENT_ACCOUNTID_HASH_KEY = "environmentNameAccountId";
  public static final String ENVIRONMENT_VERSION = "environmentVersion";

  @Deprecated
  public DaemonEnvironmentDDBRecord() {
    // Needed by dynamodb mapper
  }

  public static DaemonEnvironmentDDBRecord withHashKeyAndRangeKey(
      final String environmentName, final String accountId, final String environmentVersion) {

    return DaemonEnvironmentDDBRecord.builder()
        .environmentNameAccountId(createEnvironmentNameAccountId(environmentName, accountId))
        .environmentVersion(environmentVersion)
        .build();
  }

  public static String createEnvironmentNameAccountId(
      final String environmentName, final String accountId) {
    return EnvironmentNameAccountId.createEnvironmentNameAccountId(environmentName, accountId);
  }

  @DynamoDBIgnore
  public String getEnvironmentName() {
    return EnvironmentNameAccountId.getEnvironmentName(environmentNameAccountId);
  }

  @DynamoDBIgnore
  public String getAccountId() {
    return EnvironmentNameAccountId.getAccountId(environmentNameAccountId);
  }

  @DynamoDBHashKey(attributeName = ENVIRONMENT_ACCOUNTID_HASH_KEY)
  private String environmentNameAccountId;

  @DynamoDBRangeKey(attributeName = ENVIRONMENT_VERSION)
  private String environmentVersion;

  @DynamoDBVersionAttribute private Long recordVersion;

  @DynamoDBTypeConverted(converter = InstantDDBConverter.class)
  private Instant createdTime;

  @DynamoDBTypeConverted(converter = InstantDDBConverter.class)
  private Instant lastUpdatedTime;

  private String roleArn;
  private String clusterArn;
  //encoded name:value
  private Set<String> attributes;
  private String taskDefinitionArn;

  @DynamoDBTypeConvertedEnum private EnvironmentStatus status;

  @DynamoDBTypeConvertedEnum private EnvironmentHealth health;
}
