package ifmo.gui.controllers;

import java.io.IOException;
import java.util.LinkedList;

import ifmo.data.DisplayPerson;
import ifmo.network.TCPClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;

public class VisualizationController {

    @FXML private ImageView mapView;
    @FXML private Button backToTable;

    TCPClient tcpClient;

    VisualizationController(TCPClient tcpClient){
        this.tcpClient = tcpClient;
    }


    public void initialize() {
        LinkedList<DisplayPerson> persons = null;
        try {
            persons = tcpClient.loadCollection();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Image image = new Image(getClass().getClassLoader().getResource("map.png").toExternalForm());

        setImage(image);

        for (DisplayPerson person : persons) {
            drawCircles(person, persons);
        }
    }


    public void drawCircles(DisplayPerson person, LinkedList<DisplayPerson> persons) {
        double x =(double) person.getCoordinates().getX();
        double y =(double) person.getCoordinates().getY();
        System.out.println(x);
        System.out.println(y);

        double viewX = mapView.getLayoutX();
        double viewY = mapView.getLayoutY();
    
        double circleX = x - viewX;
        double circleY = y - viewY;

        Circle circle = new Circle(circleX, circleY, 10, Color.RED);
        Pane parent = (Pane) mapView.getParent();
        circle.setTranslateZ(100);

        int personId = person.getId();
        circle.setId(Integer.toString(personId));

        circle.setOnMouseClicked(event -> {

            String circleId = ((Circle) event.getSource()).getId();

            DisplayPerson clickedPerson = getPerson(persons, Integer.parseInt(circleId));

            Popup popup = new Popup();
            BorderPane borderPane = new BorderPane();
            borderPane.setStyle("-fx-background-color: white; -fx-padding: 10px;");
            borderPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    
            Label header = new Label(clickedPerson.getName());
            header.setStyle("-fx-font-weight: bold; -fx-padding: 5px;");
            borderPane.setTop(header);

            Label content = new Label("ID: " + clickedPerson.getId());
            content.setStyle("-fx-padding: 5px;");
            borderPane.setCenter(content);
    
            popup.getContent().add(borderPane);
    
            popup.setX(event.getScreenX());
            popup.setY(event.getScreenY());
            popup.setAutoHide(true);
            popup.show(parent.getScene().getWindow());
        });

        parent.getChildren().add(circle);
    }

    public void setImage(Image image) {
        mapView.setImage(image);
    }

    public DisplayPerson getPerson(LinkedList<DisplayPerson> people, int id){
        for (DisplayPerson person : people) {
            if(person.getId()==id){
                return person;
            }
        }
        return null;
    }
    
    @FXML
    public void handleBackToTable(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Table.fxml"));
        loader.setController(new TableController(tcpClient));
        Parent root;
        try {
            root = loader.load();
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene nextScene = new Scene(root);
            primaryStage.setScene(nextScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
