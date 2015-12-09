/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.gateway.pac4j;

import org.apache.hadoop.gateway.i18n.messages.Message;
import org.apache.hadoop.gateway.i18n.messages.MessageLevel;
import org.apache.hadoop.gateway.i18n.messages.Messages;

/**
 * Logging messages for the pac4j provider.
 *
 * @author Jerome Leleu
 * @since 0.8.0
 */
@Messages(logger="org.apache.hadoop.gateway.pac4j")
public interface Pac4jMessages {

  @Message( level = MessageLevel.ERROR, text = "SSO authentication provider url required")
  public void ssoAuthenticationProviderUrlRequired();

  @Message( level = MessageLevel.ERROR, text = "At least one pac4j client must be defined")
  public void atLeastOnePac4jClientMustBeDefined();

  @Message( level = MessageLevel.ERROR, text = "Crypto service, alias service and cluster name required")
  public void cryptoServiceAndAliasServiceAndClusterNameRequired();

  @Message( level = MessageLevel.ERROR, text = "Unable to generate a password for encryption")
  public void unableToGenerateAPasswordForEncryption(Exception e);
}
