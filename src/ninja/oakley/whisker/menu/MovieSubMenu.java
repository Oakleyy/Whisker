package ninja.oakley.whisker.menu;

import java.util.ArrayList;
import java.util.List;

import ninja.oakley.whisker.media.Media;

public class MovieSubMenu implements SubMenu {

    private List<Media> media = new ArrayList<Media>();
    
    @Override
    public List<Media> getMedia() {
        return media;
    }

    @Override
    public String toString(){
        return "Movies";
    }
}
