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
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import jssc.SerialPort;

import java.io.File;
import java.net.URL;
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

    private ChartData       chartData1;
    private ChartData       chartData2;
    private ChartData       chartData3;
    private ChartData       chartData4;
    private ChartData       chartData5;
    private ChartData       chartData6;
    private ChartData       chartData7;
    private ChartData       chartData8;

    private long            lastTimerCall;
    private AnimationTimer timer;
    private DoubleProperty value;
    private static final Random RND = new Random();

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
        pressureChart.setTitle("Pressure Plot (Pa)");

        pressureChart.addChartData(chartData1, chartData2, chartData3, chartData4);

        temperatureGauge.setTitle("Temperature Gauge (°C)");
        temperatureChart.setTitle("Temperature Plot (°C)");

        voltageTile.setTitle("Voltage Gauge (Volts)");
        timeTile.setTitle("Mission Time");
        idTile.setTitle("Team ID");
        gpsTile.setTitle("GPS Data (°deg)");
        packetTile.setTitle("Packet Count");
        altitudeTile.setTitle("Altitude Data (metres)");
        softwareTile.setTitle("Software State");
        yawTile.setTitle("Yaw (°deg)");
        pitchTile.setTitle("Pitch (°deg)");
        rollTile.setTitle("Roll (°deg)");

        gpsTile.setText("12.9713946,79.1530457");
        packetTile.setText("40");
        altitudeTile.setText("10");
        softwareTile.setText("IDLE");
        yawTile.setText("45");
        pitchTile.setText("50");
        rollTile.setText("67");

        idTile.setText("Team 2840");

        File file  = new File("src/sample/logo.png");
        Image image  = new Image(file.toURI().toString());
        ImageView logoTile = new ImageView();
        logoTile.setImage(image);

        pictureRegion.getChildren().add(logoTile);

        mapView.addMapInitializedListener(this);

        //Data.divideString(";0.0000;0.0000;0.00;0.00;0.00;0;33.49;99156.31;-0.47;-0.89;-27.68:0;1;31;798;0;0;0.0000;0.0000;0.0000;0.0000;0.00;0.00;0.00;0;33.48;99152.25;-0.32;-0.63;-27.10");



//        lastTimerCall = System.nanoTime();
//        timer = new AnimationTimer() {
//            @Override public void handle(long now) {
//                if (now > lastTimerCall + 3_500_000_000L) {
//                    /*percentageTile.setValue(RND.nextDouble() * percentageTile.getRange() * 1.5 + percentageTile.getMinValue());
//                    gaugeTile.setValue(RND.nextDouble() * gaugeTile.getRange() * 1.5 + gaugeTile.getMinValue());
//
//                    sparkLineTile.setValue(RND.nextDouble() * sparkLineTile.getRange() * 1.5 + sparkLineTile.getMinValue());
//                    //value.set(RND.nextDouble() * sparkLineTile.getRange() * 1.5 + sparkLineTile.getMinValue());
//                    //sparkLineTile.setValue(20);
//
//                    highLowTile.setValue(RND.nextDouble() * 10);
//                    series1.getData().forEach(data -> data.setYValue(RND.nextInt(100)));
//                    series2.getData().forEach(data -> data.setYValue(RND.nextInt(30)));
//                    series3.getData().forEach(data -> data.setYValue(RND.nextInt(10)));
//
//                    chartData1.setValue(RND.nextDouble() * 50);
//                    chartData2.setValue(RND.nextDouble() * 50);
//                    chartData3.setValue(RND.nextDouble() * 50);
//                    chartData4.setValue(RND.nextDouble() * 50);
//                    chartData5.setValue(RND.nextDouble() * 50);
//                    chartData6.setValue(RND.nextDouble() * 50);
//                    chartData7.setValue(RND.nextDouble() * 50);
//                    chartData8.setValue(RND.nextDouble() * 50);
//
//
//                    barChartTile.getBarChartItems().get(RND.nextInt(3)).setValue(RND.nextDouble() * 80);
//
//                    leaderBoardTile.getLeaderBoardItems().get(RND.nextInt(3)).setValue(RND.nextDouble() * 80);
//
//                    circularProgressTile.setValue(RND.nextDouble() * 120);
//
//                    stockTile.setValue(RND.nextDouble() * 50 + 500);
//
//                    gaugeSparkLineTile.setValue(RND.nextDouble() * 100);
//
//                    countryTile.setValue(RND.nextDouble() * 100);
//
//                    smoothChartData1.setValue(smoothChartData2.getValue());
//                    smoothChartData2.setValue(smoothChartData3.getValue());
//                    smoothChartData3.setValue(smoothChartData4.getValue());
//                    smoothChartData4.setValue(RND.nextDouble() * 25);
//
//                    characterTile.setDescription(Helper.ALPHANUMERIC[RND.nextInt(Helper.ALPHANUMERIC.length - 1)]);
//
//                    flipTile.setFlipText(Helper.TIME_0_TO_5[RND.nextInt(Helper.TIME_0_TO_5.length - 1)]);
//
//                    radialPercentageTile.setValue(chartData1.getValue());*/
//
//                    chartData1.setValue(RND.nextDouble() * 50);
//                    chartData2.setValue(RND.nextDouble() * 50);
//                    chartData3.setValue(RND.nextDouble() * 50);
//                    chartData4.setValue(RND.nextDouble() * 50);
//
//                    pressureChart.setValue(RND.nextDouble() * 50);
//                    pressureGauge.setValue(RND.nextDouble() * 50);
//
//                    temperatureChart.setValue(RND.nextDouble() * 50);
//                    temperatureGauge.setValue(RND.nextDouble() * 50);
//
//                    voltageTile.setValue(RND.nextDouble() * 50);
//
//                    lastTimerCall = now;
//                }
//            }
//        };
    }

    @Override
    public void mapInitialized() {

        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(12.9713946,79.1530457))
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        map = mapView.createMap(mapOptions);

        //Add markers to the map

        LatLong joeSmithLocation = new LatLong(12.9713946,79.1530457);
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(joeSmithLocation);

        Marker joeSmithMarker = new Marker(markerOptions1);

        map.addMarker( joeSmithMarker );

        //timer.start();

        MyRunnable myRunnable = new MyRunnable(this);
        Thread t = new Thread(myRunnable);
        t.start();

//        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
//        infoWindowOptions.content("<h2>Fred Wilkie</h2>"
//                + "Current Location: Safeway<br>"
//                + "ETA: 45 minutes" );
//
//        InfoWindow fredWilkeInfoWindow = new InfoWindow(infoWindowOptions);
//        fredWilkeInfoWindow.open(map, fredWilkieMarker);
    }

    @Override
    public void onDataReceived(String temp, String yaw) {

        Double doubleTemp = Double.parseDouble(temp);

        temperatureGauge.setValue(doubleTemp);
        yawTile.setText(yaw);
    }

    @Override
    public void onFailure() {
        MyRunnable myRunnable = new MyRunnable(this);
        Thread t = new Thread(myRunnable);
        t.start();
    }
}
