package org.magmafoundation.magma.patcher.imlp;

import org.magmafoundation.magma.patcher.Patcher;
import org.magmafoundation.magma.patcher.Patcher.PatcherInfo;

/**
 * DummyPatch
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 05/03/2020 - 08:53 pm
 */
@PatcherInfo(name = "dummy", description = "")
public class DummyPatch extends Patcher {

    @Override
    public byte[] transform(String className, byte[] clazz) {
        return new byte[0];
    }
}
