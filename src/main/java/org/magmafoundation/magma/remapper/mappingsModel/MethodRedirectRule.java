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

package org.magmafoundation.magma.remapper.mappingsModel;

/**
 * MethodRedirectRule
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 08:57 am
 */
public class MethodRedirectRule {

    private final String owner;
    private final String desc;
    private final String name;
    private final String remapOwner;

    public MethodRedirectRule(String owner, String name, String desc, String remapOwner) {
        this.owner = owner;
        this.desc = desc;
        this.name = name;
        this.remapOwner = remapOwner;
    }

    public String getRemapOwner() {
        return remapOwner;
    }

    public String getOwner() {
        return owner;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }
}
