package ninja.oakley.whisker;

import java.util.ArrayList;
import java.util.List;

import ninja.oakley.whisker.media.Media;
import ninja.oakley.whisker.media.Media.MediaFactory;
import ninja.oakley.whisker.menu.Library;
import ninja.oakley.whisker.menu.Library.LibraryFactory;

public final class Configuration {
    
    private final String systemUser = "pi";
    private final String systemGroup = "pi";

    public Configuration() {
        
    }

    public String getSystemUser(){
        return this.systemUser;
    }

    public String getSystemGroup(){
        return this.systemGroup;
    }


}
