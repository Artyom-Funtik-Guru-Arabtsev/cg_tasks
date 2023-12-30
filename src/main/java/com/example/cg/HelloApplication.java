package com.example.cg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Colored Circle Sector");
        Canvas canvas = new Canvas(400, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawColoredCircleSector(gc, 200, 200, 100, 90, Color.BLUE, Color.RED);
        primaryStage.setScene(new Scene(new StackPane(canvas)));
        primaryStage.show();
    }

    private void drawColoredCircleSector(GraphicsContext gc, double centerX, double centerY,
                                         double radius, double angle, Color startColor, Color endColor) {

        PixelWriter pw = gc.getPixelWriter();

        double xCoordinate1 = centerX + radius * Math.cos(angle);
        double yCoordinate1 = centerY + radius * Math.sin(angle);

        double xCoordinate2 = centerX + radius * Math.cos(-angle);
        double yCoordinate2 = centerY + radius * Math.sin(-angle);

        for (int x = 0; x < 400; x++) {
            for (int y = 0; y < 400; y++) {
                if (isPointInSector(x, y, centerX, centerY, radius, angle)) {
                    Color color = calculateColor(x, y,
                            xCoordinate1, yCoordinate1,
                            xCoordinate2, yCoordinate2,
                            centerX, centerY,
                            endColor, startColor, endColor);
                    pw.setColor(x, y, color);
                }
            }
        }

        pw.setColor((int) centerX, (int) centerY, Color.RED);
    }

    private boolean isPointInSector(double x, double y, double centerX, double centerY, double radius, double angle) {
        double startAngle = 0;
        double endAngle = Math.toRadians(angle);
        double angleToCenter = Math.atan2(y - centerY, x - centerX);

        if (endAngle < 0) endAngle += 2 * Math.PI;
        if (angleToCenter < 0) angleToCenter += 2 * Math.PI;

        return angleToCenter >= startAngle && angleToCenter <= endAngle && Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= radius;
    }

    public static Color calculateColor(double x, double y,
                                       double xUp, double yUp,
                                       double xDown, double yDown,
                                       double xCenter, double yCenter,
                                       Color up,
                                       Color center,
                                       Color down) {
        double distanceUp = Math.sqrt(Math.pow(xUp - x, 2) + Math.pow(yUp - y, 2));
        double distanceDown = Math.sqrt(Math.pow(xDown - x, 2) + Math.pow(yDown - y, 2));
        double distanceCenter = Math.sqrt(Math.pow(xCenter - x, 2) + Math.pow(yCenter - y, 2));

        double weightUp = (distanceUp != 0) ? 1 / distanceUp : 0;
        double weightDown = (distanceDown != 0) ? 1 / distanceDown : 0;
        double weightCenter = (distanceCenter != 0) ? 1 / distanceCenter : 0;

        double totalWeight = weightUp + weightDown + weightCenter;

        double red = (weightCenter * center.getRed() + weightUp * up.getRed() + weightDown * down.getRed()) / totalWeight;
        double green = (weightCenter * center.getGreen() + weightUp * up.getGreen() + weightDown * down.getGreen()) / totalWeight;
        double blue = (weightCenter * center.getBlue() + weightUp * up.getBlue() + weightDown * down.getBlue()) / totalWeight;

        return new Color(red, green, blue, 1.0);
    }
}