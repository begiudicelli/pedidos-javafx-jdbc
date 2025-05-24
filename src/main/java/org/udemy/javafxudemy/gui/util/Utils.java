package org.udemy.javafxudemy.gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
    public static Stage currentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static Integer tryParseToInt(String string){
        try{
            return Integer.parseInt(string);
        }catch(NumberFormatException e){
            return null;
        }
    }

    public static Double tryParseToDouble(String string){
        try{
            return Double.parseDouble(string);
        }catch(NumberFormatException e){
            return null;
        }
    }
}
