package com.marvin.bundle.web.container;

import com.marvin.bundle.framework.FrameworkBundle;
import com.marvin.bundle.web.WebBundle;
import com.marvin.component.configuration.ConfigurationInterface;
import com.marvin.component.container.ContainerBuilder;
import com.marvin.component.container.extension.Extension;
import com.marvin.component.container.xml.XMLDefinitionReader;
import com.marvin.component.io.loader.ClassPathResourceLoader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebExtension extends Extension {
    
    private static final Logger LOG = Logger.getLogger(WebExtension.class.getName());
    
    @Override
    public void load(HashMap<String, Object> configs, ContainerBuilder builder) {
        try {
            ClassPathResourceLoader loader = new ClassPathResourceLoader(WebBundle.class);
            XMLDefinitionReader reader = new XMLDefinitionReader(loader, builder);
            
            reader.read("resources/config/services.xml");
        
//            ConfigurationInterface configuration = this.getConfiguration();
//            HashMap<String, Object> conf = this.processConfiguration(configuration, configs);
            
//            HashMap<String, Definition> taggedDefinitions = builder.findTaggedDefinitions("event_subscriber");
            
//            for (Map.Entry<String, Definition> entrySet : taggedDefinitions.entrySet()) {
//                String id = entrySet.getKey();
//                Definition definition = entrySet.getValue();
//                
//            }
            
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
}
