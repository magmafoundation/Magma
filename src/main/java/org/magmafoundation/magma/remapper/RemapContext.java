package org.magmafoundation.magma.remapper;

import java.util.LinkedList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.objectweb.asm.tree.ClassNode;

/**
 * RemapContext
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 07:57 am
 */
public class RemapContext {

    private static final LinkedList<RemapContext> remapStack = new LinkedList<>();

    private ClassNode classNode;
    private PluginDescriptionFile description;

    public static LinkedList<RemapContext> getRemapStack() {
        return remapStack;
    }

    public static void push(RemapContext context) {
        remapStack.push(context);
    }

    public static RemapContext peek() {
        return remapStack.peek();
    }

    public static RemapContext pop() {
        return remapStack.pop();
    }

    public PluginDescriptionFile getDescription() {
        return description;
    }

    public RemapContext setDescription(PluginDescriptionFile description) {
        this.description = description;
        return this;
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    public RemapContext setClassNode(ClassNode classNode) {
        this.classNode = classNode;
        return this;
    }
}
