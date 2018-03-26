package sample;

import jssc.SerialPort;

public class MyRunnable implements Runnable {

    Data.OnDataEventListener onDataEventListener;

    public MyRunnable(Data.OnDataEventListener onDataEventListener) {
        this.onDataEventListener = onDataEventListener;
    }

    public void run() {
        try {
            SerialPort serialPort = new SerialPort("/dev/tty.usbserial-00000000");
            serialPort.openPort();//Open serial port
            serialPort.setParams(9600, 8, 1, 0);//Set params.
            Data.setOnDataEventListener(onDataEventListener);
            while (true) {
                byte[] buffer = serialPort.readBytes(30);
                if (buffer != null) {
                    Data.divideString(new String(buffer));
                }
            }
        }catch (Exception e){
            onDataEventListener.onFailure();
            e.printStackTrace();
        }
    }
}