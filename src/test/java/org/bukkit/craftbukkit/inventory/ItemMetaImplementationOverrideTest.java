package org.bukkit.craftbukkit.inventory;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.Overridden;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ItemMetaImplementationOverrideTest {

    static final Class<CraftMetaItem> parent = CraftMetaItem.class;
    @Parameter(0)
    public Callable<?> test;
    @Parameter(1)
    public String name;

    @Parameters(name = "[{index}]:{1}")
    public static List<Object[]> data() {
        final List<Object[]> testData = new ArrayList<Object[]>();
        List<Class<? extends CraftMetaItem>> classes = new ArrayList<Class<? extends CraftMetaItem>>();

        for (Material material : ItemStackTest.COMPOUND_MATERIALS) {
            Class<? extends CraftMetaItem> clazz = CraftItemFactory.instance().getItemMeta(material).getClass().asSubclass(parent);
            if (clazz != parent) {
                classes.add(clazz);
            }
        }

        List<Method> list = new ArrayList<Method>();

        for (Method method : parent.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Overridden.class)) {
                list.add(method);
            }
        }

        for (final Class<?> clazz : classes) {
            for (final Method method : list) {
                testData.add(
                    new Object[]{
                        new Callable<Method>() {
                            public Method call() throws Exception {
                                return clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                            }
                        },
                        clazz.getSimpleName() + " contains " + method.getName()
                    }
                );
            }

            testData.add(
                new Object[]{
                    new Callable<DelegateDeserialization>() {
                        public DelegateDeserialization call() throws Exception {
                            return clazz.getAnnotation(DelegateDeserialization.class);
                        }
                    },
                    clazz.getSimpleName() + " contains annotation " + DelegateDeserialization.class
                }
            );
        }

        return testData;
    }

    @Test
    public void testClass() throws Throwable {
        assertThat(name, test.call(), is(not(nullValue())));
    }
}
