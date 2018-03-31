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

package discord4j.store.common;

import org.apache.commons.codec.binary.Base64;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

public class PublicPrivateKeyGenerator {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair keyPair = keyGen.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        System.out.println("Generating key/value pair using RSA algorithm");

        byte[] privateKeyBytes = privateKey.getEncoded();
        byte[] publicKeyBytes = publicKey.getEncoded();

        try (FileOutputStream fos = new FileOutputStream("private.key")) {
            fos.write(Base64.encodeBase64(privateKeyBytes));
        }

        try (FileOutputStream fos = new FileOutputStream("public.key")) {
            fos.write(Base64.encodeBase64(publicKeyBytes));
        }
    }

}
