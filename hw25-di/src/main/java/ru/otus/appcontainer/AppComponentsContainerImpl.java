package ru.otus.appcontainer;

import lombok.SneakyThrows;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final Map<Class<?>, Object> appComponentsByType = new HashMap<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    @SneakyThrows
    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        final Object configInstance;
        Constructor<?> configClassConstructor = configClass.getConstructor();
        configInstance = configClassConstructor.newInstance();

        List<Method> provisionMethods = Arrays.stream(configClass.getMethods())
                .filter(method -> method.getDeclaredAnnotation(AppComponent.class) != null)
                .toList();

        int minOrder = findMinOrder(provisionMethods);
        int maxOrder = findMaxOrder(provisionMethods);

        for (int i = minOrder; i <= maxOrder; ++i) {
            for (Method provisionMethod : provisionMethods) {
                var appComponentAnnotation = provisionMethod.getDeclaredAnnotation(AppComponent.class);
                if (appComponentAnnotation.order() == i) {
                    createAppComponent(configInstance, provisionMethod);
                }
            }
        }
    }

    private int findMinOrder(List<Method> provisionMethods) {
        int minOrder = Integer.MAX_VALUE;
        for (Method provisionMethod : provisionMethods) {
            int order = provisionMethod.getDeclaredAnnotation(AppComponent.class).order();
            if(order < minOrder) {
                minOrder = order;
            }
        }
        return minOrder;
    }

    private int findMaxOrder(List<Method> provisionMethods) {
        int maxOrder = Integer.MIN_VALUE;
        for (Method provisionMethod : provisionMethods) {
            int order = provisionMethod.getDeclaredAnnotation(AppComponent.class).order();
            if(order > maxOrder) {
                maxOrder = order;
            }
        }
        return maxOrder;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
        try {
            configClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Config class should have no args constructor", e);
        }
        Set<String> componentNames = new HashSet<>();
        Arrays.stream(configClass.getMethods())
                .filter(method -> method.getDeclaredAnnotation(AppComponent.class) != null)
                .forEach(provisionMethod -> {
                    var appComponentAnnotation = provisionMethod.getDeclaredAnnotation(AppComponent.class);
                    String appComponentName = appComponentAnnotation.name();
                    if (componentNames.contains(appComponentName)) {
                        throw new IllegalArgumentException("App components should have unique names");
                    }
                    componentNames.add(appComponentName);
                });
    }

    @SneakyThrows
    private void createAppComponent(Object configInstance, Method provisionMethod) {
        var componentDependenciesTypes = provisionMethod.getParameterTypes();
        var componentDependencies = Arrays.stream(componentDependenciesTypes)
                .map(this::getAppComponent)
                .toArray();
        Object appComponent = provisionMethod.invoke(configInstance, componentDependencies);

        Class<?> appComponentClass = appComponent.getClass();
        Class<?>[] appComponentInterfaces = appComponentClass.getInterfaces();

        List<Class<?>> appComponentTypes = new ArrayList<>();
        appComponentTypes.add(appComponentClass);
        appComponentTypes.addAll(List.of(appComponentInterfaces));

        String appComponentName = provisionMethod.getDeclaredAnnotation(AppComponent.class).name();

        putComponentByType(appComponent, appComponentTypes);
        putComponentByName(appComponent, appComponentName);
    }

    private void putComponentByName(Object appComponent, String appComponentName) {
        appComponentsByName.put(appComponentName, appComponent);
    }

    private void putComponentByType(Object appComponent, List<Class<?>> appComponentTypes) {
        for (Class<?> appComponentType : appComponentTypes) {
            if(appComponentsByType.containsKey(appComponentType)) {
                appComponentsByType.put(appComponentType, new IllegalStateException("Multiple candidates for this type"));
            } else {
                appComponentsByType.put(appComponentType, appComponent);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        Object obj = appComponentsByType.get(componentClass);
        if (obj == null) {
            throw new IllegalArgumentException("Found no component with provided type");
        }
        if (obj instanceof RuntimeException exception) {
            throw exception;
        }
        return (C) obj;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        Object obj = appComponentsByName.get(componentName);
        if (obj == null) {
            throw new IllegalArgumentException("Found no component with name \"" + componentName + "\"");
        }
        return (C) obj;
    }
}
