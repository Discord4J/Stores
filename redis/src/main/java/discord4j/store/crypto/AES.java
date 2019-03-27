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

package discord4j.store.crypto;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public abstract class AES {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CIPHER = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 128 / 8;

    public static SecretKey getSecretKey(String keyFile) throws IOException {
        byte[] keyb = Files.readAllBytes(Paths.get(keyFile));
        return new SecretKeySpec(keyb, "AES");
    }

    public static byte[] encrypt(byte[] plainBytes, SecretKey secretKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        byte[] iv = new byte[IV_LENGTH];
        RANDOM.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher ci = Cipher.getInstance(CIPHER);
        ci.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        return combine(iv, ci.doFinal(plainBytes));
    }

    private static byte[] combine(byte[] first, byte[] second) {
        byte[] combined = new byte[first.length + second.length];
        System.arraycopy(first, 0, combined, 0, first.length);
        System.arraycopy(second, 0, combined, first.length, second.length);
        return combined;
    }

    public static byte[] decrypt(byte[] cipherBytes, SecretKey secretKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        byte[] iv = new byte[IV_LENGTH];
        byte[] rest = new byte[cipherBytes.length - IV_LENGTH];
        System.arraycopy(cipherBytes, 0, iv, 0, iv.length);
        System.arraycopy(cipherBytes, IV_LENGTH, rest, 0, rest.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher ci = Cipher.getInstance(CIPHER);
        ci.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        return ci.doFinal(rest);
    }
}
