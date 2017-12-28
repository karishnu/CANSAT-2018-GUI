package sample;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import jssc.SerialPort;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, MapComponentInitializedListener {

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
    private Tile logoTile;

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

    private GoogleMap map;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        SerialPort serialPort = new SerialPort("/dev/tty.usbmodem1411");
        try {
            serialPort.openPort();//Open serial por
            //serialPort.setParams(9600, 8, 1, 0);//Set params.
            String buffer="";
            while (true) {
                buffer = buffer + serialPort.readString();
                if(buffer.length()>=1){
                    if(buffer.charAt(buffer.length()-1)==';'){
                        System.out.println(buffer);
                        buffer="";
                    }
                }

//                if (buffer != null) {
//                        System.out.println(buffer.replace("\n",""));
//                        System.out.println("mayank");
////                    String dataString  = new String(buffer);
////                    System.out.println(dataString.replace("\n",""));
////                    System.out.println(dataString.split(",")[0]);
////                    for(String data: dataString.split(",")){
////                        System.out.println(data[0]);
////                    }
//                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        gridPane.setHgap(5); //horizontal gap in pixels => that's what you are asking for
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        pressureGauge.setTitle("Pressure Gauge");
        pressureChart.setTitle("Pressure Plot");

        temperatureGauge.setTitle("Temperature Gauge");
        temperatureChart.setTitle("Temperature Plot");

        voltageTile.setTitle("Voltage Gauge");
        timeTile.setTitle("Mission Time");
        idTile.setTitle("Team ID");
        gpsTile.setTitle("GPS Data");
        packetTile.setTitle("Packet Count");
        altitudeTile.setTitle("Altitude Data");
        softwareTile.setTitle("Software State");

        idTile.setText("AXXHGS2344");

        mapView.addMapInitializedListener(this);
    }

    @Override
    public void mapInitialized() {
        LatLong joeSmithLocation = new LatLong(47.6197, -122.3231);
        LatLong joshAndersonLocation = new LatLong(47.6297, -122.3431);
        LatLong bobUnderwoodLocation = new LatLong(47.6397, -122.3031);
        LatLong tomChoiceLocation = new LatLong(47.6497, -122.3325);
        LatLong fredWilkieLocation = new LatLong(47.6597, -122.3357);


        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(47.6097, -122.3331))
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        map = mapView.createMap(mapOptions);

        //Add markers to the map
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(joeSmithLocation);

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(joshAndersonLocation);

        MarkerOptions markerOptions3 = new MarkerOptions();
        markerOptions3.position(bobUnderwoodLocation);

        MarkerOptions markerOptions4 = new MarkerOptions();
        markerOptions4.position(tomChoiceLocation);

        MarkerOptions markerOptions5 = new MarkerOptions();
        markerOptions5.position(fredWilkieLocation);

        Marker joeSmithMarker = new Marker(markerOptions1);
        Marker joshAndersonMarker = new Marker(markerOptions2);
        Marker bobUnderwoodMarker = new Marker(markerOptions3);
        Marker tomChoiceMarker= new Marker(markerOptions4);
        Marker fredWilkieMarker = new Marker(markerOptions5);

        map.addMarker( joeSmithMarker );
        map.addMarker( joshAndersonMarker );
        map.addMarker( bobUnderwoodMarker );
        map.addMarker( tomChoiceMarker );
        map.addMarker( fredWilkieMarker );

//        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
//        infoWindowOptions.content("<h2>Fred Wilkie</h2>"
//                + "Current Location: Safeway<br>"
//                + "ETA: 45 minutes" );
//
//        InfoWindow fredWilkeInfoWindow = new InfoWindow(infoWindowOptions);
//        fredWilkeInfoWindow.open(map, fredWilkieMarker);
    }

}
