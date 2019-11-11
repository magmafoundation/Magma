package org.magmafoundation.magma.remapper.remappers;

import org.magmafoundation.magma.remapper.proxy.DelegateURLClassLoder;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 * MagmaClassRemapper
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 08:44 am
 */
public class MagmaClassRemapper extends ClassRemapper {

    public MagmaClassRemapper(ClassVisitor cv, Remapper remapper) {
        super(cv, remapper);
    }

    protected MagmaClassRemapper(int api, ClassVisitor cv, Remapper remapper) {
        super(api, cv, remapper);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if ("java/net/URLClassLoader".equals(superName)) {
            superName = DelegateURLClassLoder.desc;
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    protected MethodVisitor createMethodRemapper(MethodVisitor mv) {
        return new ReflectionMethodRemapper(mv, remapper);
    }
}
