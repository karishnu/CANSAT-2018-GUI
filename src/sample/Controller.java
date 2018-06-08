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

    @FXML
    private Tile gpsTimeTile;

    private Double globdoubleTemp = 0.00, globdoublePressure = 0.00, globdoubleYaw = 0.00, globdoubleRoll = 0.00, globlatitude = 0.00;
    private Double globlongitude = 0.00, globvoltage = 0.00, globaltitude = 0.00, globalt_gps = 0.00, globdoublePitch = 0.00, globmissiontime = 0.00;
    private Integer globgpshour = 0, globgpsmin = 0, globgpssecs = 0, globgpssats = 0, globstate = 0;

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    FileWriter fileWriter;

    private static final String FILE_HEADER = "team_id,mission_time,packet_count,altitude,pressure,temperature,voltage,gps_time," +
            "gps_lat,gps_long,gps_alt,gps_sats,roll,pitch,yaw,soft_state";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        button.setText("Close GUI and Save CSV file.");
        button.setMaxHeight(60);
        button.setOnAction(actionEvent -> {
            closeFile();
        });

        gridPane.setHgap(5); //horizontal gap in pixels => that's what you are asking for
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        pressureGauge.setTitle("Pressure Gauge (Pa)");
        pressureChart.setTitle("Pressure Plot (Pa)");

        temperatureGauge.setTitle("Temperature Gauge (°C)");
        temperatureChart.setTitle("Temperature Plot (°C)");

        voltageTile.setTitle("Voltage Gauge (Volts)");
        timeTile.setTitle("Mission Time");
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
        gpsTimeTile.setTitle("GPS Time");

        File file = new File("src/sample/logo.png");
        Image image = new Image(file.toURI().toString());
//        ImageView logoTile = new ImageView();
//        logoTile.setImage(image);

        ImageView imgView = new ImageView(image);

        Tile tile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(750, 750)
                .title("TEAM")
                .text("Team ID - 2840")
                .textVisible(true)
                .graphic(imgView)
                .build();

        GridPane.setConstraints(tile, 4, 0);
        gridPane.getChildren().add(tile);

        //softwareTile.setDescription("ASCENDING");
        softwareTile.setDescriptionAlignment(Pos.CENTER);

        MyRunnable myRunnable = new MyRunnable(this);
        Thread t = new Thread(myRunnable);
        t.start();

        try {
            fileWriter = new FileWriter("data_file.csv");
            fileWriter.append(FILE_HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);

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
                softwareTile.setDescription(getStateText(globstate));
                timeTile.setValue(globmissiontime);
                satsTile.setValue(globgpssats);

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
            fileWriter.append("2840");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globmissiontime));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globaltitude));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoublePressure));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoubleTemp));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globvoltage));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globlatitude));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globlongitude));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globalt_gps));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globgpssats));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoubleRoll));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoublePitch));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoubleYaw));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globstate));
            fileWriter.append(NEW_LINE_SEPARATOR);

        } catch (Exception e) {
            System.out.println("Data not appended to file.");
        }
    }

    @Override
    public void onDataReceived(HashMap<String, String> hashMap) {

        try {
            //globmissiontime = Double.parseDouble(hashMap.get("mission_time"));
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
            globgpshour = Integer.parseInt(hashMap.get("hour"));
            globgpsmin = Integer.parseInt(hashMap.get("minute"));
            globgpssecs = Integer.parseInt(hashMap.get("seconds"));
            globstate = Integer.parseInt(hashMap.get("state"));
            globgpssats = Integer.parseInt(hashMap.get("sats"));
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

    private String getStateText(int statevalue) {
        switch (statevalue){
            case 0:
                return "IDLE";
            case 1:
                return "ASCENDING";
            case 2:
                return "DESCENDING";
            case 3:
                return "FLAPS DEPLOYED";
            case 4:
                return "HEAT SHIELD DEPLOYED";
            default:
                return "IDLE";
        }
    }
}