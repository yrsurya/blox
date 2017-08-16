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
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@DynamoDBTable(tableName = "DaemonEnvironmentLatestVersion")
@Data
@Builder
//Required for builder because an empty constructor exists
@AllArgsConstructor
public class DaemonEnvironmentLatestVersionDDBRecord {

  public static final String ENVIRONMENT_NAME_HASH_KEY = "environmentName";
  public static final String ENVIRONMENT_VERSION_SORT_KEY = "environmentVersion";

  @Deprecated
  public DaemonEnvironmentLatestVersionDDBRecord() {
    // Needed by dynamodb mapper
  }

  @DynamoDBHashKey(attributeName = ENVIRONMENT_NAME_HASH_KEY)
  private String environmentName;

  @DynamoDBRangeKey(attributeName = ENVIRONMENT_VERSION_SORT_KEY)
  private String environmentVersion;

  @DynamoDBVersionAttribute private Long version;
}
