package org.magmafoundation.magma.remapper.inter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 * ClassRemapperSupplier
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 17/06/2018 - 07:29 am
 */
public interface ClassRemapperSupplier {

    default ClassRemapper getClassRemapper(ClassVisitor classVisitor) {
        return new ClassRemapper(classVisitor, (Remapper) this);
    }

}
