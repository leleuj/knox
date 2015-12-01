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
package org.apache.hadoop.gateway.pac4j.session;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.gateway.services.security.CryptoService;
import org.apache.hadoop.gateway.services.security.EncryptionResult;
import org.pac4j.core.context.ContextHelper;
import org.pac4j.core.context.Cookie;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.util.JavaSerializationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class KnoxSessionStore implements SessionStore {

    private final static Logger logger = LoggerFactory.getLogger(KnoxSessionStore.class);

    public final static String PAC4J_PASSWORD = "pac4j.password";

    private final static String PAC4J_SESSION_PREFIX = "pac4j.session.";

    private final JavaSerializationHelper javaSerializationHelper;

    private final CryptoService cryptoService;

    private final String clusterName;

    public KnoxSessionStore(final CryptoService cryptoService, final String clusterName) {
        javaSerializationHelper = new JavaSerializationHelper();
        this.cryptoService = cryptoService;
        this.clusterName = clusterName;
    }

    public String getOrCreateSessionId(WebContext context) {
        return null;
    }

    private Serializable decryptBase64(final String v) {
        byte[] bytes = Base64.decodeBase64(v);
        EncryptionResult result = EncryptionResult.fromByteArray(bytes);
        byte[] clear = cryptoService.decryptForCluster(this.clusterName,
                PAC4J_PASSWORD,
                result.cipher,
                result.iv,
                result.salt);
        if (clear != null) {
            return javaSerializationHelper.unserializeFromBytes(clear);
        }
        return null;
    }

    public Object get(WebContext context, String key) {
        final Cookie cookie = ContextHelper.getCookie(context, PAC4J_SESSION_PREFIX + key);
        Object value = null;
        if (cookie != null) {
            value = decryptBase64(cookie.getValue());
        }
        logger.debug("Get from session: {} = {}", key, value);
        return value;
    }

    private String encryptBase64(final Object o) {
        final byte[] bytes = javaSerializationHelper.serializeToBytes((Serializable) o);
        EncryptionResult result = cryptoService.encryptForCluster(this.clusterName, PAC4J_PASSWORD, bytes);
        return Base64.encodeBase64String(result.toByteAray());
    }

    public void set(WebContext context, String key, Object value) {
        logger.debug("Save in session: {} = {}", key, value);
        final Cookie cookie = new Cookie(PAC4J_SESSION_PREFIX + key, encryptBase64(value));
        cookie.setDomain(context.getServerName());
        cookie.setHttpOnly(true);
        cookie.setSecure(ContextHelper.isHttpsOrSecure(context));
        context.addResponseCookie(cookie);
    }
}
