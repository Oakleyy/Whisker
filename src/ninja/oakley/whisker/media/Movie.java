package ninja.oakley.whisker.media;

import java.nio.file.Path;

import javafx.scene.image.Image;

public class Movie implements Media {

    private final Path path;

    public Movie(Path path){
        this.path = path;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAuthor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Image getCoverImage() {
        // TODO Auto-generated method stub
        return null;
    }

}
