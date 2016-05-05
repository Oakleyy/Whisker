package ninja.oakley.whisker.menu;

import java.util.ArrayList;
import java.util.List;

import ninja.oakley.whisker.media.Media;

public enum MovieSubMenu implements SubMenu {

    INSTANCE;
    
    public static MovieSubMenu getSubMenu(){
        return INSTANCE;
    }
    
    private final String name = "Movies";
    private final List<Media> media = new ArrayList<Media>();
    
    private MovieSubMenu(){
        
    }
    
    public String getName(){
        return name;
    }
    
    
    
    @Override
    public List<Media> getMedia() {
        return media;
    }

    /**
     * Returns the same result as getName()
     * 
     * @return String name of the submenu
     */
    @Override
    public String toString(){
        return getName();
    }
}
