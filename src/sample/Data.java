package sample;

import java.util.*;

public class Data {

    private static final String mainDivider = ":";
    private static final String checkDivider = ";";
    private static final int valuesNo = 16;

    static String toSend = "";

    private static OnDataEventListener onDataEventListenerMain;

    public static void setOnDataEventListener(OnDataEventListener onDataEventListener) {
        onDataEventListenerMain = onDataEventListener;
    }

    public static void divideString(String input) {

        input = input.replace("\n", "");
        toSend = toSend + input;

        List<String> split_array;

        String[] afterSplit = toSend.split(mainDivider);

        if (afterSplit.length > 2) {

            split_array = new ArrayList<>();
            Collections.addAll(split_array, afterSplit);

            try {
                parseString(split_array.get(0));
            }
            catch (Exception e){
                System.out.println("Couldn't parse data frame.");
            }

            split_array.remove(0);
            toSend = String.join(":", split_array);
        }
    }

    private static void parseString(String input) {

        System.out.println(input);

        String values[] = input.split(checkDivider);

        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(values));
        arrayList.remove(0);

        if (arrayList.size() == valuesNo) {
            System.out.print("\n");

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("team_id", arrayList.get(0));
            //hashMap.put("mission_time", arrayList.get(1));
            //hashMap.put("packets", arrayList.get(2));
            hashMap.put("altitude", arrayList.get(1));
            hashMap.put("pressure", arrayList.get(2));
            hashMap.put("temperature", arrayList.get(3));
            hashMap.put("voltage", arrayList.get(4));
            hashMap.put("hour", arrayList.get(5));
            hashMap.put("minute", arrayList.get(6));
            hashMap.put("seconds", arrayList.get(7));
            //hashMap.put("mseconds", arrayList.get(9));
            hashMap.put("latitude", arrayList.get(8));
            hashMap.put("longitude", arrayList.get(9));
            hashMap.put("gps_alt", arrayList.get(10));
            hashMap.put("sats", arrayList.get(11));
            hashMap.put("roll", arrayList.get(12));
            hashMap.put("pitch", arrayList.get(13));
            hashMap.put("yaw", arrayList.get(14));
            hashMap.put("state", arrayList.get(15));

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
