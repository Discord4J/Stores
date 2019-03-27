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

import org.junit.Test;

import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class EncryptDecryptTest {

    @Test
    public void testEncryptDecryptStringUsingAES() throws NoSuchAlgorithmException, IllegalBlockSizeException,
            InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecretKey skey = kgen.generateKey();
        String input = "       ....                               .           \n" +
                "   .xH888888Hx.        .x~~\"*D3j.     .x88888x.       \n" +
                " .H8888888888888:     d8Nu.  9888c   :8**888888X.  :> \n" +
                " 888*\"\"\"?\"\"*88888X    88888  98888   f    `888888x./  \n" +
                "'f     d8x.   ^%88k   \"***\"  9888%  '       `*88888~  \n" +
                "'>    <88888X   '?8        ..@8*\"    \\.    .  `?)X.   \n" +
                " `:..:`888888>    8>    ````\"8D3j     `~=-^   X88> ~  \n" +
                "        `\"*88     X    ..    ?8888L          X8888  ~ \n" +
                "   .xHHhx..\"      !  :@88N   '8888N          488888   \n" +
                "  X88888888hx. ..!   *8888~  '8888F  .xx.     88888X  \n" +
                " !   \"*888888888\"    '*8\"`   9888%  '*8888.   '88888> \n" +
                "        ^\"***\"`        `~===*%\"`      88888    '8888> \n" +
                "                                      `8888>    `888  \n" +
                "                                       \"8888     8%   \n" +
                "                                        `\"888x:-\"     ";
        assertEquals(input, new String(AES.decrypt(AES.encrypt(input.getBytes(), skey), skey)));
    }
}
