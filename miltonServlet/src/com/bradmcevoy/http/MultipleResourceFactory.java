package com.bradmcevoy.http;

import com.bradmcevoy.http.*;
import java.util.ArrayList;
import java.util.List;

public class MultipleResourceFactory implements ResourceFactory, Initable {
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MultipleResourceFactory.class);
    
    private final List<ResourceFactory> factories = new ArrayList<ResourceFactory>();
    
    public MultipleResourceFactory() {
    }

    @Override
    public Resource getResource(String host, String url) {
        for( ResourceFactory rf : factories ) {
            Resource r = rf.getResource(host,url);
            if( r != null ) {
//                log.debug("got resource: " + r.getClass().getName() + " from resource factory: " + rf.getClass().getName());
                return r;
            }
        }
        log.debug("no resource factory supplied a resouce");
        return null;
    }

    @Override
    public String getSupportedLevels() {
        String s = "1,2";
        for( ResourceFactory rf : factories ) {
            String s2 = rf.getSupportedLevels();
            if( s2.length() < s.length() ) return s2;
        }
        return s;
    }

    
    
    @Override
    public void init(ApplicationConfig config, HttpManager manager) {        
        String sFactories = config.getInitParameter("resource.factory.multiple");
        init(sFactories, config, manager);
    }
     

    protected void init(String sFactories,ApplicationConfig config, HttpManager manager) {
        log.debug("init: " + sFactories );
        String[] arr = sFactories.split(",");
        for(String s : arr ) {
            createFactory(s,config,manager);
        }        
    }
    
    private void createFactory(String s,ApplicationConfig config, HttpManager manager) {
        log.debug("createFactory: " + s);
        Class c;
        try {
            c = Class.forName(s);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(s,ex);
        }
        Object o;
        try {
            o = c.newInstance();
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(s,ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(s,ex);
        }
        ResourceFactory rf = (ResourceFactory) o;
        if( rf instanceof Initable ) {
            Initable i = (Initable)rf;
            i.init(config,manager);
        }
        factories.add(rf);
    }



    @Override
    public void destroy(HttpManager manager) {
        if( factories == null ) {
            log.warn("factories is null");
            return ;
        }
        for( ResourceFactory f : factories ) {
            if( f instanceof Initable ) {
                ((Initable)f).destroy(manager);
            }
        }
    }
    
}
