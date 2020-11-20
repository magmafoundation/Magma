/*
 * Magma Server
 * Copyright (C) 2019-2020.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.magmafoundation.magma.remapper.proxy;

import java.security.ProtectionDomain;
import net.md_5.specialsource.repo.RuntimeRepo;
import org.magmafoundation.magma.remapper.utils.RemappingUtils;

/**
 * DelegateClassLoder
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 20/11/2020 - 04:23 am
 */
public class DelegateClassLoader extends ClassLoader {

    public static final String desc = DelegateClassLoader.class.getName().replace('.', '/');

    protected DelegateClassLoader() {
        super();
    }

    protected DelegateClassLoader(ClassLoader parent) {
        super(parent);
    }

    public final Class<?> defineClassMagma(byte[] b, int off, int len) throws ClassFormatError {
        return defineClassMagma(null, b, off, len, null);
    }

    public final Class<?> defineClassMagma(String name, byte[] b, int off, int len) throws ClassFormatError {
        return defineClassMagma(name, b, off, len, null);
    }

    public final Class<?> defineClassMagma(String name, java.nio.ByteBuffer b, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (!b.isDirect() && b.hasArray()) {
            return remappedFindClass(name, b.array(), protectionDomain);
        }
        return defineClass(name, b, protectionDomain);
    }

    public final Class<?> defineClassMagma(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (off == 0) {
            return remappedFindClass(name, b, protectionDomain);
        }

        return defineClass(name, b, off, len, protectionDomain);
    }

    private Class<?> remappedFindClass(String name, byte[] stream, ProtectionDomain protectionDomain) throws ClassFormatError {
        Class<?> result;
        try {
            byte[] bytecode = RemappingUtils.jarRemapper.remapClassFile(stream, RuntimeRepo.getInstance());
            bytecode = RemappingUtils.remapFindClass(null, null, bytecode);
            result = this.defineClass(name, bytecode, 0, bytecode.length, protectionDomain);
        } catch (Throwable t) {
            throw new ClassFormatError("Failed to remap class " + name);
        }
        return result;
    }
}
