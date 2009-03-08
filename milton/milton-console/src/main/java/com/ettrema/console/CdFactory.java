
package com.ettrema.console;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.ResourceFactory;
import java.util.List;

public class CdFactory implements ConsoleCommandFactory {

    @Override
    public ConsoleCommand create(List<String> args, String host, String currentDir, Auth auth,ResourceFactory resourceFactory) {
        return new Cd(args, host, currentDir, resourceFactory);
    }

    @Override
    public String[] getCommandNames() {
        return new String[]{"cd"};
    }

    @Override
    public String getDescription() {
        return "Change Directory to a path, absolute or relative";
    }
    
    

}
