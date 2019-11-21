package org.magmafoundation.magma.remapper.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.specialsource.transformer.MavenShade;
import org.bukkit.plugin.PluginDescriptionFile;
import org.magmafoundation.magma.Magma;
import org.magmafoundation.magma.remapper.RemapContext;
import org.magmafoundation.magma.remapper.inter.ClassRemapperSupplier;
import org.magmafoundation.magma.remapper.mappingsModel.ClassMappings;
import org.magmafoundation.magma.remapper.remappers.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;
import sun.reflect.Reflection;

/**
 * RemappingUtils
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 17/06/2018 - 07:25 am
 */
public class RemappingUtils {

    public static final String nmsPrefix = "net.minecraft.server.";
    public static final MagmaJarMapping jarMapping;
    private static final List<Remapper> remappers = new ArrayList<>();

    static {
        jarMapping = new MagmaJarMapping();
        jarMapping.packages.put("org/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil/", "it/unimi/dsi/fastutil/");
        jarMapping.packages.put("org/bukkit/craftbukkit/libs/jline/", "jline/");
        jarMapping.packages.put("org/bukkit/craftbukkit/libs/joptsimple/", "joptsimple/");
        jarMapping.registerMethodMapping("org/bukkit/Bukkit", "getOnlinePlayers", "()[Lorg/bukkit/entity/Player;", "org/bukkit/Bukkit", "_INVALID_getOnlinePlayers", "()[Lorg/bukkit/entity/Player;");
        jarMapping.registerMethodMapping("org/bukkit/Server", "getOnlinePlayers", "()[Lorg/bukkit/entity/Player;", "org/bukkit/Server", "_INVALID_getOnlinePlayers", "()[Lorg/bukkit/entity/Player;");
        jarMapping.registerMethodMapping("org/bukkit/craftbukkit/" + Magma.getBukkitVersion() + "/CraftServer", "getOnlinePlayers", "()[Lorg/bukkit/entity/Player;", "org/bukkit/craftbukkit/" + Magma.getBukkitVersion() + "/CraftServer", "_INVALID_getOnlinePlayers", "()[Lorg/bukkit/entity/Player;");
        jarMapping.setInheritanceMap(new MagmaInheritanceMap());
        jarMapping.setFallbackInheritanceProvider(new MagmaInheritanceProvider());


        Map<String, String> relocations = new HashMap<String, String>();
        relocations.put("net.minecraft.server", "net.minecraft.server." + Magma.getBukkitVersion());
        try {
            jarMapping.loadMappings(new BufferedReader(new InputStreamReader(Magma.class.getClassLoader().getResourceAsStream("mappings/NMSMappings.srg"))), new MavenShade(relocations), null, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        remappers.add(new NMSVersionRemapper());
        MagmaJarRemapper jarRemapper = new MagmaJarRemapper(jarMapping);
        remappers.add(jarRemapper);
        remappers.add(new ReflectionRemapper());
        jarMapping.initFastMethodMapping(jarRemapper);
    }

    private static final Object remapLock = new Object();

    public static byte[] remapFindClass(PluginDescriptionFile description, String name, byte[] bs) throws IOException {
        synchronized (remapLock) {
            ClassNode classNode = new ClassNode();
            new ClassReader(bs).accept(classNode, ClassReader.EXPAND_FRAMES);
            for (Remapper remapper : remappers) {
                if (description != null && remapper instanceof NMSVersionRemapper) {
                    continue;
                }
                try {
                    RemapContext.push(new RemapContext().setClassNode(classNode).setDescription(description));
                    ClassNode container = new ClassNode();
                    ClassRemapper classRemapper;
                    if (remapper instanceof ClassRemapperSupplier) {
                        classRemapper = ((ClassRemapperSupplier) remapper).getClassRemapper(container);
                    } else {
                        classRemapper = new ClassRemapper(container, remapper);
                    }
                    classNode.accept(classRemapper);
                    classNode = container;
                } finally {
                    RemapContext.pop();
                }
            }
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            bs = writer.toByteArray();
            return bs;
        }
    }

    public static String map(String typeName) {
        typeName = mapPackage(typeName);
        return jarMapping.classes.getOrDefault(typeName, typeName);
    }

    public static String reverseMap(String typeName) {
        ClassMappings mapping = jarMapping.byNMSInternalName.get(typeName);
        return mapping == null ? typeName : mapping.getNmsSrcName();
    }

    public static String reverseMap(Class clazz) {
        ClassMappings mapping = jarMapping.byMCPName.get(clazz.getName());
        return mapping == null ? ASMUtils.toInternalName(clazz) : mapping.getNmsSrcName();
    }

    public static String mapPackage(String typeName) {
        for (Map.Entry<String, String> entry : jarMapping.packages.entrySet()) {
            String prefix = entry.getKey();
            if (typeName.startsWith(prefix)) {
                return entry.getValue() + typeName.substring(prefix.length());
            }
        }
        return typeName;
    }

    public static String remapMethodDesc(String methodDescriptor) {
        Type rt = Type.getReturnType(methodDescriptor);
        Type[] ts = Type.getArgumentTypes(methodDescriptor);
        rt = Type.getType(ASMUtils.toDescriptorV2(map(ASMUtils.getInternalName(rt))));
        for (int i = 0; i < ts.length; i++) {
            ts[i] = Type.getType(ASMUtils.toDescriptorV2(map(ASMUtils.getInternalName(ts[i]))));
        }
        return Type.getMethodType(rt, ts).getDescriptor();
    }

    public static String mapMethodName(Class clazz, String name, MethodType methodType) {
        return mapMethodName(clazz, name, methodType.parameterArray());
    }

    public static String mapMethodName(Class type, String name, Class<?>... parameterTypes) {
        return jarMapping.fastMapMethodName(type, name, parameterTypes);
    }

    public static String inverseMapMethodName(Class type, String name, Class<?>... parameterTypes) {
        return jarMapping.fastReverseMapMethodName(type, name, parameterTypes);
    }

    public static String mapFieldName(Class type, String fieldName) {
        return jarMapping.fastMapFieldName(type, fieldName);
    }

    public static String inverseMapFieldName(Class type, String fieldName) {
        return jarMapping.fastReverseMapFieldName(type, fieldName);
    }

    public static ClassLoader getCallerClassLoder() {
        return Reflection.getCallerClass(3).getClassLoader();
    }

    public static String inverseMapName(Class clazz) {
        ClassMappings mapping = jarMapping.byMCPName.get(clazz.getName());
        return mapping == null ? clazz.getName() : mapping.getNmsName();
    }

    public static String inverseMapSimpleName(Class clazz) {
        ClassMappings mapping = jarMapping.byMCPName.get(clazz.getName());
        return mapping == null ? clazz.getSimpleName() : mapping.getNmsSimpleName();
    }
}
