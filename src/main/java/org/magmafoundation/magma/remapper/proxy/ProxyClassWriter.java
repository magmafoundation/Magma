package org.magmafoundation.magma.remapper.proxy;

import org.magmafoundation.magma.remapper.utils.RemappingUtils;

import java.io.IOException;

/**
 * ProxyClassWriter
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 08:50 am
 */
public class ProxyClassWriter {

    public static byte[] remapClass(byte[] code) {
        try {
            return RemappingUtils.remapFindClass(null, null, code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
