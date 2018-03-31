/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J. If not, see <http://www.gnu.org/licenses/>.
 */

package discord4j.store.redis;

import discord4j.store.common.RSA;
import io.lettuce.core.codec.RedisCodec;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SecureSerializedObjectCodec implements RedisCodec<Object, Object> {

    private static final Logger log = Loggers.getLogger(SecureSerializedObjectCodec.class);

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public SecureSerializedObjectCodec(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @Override
    public Object decodeKey(ByteBuffer bytes) {
        try {
            byte[] array = new byte[bytes.remaining()];
            bytes.get(array);
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(array));
            return is.readObject();
        } catch (Exception e) {
            log.warn("Could not decode key", e);
            return null;
        }
    }

    @Override
    public Object decodeValue(ByteBuffer bytes) {
        try {
            byte[] array = new byte[bytes.remaining()];
            bytes.get(array);
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(RSA.decrypt(array, privateKey)));
            return is.readObject();
        } catch (Exception e) {
            log.warn("Could not decode value", e);
            return null;
        }
    }

    @Override
    public ByteBuffer encodeKey(Object key) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bytes);
            os.writeObject(key);
            return ByteBuffer.wrap(bytes.toByteArray());
        } catch (IOException e) {
            log.warn("Could not encode key", e);
            return null;
        }
    }

    @Override
    public ByteBuffer encodeValue(Object value) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bytes);
            os.writeObject(value);
            return ByteBuffer.wrap(RSA.encrypt(bytes.toByteArray(), publicKey));
        } catch (IOException | GeneralSecurityException e) {
            log.warn("Could not encode value", e);
            return null;
        }
    }
}
