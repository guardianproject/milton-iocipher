package com.bradmcevoy.http;

public class ServletHttpManager extends HttpManager implements Initable {
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ServletHttpManager.class);
    
    public ServletHttpManager(ResourceFactory resourceFactory) {
        super(resourceFactory);
    }
    
    
    
    @Override
    public void init(ApplicationConfig config,HttpManager manager) {
        log.debug("init");
        if( resourceFactory != null ) {
            if( resourceFactory instanceof Initable ) {
                Initable i = (Initable)resourceFactory;
                i.init(config,manager);
            }
            for( String paramName : config.getInitParameterNames() ) {
                if( paramName.startsWith("filter_") ) {
                    String filterClass = config.getInitParameter(paramName);
                    log.debug("init filter: " + filterClass);
                    String[] arr = paramName.split("_");
                    String ordinal = arr[arr.length-1];
                    int pos = Integer.parseInt(ordinal);
                    initFilter(config, filterClass, pos);
                }
            }
        }
    }

    private void initFilter(final ApplicationConfig config, final String filterClass, final int pos) {
        try {
            Class c = Class.forName(filterClass);
            Filter filter = (Filter) c.newInstance();
            if( filter instanceof Initable ) {
                ((Initable)filter).init(config,this);
            }
            this.addFilter(pos,filter);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(filterClass,ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(filterClass,ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(filterClass,ex);
        }
    }
    
    @Override
    public void destroy(HttpManager manager) {
        log.debug("destroy");
        if( resourceFactory != null ) {
            if( resourceFactory instanceof Initable ) {
                Initable i = (Initable)resourceFactory;
                i.destroy(manager);
            }
        }
    }
    
    
    
}
