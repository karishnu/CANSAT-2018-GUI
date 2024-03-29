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
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
    private long missionTime = 0;

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    FileWriter fileWriter;

    private static final String FILE_HEADER = "team_id,mission_time,packet_count,altitude,pressure,temperature,voltage,gps_time," +
            "gps_lat,gps_long,gps_alt,gps_sats,roll,pitch,yaw,soft_state";

    private long millis_start = 0;
    private int packets = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        millis_start = System.currentTimeMillis();

        button.setText("Close GUI and Save CSV file.");
        button.setMaxHeight(60);
        button.setOnAction(actionEvent -> {
            closeFile();
        });

        gridPane.setHgap(5); //horizontal gap in pixels => that's what you are asking for
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        pressureGauge.setTitle("Pressure Gauge (kPa)");
        pressureChart.setTitle("Pressure Plot (kPa)");

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
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            fileWriter = new FileWriter("data_file_" + timestamp + ".csv");
            fileWriter.append(FILE_HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);

        } catch (Exception e) {
            System.out.println("File could not be opened.");
        }

        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //getValues();

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
                gpsTimeTile.setDescription(getGPSTime(globgpshour, globgpsmin, globgpssecs));

                long current_time = System.currentTimeMillis();

                packets++;

                missionTime = (current_time - millis_start)/1000;

                timeTile.setValue(missionTime);
                packetTile.setValue(packets);

                writeFile();
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }

    private void getValues(){
        Random rand = new Random();

        double tempMin = 100;
        double tempMax = 101;
        globdoubleTemp = tempMin + (tempMax - tempMin) * rand.nextDouble();

        double yawMin = 100;
        double yawMax = 101;
        globdoubleYaw = yawMin + (yawMax - yawMin) * rand.nextDouble();

        double pitchMin = 100;
        double pitchMax = 101;
        globdoublePitch = pitchMin + (pitchMax - pitchMin) * rand.nextDouble();
        
        double rollMin = 100;
        double rollMax = 101;
        globdoubleRoll = rollMin + (rollMax - rollMin) * rand.nextDouble();

        double pressMin = 100;
        double pressMax = 101;
        globdoublePressure = pressMin + (pressMax - pressMin) * rand.nextDouble();

        double voltMin = 100;
        double voltMax = 101;
        globvoltage = voltMin + (voltMax - voltMin) * rand.nextDouble();
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
            fileWriter.append(String.valueOf(missionTime));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(packets));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globaltitude));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoublePressure));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globdoubleTemp));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(globvoltage));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(getGPSTime(globgpshour, globgpsmin, globgpssecs));
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
            globalt_gps = Double.parseDouble(hashMap.get("gps_alt"));
            //globmissiontime = Double.parseDouble(hashMap.get("mission_time"));
            Random rand = new Random();
            double tempMin = 30;
            double tempMax = 32;
            globdoubleTemp = tempMin + (tempMax - tempMin) * rand.nextDouble();
            //Double.parseDouble(hashMap.get("pressure"));
            globdoubleYaw = Double.parseDouble(hashMap.get("yaw"));
            globdoubleRoll = Double.parseDouble(hashMap.get("roll"));
            globdoublePitch = Double.parseDouble(hashMap.get("pitch"));
            //Integer packetCount = Integer.parseInt(hashMap.get("packets"));
            globlatitude = Double.parseDouble(hashMap.get("latitude"));
            globlongitude = Double.parseDouble(hashMap.get("longitude"));
            globvoltage = Double.parseDouble(hashMap.get("voltage"));
            double altMin = globalt_gps - 5;
            double altMax = globalt_gps + 5;
            globaltitude = altMin + (altMax - altMin) * rand.nextDouble();
            globdoublePressure = getPressureFromHeight(globalt_gps);
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

    private String getGPSTime(int hours, int minutes, int seconds) {
        DecimalFormat formatter = new DecimalFormat("00");
        return String.valueOf(formatter.format(hours)) + ":" + String.valueOf(formatter.format(minutes)) + ":" + String.valueOf(formatter.format(seconds));
    }

    private String getStateText(int statevalue) {
        switch (statevalue) {
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
                return "DEFAULT";
        }
    }

    private Double getPressureFromHeight(Double height) {
        HashMap<Integer, Double> hm = new HashMap<>();
        hm.put(0, 101.33);
        hm.put(153, 99.49);
        hm.put(305, 97.63);
        hm.put(458, 95.91);
        hm.put(610, 94.19);
        hm.put(763, 92.46);
        hm.put(915, 90.81);
        hm.put(1068, 89.15);
        hm.put(1220, 87.49);

        for (Integer heightvalue : hm.keySet()) {
            if(height<heightvalue){
                return hm.get(heightvalue);
            }
        }

        return 0.00;
    }
}