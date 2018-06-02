package sample;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import jssc.SerialPort;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable, MapComponentInitializedListener, Data.OnDataEventListener {

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

    private GoogleMap map;

    private ChartData chartData1;
    private ChartData chartData2;
    private ChartData chartData3;
    private ChartData chartData4;
    private ChartData chartData5;
    private ChartData chartData6;
    private ChartData chartData7;
    private ChartData chartData8;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Chart Data
        chartData1 = new ChartData("Item 1", 24.0, Tile.GREEN);
        chartData2 = new ChartData("Item 2", 10.0, Tile.BLUE);
        chartData3 = new ChartData("Item 3", 12.0, Tile.RED);
        chartData4 = new ChartData("Item 4", 13.0, Tile.YELLOW_ORANGE);
        chartData5 = new ChartData("Item 5", 13.0, Tile.BLUE);
        chartData6 = new ChartData("Item 6", 13.0, Tile.BLUE);
        chartData7 = new ChartData("Item 7", 13.0, Tile.BLUE);
        chartData8 = new ChartData("Item 8", 13.0, Tile.BLUE);

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

        File file = new File("src/sample/logo.png");
        Image image = new Image(file.toURI().toString());
        ImageView logoTile = new ImageView();
        logoTile.setImage(image);
        GridPane.setConstraints(logoTile, 3, 0);
        gridPane.getChildren().add(logoTile);

        idTile.setText("Team 2840");

//        box = new Cylinder(50, 150);
//        GridPane.setValignment(box, VPos.CENTER);
//        GridPane.setHalignment(box, HPos.CENTER);
//        GridPane.setConstraints(box, 2, 2, 2, 2);
//        gridPane.getChildren().add(box);

        mapView.addMapInitializedListener(this);
    }

    @Override
    public void mapInitialized() {

        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(12.9713946, 79.1530457))
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        map = mapView.createMap(mapOptions);

        //Add markers to the map

//        LatLong joeSmithLocation = new LatLong(12.9713946, 79.1530457);
//        MarkerOptions markerOptions1 = new MarkerOptions();
//        markerOptions1.position(joeSmithLocation);
//
//        Marker joeSmithMarker = new Marker(markerOptions1);
//
//        map.addMarker(joeSmithMarker);

        MyRunnable myRunnable = new MyRunnable(this);
        Thread t = new Thread(myRunnable);
        t.start();
    }

//    Double xroll = 0.00;
//    Double xpitch = 0.00;
//    Double xyaw = 0.00;
//
//    Double yroll = 0.00;
//    Double ypitch = 0.00;
//    Double yyaw = 0.00;


    @Override
    public void onDataReceived(HashMap<String, String> hashMap) {

        Double doubleTemp = Double.parseDouble(hashMap.get("temperature"));
        Double doublePressure = Double.parseDouble(hashMap.get("pressure"));
        Double doubleYaw = Double.parseDouble(hashMap.get("yaw"));
        Double doubleRoll = Double.parseDouble(hashMap.get("roll"));
        Double doublePitch = Double.parseDouble(hashMap.get("pitch"));
        Integer packetCount = Integer.parseInt(hashMap.get("packets"));

        temperatureGauge.setValue(doubleTemp);
        temperatureChart.setValue(doubleTemp);
        yawTile.setValue(doubleYaw);
        rollTile.setValue(doubleRoll);
        pitchTile.setValue(doublePitch);
        pressureGauge.setValue(doublePressure);
        pressureChart.setValue(doublePressure);
        packetTile.setValue(packetCount);

//        yroll = xroll - Double.parseDouble(hashMap.get("roll"));
//        xroll = Double.parseDouble(hashMap.get("roll"));
//        ypitch = xpitch - Double.parseDouble(hashMap.get("pitch"));
//        xpitch = Double.parseDouble(hashMap.get("pitch"));
//        yyaw = xyaw - Double.parseDouble(hashMap.get("yaw"));
//        xyaw = Double.parseDouble(hashMap.get("yaw"));
//
//        rotateNode(box, ypitch, yyaw, yroll);
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
