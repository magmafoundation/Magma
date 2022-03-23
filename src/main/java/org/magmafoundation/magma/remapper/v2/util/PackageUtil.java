package org.magmafoundation.magma.remapper.v2.util;

import java.util.jar.Attributes;
import java.util.jar.Manifest;
import io.netty.util.internal.ConcurrentSet;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PackageUtil {

    private static PackageUtil instance = new PackageUtil();

    private ConcurrentSet<Package> fixedPackages = new ConcurrentSet<>();

    public void fixPackage(Package pkg, Manifest manifest) {
        if (!fixedPackages.contains(pkg)) {
            Attributes attr = manifest.getMainAttributes();
            if (attr != null) {
                try {
                    try {
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.SPECIFICATION_TITLE), "specTitle");
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.SPECIFICATION_VERSION), "specVersion");
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.SPECIFICATION_VENDOR), "specVendor");
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE), "implTitle");
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION), "implVersion");
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR), "implVendor");
                    } catch (Exception ignored) {
                    }
                } finally {
                    fixedPackages.add(pkg);
                }
            }
        }
    }

    public ConcurrentSet<Package> getFixedPackages() {
        return fixedPackages;
    }

    public static PackageUtil getInstance() {
        return instance;
    }
}
