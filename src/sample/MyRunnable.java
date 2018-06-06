package sample;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.util.Arrays;

public class MyRunnable implements Runnable {

    private Data.OnDataEventListener onDataEventListener;
    private SerialPort serialPort;
    final private String PORT = "/dev/tty.usbserial-00000000";

    public MyRunnable(Data.OnDataEventListener onDataEventListener) {
        this.onDataEventListener = onDataEventListener;
    }

    public void run() {
        try {
            connect();
            Data.setOnDataEventListener(onDataEventListener);
            while (true) {
                byte[] buffer = serialPort.readBytes(10);
                //System.out.println(new String(buffer));
                if (buffer != null) {
                    Data.divideString(new String(buffer));
                } else {
                    String[] list = SerialPortList.getPortNames();
                    boolean exist = Arrays.stream(list).anyMatch(str -> str.trim().equals(PORT));
                    if (exist) {
                        System.out.println("serial port connection exists");
                    } else {
                        try {
                            //this port is not listed among the available ports..* need to close the port and release the memory
                            serialPort.closePort();
                            System.out.println("closing the port to reconnect");
                            connect();
                        } catch (SerialPortException e) {
                            System.out.println("closing the port failed");
                        }
                        System.out.println("serial connection is not ready");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //onDataEventListener.onFailure();
        }
    }

    private void connect() {
        try {
            serialPort = new SerialPort(PORT);
            serialPort.openPort();//Open serial port
            serialPort.setParams(9600, 8, 1, 0);//Set params.
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not open port. Closing it.");
            try {
                serialPort.closePort();
                connect();
            }
            catch (Exception e2){
                e2.printStackTrace();
                System.out.println("Could not close port.");
            }
        }
    }
}