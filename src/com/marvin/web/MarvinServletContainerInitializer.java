package com.marvin.web;

import com.marvin.component.util.ReflectionUtils;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

@HandlesTypes(WebApplicationInitializer.class)
public class MarvinServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext sc) throws ServletException {
        List<WebApplicationInitializer> initializers = new LinkedList<>();

        if (set != null) {
            for (Class<?> waiClass : set) {
				// Be defensive: Some servlet containers provide us with invalid classes,
                // no matter what @HandlesTypes says...
                if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers())
                        && WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
                    try {
                        initializers.add((WebApplicationInitializer) ReflectionUtils.accessibleConstructor(waiClass).newInstance());
                    } catch (Throwable ex) {
                        throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
                    }
                }
            }
        }

        if (initializers.isEmpty()) {
            sc.log("No Marvin WebApplicationInitializer types detected on classpath");
            return;
        }

        sc.log(initializers.size() + " Marvin WebApplicationInitializers detected on classpath");
        
        // maybe order initializers.
        
        for (WebApplicationInitializer initializer : initializers) {
            initializer.onStartup(sc);
        }

    }

}
