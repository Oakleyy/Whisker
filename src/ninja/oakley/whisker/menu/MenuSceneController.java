package ninja.oakley.whisker.menu;

import java.awt.Point;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import ninja.oakley.whisker.AbstractController;
import ninja.oakley.whisker.media.Media;
import ninja.oakley.whisker.media.Movie;

public class MenuSceneController extends AbstractController<VBox> {

    @FXML private TilePane tilePane;

    @FXML private SplitPane splitVert;
    @FXML private SplitPane splitHorizLeft;
    @FXML private ListView<SubMenu> subMenuList;

    @FXML private Label status;

    private Point currentSelection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setStatus("");
        subMenuList.getItems().add(MovieSubMenu.getSubMenu());
        tilePane.setHgap(8.0);
        tilePane.setVgap(8.0);
        tilePane.setAlignment(Pos.CENTER);
    }

    public void initScene(){
        getScene().getStylesheets().add("/javafx/css/menu.css");

        getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try{
                    switch (event.getCode()) {
                        case UP: moveCursor(Direction.TOP); break;
                        case DOWN: moveCursor(Direction.BOTTOM); break;
                        case LEFT: moveCursor(Direction.LEFT); break;
                        case RIGHT: moveCursor(Direction.RIGHT); break;
                        default: break;
                    }
                } catch(IndexOutOfBoundsException e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    public Media getCurrentSelection(){
        return null;
    }

    public void moveCursor(Direction dir){
        int x = 0, y = 0;
        switch(dir){
            case BOTTOM:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
            case TOP:
                y--;
                break;
            default:
                throw new RuntimeException("No such direction.");    
        }

        Point point;
        if(currentSelection != null){
            point = new Point(currentSelection);
            point.translate(x, y);
        } else {
            point = new Point(0,0);
        }

        moveCursor(point);
    }

    public void moveCursor(Point point){
        moveCursor((int) point.getX(), (int) point.getY());
    }

    public void moveCursor(int x, int y){
        if(!checkPoint(x, y)){
            throw new IndexOutOfBoundsException("Out of coordinate range.");
        }
        System.out.println(x + "_" + y);
        if(currentSelection != null){
            int prevIndex = getIndex((int) currentSelection.getX(), (int) currentSelection.getY());
            MediaImageView prevImage = (MediaImageView) tilePane.getChildren().get(prevIndex);
            prevImage.applyCSS(false);
        }

        int newIndex = getIndex(x, y);    
        MediaImageView newImage = (MediaImageView) tilePane.getChildren().get(newIndex);

        newImage.applyCSS(true);

        currentSelection = new Point(x, y);
    }

    private int getIndex(int x, int y){
        return (y * getActualColumns()) + x;
    }

    private int size(){
        return tilePane.getChildren().size();
    }

    private boolean checkPoint(int x, int y){
        return getIndex(x,y) < size() && y < getActualRows() && x < getActualColumns() && x >= 0 && y >= 0;
    }

    public void setStatus(String st){
        status.setText(st);
    }

    private int getActualRows() {
        try {
            return (int) getField(tilePane, "actualRows");
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e) {
        }
        return -1;
    }

    private int getActualColumns() {
        try {
            return (int) getField(tilePane, "actualColumns");
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e) {
        }
        return -1;
    }

    private Object getField(Object obj, String st) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Field field = obj.getClass().getDeclaredField(st);
        field.setAccessible(true);

        return field.get(obj);
    }

    @Override
    public String getFileName() {
        return "MenuScene.fxml";
    }
}
