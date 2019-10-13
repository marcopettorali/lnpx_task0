

import com.sun.prism.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;


public class PCIcon extends StackPane{
    private Circle circle;
    private Text text;
    
    public PCIcon(double X, double Y, double radius,int counter,double x_offset,double y_offset) {
        circle = new Circle();
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStroke(Paint.valueOf("000000"));
        circle.setFill(Paint.valueOf("d3d3d3"));
        circle.setRadius(radius - 2);
        Text text = new Text(Integer.toString(counter));
        this.getChildren().addAll(circle,text);
        this.relocate(X * (2*radius) + (x_offset/2), Y * (2*radius) + (y_offset/2));
    }
    
    public void FillYellow(){
        circle.setFill(Paint.valueOf("FFFF33"));
    }

}
