package org.magmafoundation.magma.patcher;

import com.google.common.reflect.Reflection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.fml.common.FMLLog;
import org.reflections.Reflections;

/**
 * PatcherManager
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 05/03/2020 - 08:52 pm
 */
public class PatcherManager {

    private List<Patcher> patcherList = new ArrayList<>();


    public void init() {
        initPatches();
        FMLLog.info("%s patches loaded!", patcherList.size());
        patcherList.forEach(patcher -> FMLLog.info("%s [%s] loaded", patcher.getName(), patcher.getDescription()));
    }

    private void initPatches() {
        Reflections reflections = new Reflections(Patcher.class.getPackage().getName());

        reflections.getTypesAnnotatedWith(Patcher.PatcherInfo.class).forEach(aClass -> {
            try {
                Patcher patcher = (Patcher) aClass.newInstance();
                patcherList.add(patcher);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }

    public List<Patcher> getPatcherList() {
        return patcherList;
    }

    public <T extends Patcher> Patcher getPatchByClass(final Class<T> clazz) {
        return patcherList.stream().filter(patcher -> patcher.getClass().equals(clazz)).findFirst().map(clazz::cast).orElse(null);
    }

    public Patcher getPatchByName(final String patchName) {
        return patcherList.stream().filter(patcher -> patcher.getName().toLowerCase().replaceAll(" ", "").equalsIgnoreCase(patchName)).findFirst().orElse(null);
    }
}
