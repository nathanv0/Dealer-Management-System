package org.example.hellofx;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

public class Controller {

    @FXML
    private Circle myCircle;
    private double x;
    private double y;


    public void up(ActionEvent e) {
        System.out.println("UP press");
        myCircle.setCenterY(y -= 5);
    }
    public void down(ActionEvent e) {
        System.out.println("down press");
        myCircle.setCenterY(y += 5);
    }
    public void left(ActionEvent e) {
        System.out.println("left press");
        myCircle.setCenterX(x -= 5);
    }
    public void right(ActionEvent e) {
        System.out.println("right press");
        myCircle.setCenterX(x += 5);
    }
}
