package org.bukkit.craftbukkit.inventory;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemFactory;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class ItemFactoryTest extends AbstractTestingBase {

    @Test
    public void testKnownAttributes() throws Throwable {
        final Collection<String> names = new HashSet<>();
        File classesDirectory = new File(CommandBase.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        Collection<Class> allClassFiles = getMinecraftServerClasses(getAllClassesFilesFromDir(classesDirectory));
        for (Class clazz : allClassFiles) {
            assertThat(clazz.getName(), clazz, is(not(nullValue())));
            for (final Field field : clazz.getDeclaredFields()) {
                if (IAttribute.class.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    final String attributeName = ((IAttribute) field.get(null)).getName();
                    assertThat("Logical error: duplicate name `" + attributeName + "' in " + clazz.getName(), names.add(attributeName), is(true));
                    assertThat(clazz.getName(), CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES, hasItem(attributeName));
                }
            }
        }
        assertThat("Extra values detected", CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES, is(names));
    }

    private Collection<File> getAllClassesFilesFromDir(File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalStateException("File " + directory.getName() + " does not exist or is not a directory!");
        }
        Collection<File> allClasses = new HashSet<>();
        for (File fileInDir : directory.listFiles()) {
            if (fileInDir.isDirectory()) {
                allClasses.addAll(this.getAllClassesFilesFromDir(fileInDir));
            } else if (fileInDir.getName().endsWith(".class")) {
                allClasses.add(fileInDir);
            }
        }
        return allClasses;
    }

    private Collection<Class> getMinecraftServerClasses(Collection<File> files) {
        Collection<Class> minecraftClasses = new ArrayList<>();
        String essentialClassPathElement = File.separator + "net" + File.separator + "minecraft" + File.separator;
        files.stream().filter(file -> file.getPath().contains(essentialClassPathElement)).forEach(minecraftClassFile -> {
            String absoluteClassPath = minecraftClassFile.getPath().replace(File.separator, ".");
            int from = absoluteClassPath.indexOf("net.minecraft.");
            int to = absoluteClassPath.length() - ".class".length();
            Class serverClass;
            try {
                serverClass = Class.forName(absoluteClassPath.substring(from, to));
            } catch (Throwable ignoreClientClass) {
                return;
            }
            minecraftClasses.add(serverClass);
        });
        return minecraftClasses;
    }
}
