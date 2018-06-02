package sample;

import java.util.*;

public class Data {

    private static final String mainDivider = ":";
    private static final String checkDivider = ";";
    private static final int valuesNo = 19;

    static String toSend = "";

    private static OnDataEventListener onDataEventListenerMain;

    public static void setOnDataEventListener(OnDataEventListener onDataEventListener) {
        onDataEventListenerMain = onDataEventListener;
    }

    public static void divideString(String input) {

        input = input.replace("\n", "");

        toSend = toSend + input;

        List<String> split_array = new ArrayList<>();

        if (toSend.split(mainDivider).length >= 2) {

            Collections.addAll(split_array, toSend.split(mainDivider));

            parseString(split_array.get(0));
            split_array.remove(0);

            toSend = String.join(":", split_array);
        }
    }

    private static void parseString(String input) {

        String values[] = input.split(checkDivider);

        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(values));
        arrayList.remove(0);

        System.out.print(input);
        System.out.print("\n");

        for (String value : values) {
            System.out.print(value + " ");
        }

        if (arrayList.size() == valuesNo) {
            System.out.print("\n");

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("team_id", arrayList.get(0));
            hashMap.put("mission_time", arrayList.get(1));
            hashMap.put("packets", arrayList.get(2));
            hashMap.put("altitude", arrayList.get(3));
            hashMap.put("pressure", arrayList.get(4));
            hashMap.put("temperature", arrayList.get(5));
            hashMap.put("voltage", arrayList.get(6));
            hashMap.put("hour", arrayList.get(7));
            hashMap.put("minute", arrayList.get(8));
            hashMap.put("seconds", arrayList.get(9));
            hashMap.put("mseconds", arrayList.get(10));
            hashMap.put("latitude", arrayList.get(11));
            hashMap.put("longitude", arrayList.get(12));
            hashMap.put("gps_alt", arrayList.get(13));
            hashMap.put("sats", arrayList.get(14));
            hashMap.put("roll", arrayList.get(15));
            hashMap.put("pitch", arrayList.get(16));
            hashMap.put("yaw", arrayList.get(17));
            hashMap.put("state", arrayList.get(18));

            printValues(hashMap);

            onDataEventListenerMain.onDataReceived(hashMap);
        } else {
            System.out.println("Incorrect number of values received.");
        }
    }

    private static void printValues(HashMap<String, String> hashMap) {
        for (String name : hashMap.keySet()) {
            String value = hashMap.get(name);
            System.out.print(" | " + name + " " + value + " ");
        }
        System.out.print("\n");
    }

    interface OnDataEventListener {
        void onDataReceived(HashMap<String, String> hashMap);

        void onFailure();
    }
}
