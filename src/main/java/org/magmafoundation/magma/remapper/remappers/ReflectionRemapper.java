package org.magmafoundation.magma.remapper.remappers;

import org.magmafoundation.magma.remapper.inter.ClassRemapperSupplier;
import org.magmafoundation.magma.remapper.utils.ASMUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 * ReflectionRemapper
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 08:43 am
 */
public class ReflectionRemapper extends Remapper implements ClassRemapperSupplier {

    @Override
    public ClassRemapper getClassRemapper(ClassVisitor classWriter) {
        return new MagmaClassRemapper(classWriter, this);
    }

    @Override
    public String mapSignature(String signature, boolean typeSignature) {
        if (ASMUtils.isValidSingnature(signature)) {
            return super.mapSignature(signature, typeSignature);
        } else {
            return signature;
        }
    }
}

