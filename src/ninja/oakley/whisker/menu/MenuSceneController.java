package ninja.oakley.whisker.menu;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import ninja.oakley.whisker.AbstractController;

public class MenuSceneController extends AbstractController<VBox> {

    private SubMenu currentSubMenu;

    @FXML private TilePane tilePane;

    @FXML private SplitPane splitVert;
    @FXML private SplitPane splitHorizLeft;
    @FXML private ListView<SubMenu> subMenuList;

    @FXML private Label status;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setStatus("");
        subMenuList.getItems().add(new MovieSubMenu());
        tilePane.setHgap(8.0);
        tilePane.setVgap(8.0);
        tilePane.setPrefColumns(5);
        updateTilePane();
    }


    public void setStatus(String st){
        status.setText(st);
    }

    @Override
    public String getFileName() {
        return "MenuScene.fxml";
    }
    
    public void updateTilePane(){
        ImageView a = new ImageView(new Image("file:/home/pi/whisker/pj.png"));
        ImageView b = new ImageView(new Image("file:/home/pi/whisker/creed.png"));
        ImageView c = new ImageView(new Image("file:/home/pi/whisker/dino.png"));
        ImageView d = new ImageView(new Image("file:/home/pi/whisker/goose.png"));
        ImageView e = new ImageView(new Image("file:/home/pi/whisker/peanuts.png"));
        ImageView f = new ImageView(new Image("file:/home/pi/whisker/spec.png"));
        tilePane.getChildren().addAll(a, b, c, d, e, f);
    }

}
