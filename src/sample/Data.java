package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Data {

    private static String mainDivider = ":";
    private static String checkDivider = ";";
    private static int valuesNo = 21;

    static String toSend = "";

    private static OnDataEventListener onDataEventListenerMain;

    public static void setOnDataEventListener(OnDataEventListener onDataEventListener){
        onDataEventListenerMain = onDataEventListener;
    }

    public static String divideString(String input) {

        input = input.replace("\n", "");

        String returnValue = "";

        toSend = toSend + input;

        List<String> split_array = new ArrayList<>();

        if (toSend.split(mainDivider).length >= 2) {

            Collections.addAll(split_array, toSend.split(mainDivider));

            parseString(split_array.get(0));
            split_array.remove(0);

            toSend = String.join(":", split_array);
        }


        return returnValue;
    }

    private static void parseString(String input) {

        String values[] = input.split(checkDivider);

        if (values.length == valuesNo + 1) {
            System.out.print("\n");
            for (String value : values) {
                System.out.print(value + " ");
            }

            onDataEventListenerMain.onDataReceived(values[16], values[20]);
        }
    }

    interface OnDataEventListener {
        void onDataReceived(String temperature, String yaw);
    }
}
