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

package org.magmafoundation.magma.remapper.utils;

/**
 * ReflectionUtils
 *
 * Move into its own class due to not loading correctly with plugins
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/06/2020 - 11:10 pm
 */
public class ReflectionUtils {

    private static final SecurityManager securityManager = new SecurityManager();

    public static Class<?> getCallerClass(int skip) {
        return securityManager.getCallerClass(skip);
    }

    public static ClassLoader getCallerClassLoader() {
        return ReflectionUtils.getCallerClass(3).getClassLoader();
    }

    static class SecurityManager extends java.lang.SecurityManager {

        public Class<?> getCallerClass(int skip) {
            return getClassContext()[skip + 1];
        }
    }
}
