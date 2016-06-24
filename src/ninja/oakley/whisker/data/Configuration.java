package ninja.oakley.whisker.data;

import java.util.ArrayList;
import java.util.List;

import ninja.oakley.whisker.media.Media;
import ninja.oakley.whisker.media.Media.MediaFactory;

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
