package sample;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.sun.org.apache.bcel.internal.generic.NEW;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import jssc.SerialPort;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable, Data.OnDataEventListener {

    @FXML
    private GoogleMapView mapView;

    @FXML
    private Tile pressureGauge;

    @FXML
    private Tile pressureChart;

    @FXML
    private Tile temperatureGauge;

    @FXML
    private Tile temperatureChart;

    @FXML
    private Tile voltageTile;

    @FXML
    private Tile timeTile;

    @FXML
    private HBox pictureRegion;

    @FXML
    private Tile gpsTile;

    @FXML
    private Tile idTile;

    @FXML
    private Tile packetTile;

    @FXML
    private Tile altitudeTile;

    @FXML
    private Tile softwareTile;

    @FXML
    private GridPane gridPane;

    @FXML
    private Tile yawTile;

    @FXML
    private Tile pitchTile;

    @FXML
    private Tile rollTile;

    @FXML
    private Tile voltageChart;

    @FXML
    private Tile latitudeTile;

    @FXML
    private Tile longitudeTile;

    @FXML
    private Tile satsTile;

    @FXML
    private Tile altitudeGpsTile;

    @FXML
    private Button button;

    private ChartData chartData1;
    private ChartData chartData2;
    private ChartData chartData3;
    private ChartData chartData4;
    private ChartData chartData5;
    private ChartData chartData6;
    private ChartData chartData7;
    private ChartData chartData8;

    Double globdoubleTemp, globdoublePressure, globdoubleYaw, globdoubleRoll, globlatitude, globlongitude, globvoltage, globaltitude, globalt_gps, globdoublePitch;

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    FileWriter fileWriter;

    private static final String FILE_HEADER = "altitude,temperature,pressure,voltage,latitude,longitude,gps_alt,roll,pitch,yaw";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        // Chart Data
//        chartData1 = new ChartData("Item 1", 24.0, Tile.GREEN);
//        chartData2 = new ChartData("Item 2", 10.0, Tile.BLUE);
//        chartData3 = new ChartData("Item 3", 12.0, Tile.RED);
//        chartData4 = new ChartData("Item 4", 13.0, Tile.YELLOW_ORANGE);
//        chartData5 = new ChartData("Item 5", 13.0, Tile.BLUE);
//        chartData6 = new ChartData("Item 6", 13.0, Tile.BLUE);
//        chartData7 = new ChartData("Item 7", 13.0, Tile.BLUE);
//        chartData8 = new ChartData("Item 8", 13.0, Tile.BLUE);

        button.setText("Close GUI and Save CSV file.");
        button.setMaxHeight(60);
        button.setOnAction(actionEvent ->  {
            closeFile();
        });

        gridPane.setHgap(5); //horizontal gap in pixels => that's what you are asking for
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        pressureGauge.setTitle("Pressure Gauge (Pa)");
        pressureGauge.setMinValue(99000);
        pressureGauge.setMaxValue(100000);
        pressureChart.setTitle("Pressure Plot (Pa)");

        temperatureGauge.setTitle("Temperature Gauge (°C)");
        temperatureChart.setTitle("Temperature Plot (°C)");

        voltageTile.setTitle("Voltage Gauge (Volts)");
        timeTile.setTitle("Mission Time");
        idTile.setTitle("Team ID");
        //gpsTile.setTitle("GPS Data (°deg)");
        packetTile.setTitle("Packet Count");
        altitudeTile.setTitle("Altitude Data (metres)");
        softwareTile.setTitle("Software State");
        yawTile.setTitle("Yaw (°deg)");
        pitchTile.setTitle("Pitch (°deg)");
        rollTile.setTitle("Roll (°deg)");
        voltageChart.setTitle("Voltage Plot (Volts)");
        latitudeTile.setTitle("Latitude");
        longitudeTile.setTitle("Longitude");
        satsTile.setTitle("Satellites");
        altitudeGpsTile.setTitle("Altitude (GPS)");

        File file = new File("src/sample/logo.png");
        Image image = new Image(file.toURI().toString());
        ImageView logoTile = new ImageView();
        logoTile.setImage(image);
        GridPane.setConstraints(logoTile, 3, 0);
        gridPane.getChildren().add(logoTile);

        idTile.setText("Team 2840");

        softwareTile.setDescription("ASCENDING");
        softwareTile.setDescriptionAlignment(Pos.CENTER);

        MyRunnable myRunnable = new MyRunnable(this);
        Thread t = new Thread(myRunnable);
        t.start();

        try {
            fileWriter = new FileWriter("data_file.csv");
        } catch (Exception e) {
            System.out.println("File could not be opened.");
        }

        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                temperatureGauge.setValue(globdoubleTemp);
                temperatureChart.setValue(globdoubleTemp);
                yawTile.setValue(globdoubleYaw);
                rollTile.setValue(globdoubleRoll);
                pitchTile.setValue(globdoublePitch);
                pressureGauge.setValue(globdoublePressure);
                pressureChart.setValue(globdoublePressure);
                voltageTile.setValue(globvoltage);
                voltageChart.setValue(globvoltage);
                latitudeTile.setValue(globlatitude);
                longitudeTile.setValue(globlongitude);
                altitudeTile.setValue(globaltitude);
                altitudeGpsTile.setValue(globalt_gps);

                writeFile();
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }

    private void closeFile() {
        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error while flushing/closing fileWriter !!!");
            e.printStackTrace();
        }
    }

    private void writeFile() {
        try {
            fileWriter.append(String.valueOf(globaltitude));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoubleTemp));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoublePressure));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globvoltage));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globlatitude));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globlongitude));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globalt_gps));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoubleRoll));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoublePitch));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoubleYaw));
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (Exception e) {
            System.out.println("Data not appended to file.");
        }
    }

    @Override
    public void onDataReceived(HashMap<String, String> hashMap) {

        try {
            globdoubleTemp = Double.parseDouble(hashMap.get("temperature"));
            globdoublePressure = Double.parseDouble(hashMap.get("pressure"));
            globdoubleYaw = Double.parseDouble(hashMap.get("yaw"));
            globdoubleRoll = Double.parseDouble(hashMap.get("roll"));
            globdoublePitch = Double.parseDouble(hashMap.get("pitch"));
            //Integer packetCount = Integer.parseInt(hashMap.get("packets"));
            globlatitude = Double.parseDouble(hashMap.get("latitude"));
            globlongitude = Double.parseDouble(hashMap.get("longitude"));
            globvoltage = Double.parseDouble(hashMap.get("voltage"));
            globaltitude = Double.parseDouble(hashMap.get("altitude"));
            globalt_gps = Double.parseDouble(hashMap.get("gps_alt"));
        } catch (Exception e) {
            System.out.println("Data could not be converted to appropriate format.");
        }
    }

    private void rotateNode(Node n, double x, double y, double z) {
        n.getTransforms().add(new Rotate(x, 0, 0, 0, Rotate.X_AXIS));
        n.getTransforms().add(new Rotate(y, 0, 0, 0, Rotate.Y_AXIS));
        n.getTransforms().add(new Rotate(z, 0, 0, 0, Rotate.Z_AXIS));
    }

    @Override
    public void onFailure() {
        MyRunnable myRunnable = new MyRunnable(this);
        Thread t = new Thread(myRunnable);
        t.start();
    }
}
