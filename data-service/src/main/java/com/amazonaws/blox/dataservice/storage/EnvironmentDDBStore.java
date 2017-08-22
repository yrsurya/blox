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

import com.amazonaws.AmazonServiceException;
import com.amazonaws.blox.dataservice.model.Environment;
import com.amazonaws.blox.dataservice.model.EnvironmentType;
import com.amazonaws.blox.dataservice.exception.StorageException;
import com.amazonaws.blox.dataservice.storage.model.DaemonEnvironmentDDBRecord;
import com.amazonaws.blox.dataservice.storage.model.DaemonEnvironmentDDBRecordTranslator;
import com.amazonaws.blox.dataservicemodel.v1.exception.EnvironmentNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EnvironmentDDBStore implements EnvironmentStore {

  @NonNull private DynamoDBMapper dynamoDBMapper;

  @Override
  public Environment createDaemonEnvironment(final Environment environment)
      throws StorageException {
    if (!environment.getType().equals(EnvironmentType.Daemon)) {
      throw new IllegalArgumentException(
          String.format(
              "Environment type must be daemon instead of %s", environment.getType().name()));
    }

    //TODO: add environment version record and record latest version

    final DaemonEnvironmentDDBRecord record =
        DaemonEnvironmentDDBRecordTranslator.toDaemonEnvironmentDDBRecord(environment);
    try {
      dynamoDBMapper.save(record);
    } catch (final AmazonServiceException e) {
      throw new StorageException(
          String.format(
              "Could not save record with environment name %s and accountId %s",
              environment.getName(), environment.getAccountId()),
          e);
    }

    return DaemonEnvironmentDDBRecordTranslator.fromDaemonEnvironmentDDBRecord(record);
  }

  @Override
  public Environment getDaemonEnvironment(final String environmentName, final String accountId)
      throws EnvironmentNotFoundException, StorageException {
    //TODO: get latest version
    final String environmentVersion = "1";

    return getDaemonEnvironment(environmentName, accountId, environmentVersion);
  }

  @Override
  public Environment getDaemonEnvironment(
      final String environmentName, final String accountId, final String environmentVersion)
      throws EnvironmentNotFoundException, StorageException {
    try {
      final DaemonEnvironmentDDBRecord record =
          dynamoDBMapper.load(
              DaemonEnvironmentDDBRecord.withHashKeyAndRangeKey(
                  environmentName, accountId, environmentVersion));

      if (record == null) {
        throw new EnvironmentNotFoundException(
            String.format(
                "Environment with name %s, account id %s, and version %s was not found",
                environmentName, accountId, environmentVersion));
      }

      return DaemonEnvironmentDDBRecordTranslator.fromDaemonEnvironmentDDBRecord(record);
    } catch (final AmazonServiceException e) {
      throw new StorageException(
          String.format(
              "Could not load daemon environment with environmentName, accountId and environmentVersion",
              environmentName,
              accountId,
              environmentVersion),
          e);
    }
  }
}
