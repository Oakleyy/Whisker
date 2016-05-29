package ninja.oakley.whisker.menu;

import java.awt.AWTException;
import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Field;
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
import ninja.oakley.whisker.Whisker;
import ninja.oakley.whisker.hardware.ButtonController.ButtonListener;
import ninja.oakley.whisker.hardware.Direction;
import ninja.oakley.whisker.hardware.JoystickController.JoystickListener;
import ninja.oakley.whisker.media.Media;

public class MenuSceneController extends AbstractController<VBox> {

    private final Whisker instance;

    private final JoystickListener joystick = new JoystickTest();
    private final ButtonListener button = new ButtonTest();

    @FXML private TilePane tilePane;

    @FXML private SplitPane splitVert;
    @FXML private SplitPane splitHorizLeft;
    @FXML private ListView<Library> subMenuList;

    @FXML private Label status;

    private Point currentSelection;

    public MenuSceneController(Whisker instance){
        this.instance = instance;
    }

    @Override
    public void init() {
        setStatus("");
        tilePane.setHgap(8.0);
        tilePane.setVgap(8.0);
        tilePane.setAlignment(Pos.CENTER);

        getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try{
                    switch (event.getCode()) {
                        case UP: moveCursor(Direction.TOP); break;
                        case DOWN: moveCursor(Direction.BOTTOM); break;
                        case LEFT: moveCursor(Direction.LEFT); break;
                        case RIGHT: moveCursor(Direction.RIGHT); break;
                        case SPACE:
                            try {
                                instance.playMedia(getCurrentSelection());
                            } catch (AWTException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        default: break;
                    }
                } catch(IndexOutOfBoundsException e){

                }
            }
        });
    }

    private void moveCursor(Direction dir){
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

    private void moveCursor(Point point){
        moveCursor((int) point.getX(), (int) point.getY());
    }

    private void moveCursor(int x, int y){
        if(!checkPoint(x, y)){
            throw new IndexOutOfBoundsException("Out of coordinate range.");
        }

        if(currentSelection != null){
            int prevIndex = getIndex((int) currentSelection.getX(), (int) currentSelection.getY());
            MediaImageView prevImage = (MediaImageView) tilePane.getChildren().get(prevIndex);
            prevImage.applyEffect(false);
        }

        int newIndex = getIndex(x, y);    
        MediaImageView newImage = (MediaImageView) tilePane.getChildren().get(newIndex);

        newImage.applyEffect(true);

        currentSelection = new Point(x, y);
    }

    private Media getCurrentSelection(){
        return getSelection((int) currentSelection.getX(), (int) currentSelection.getY()).getMedia();
    }

    private MediaImageView getSelection(int x, int y){
        return (MediaImageView) tilePane.getChildren().get(getIndex(x, y));
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

    public JoystickListener getJoystickListener(){
        return this.joystick;
    }

    private class JoystickTest implements JoystickListener {

        @Override
        public void execute(Direction dir) {
            if(getRootNode().isVisible()){
                moveCursor(dir);
            }
        }

    }

    public ButtonListener getButtonListener(){
        return this.button;
    }

    private class ButtonTest implements ButtonListener {

        @Override
        public void execute() {
            if(getRootNode().isVisible()){


            }
        }

    }

    @Override
    public String getFileName() {
        return "MenuScene.fxml";
    }
}
