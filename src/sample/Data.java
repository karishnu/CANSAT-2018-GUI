package sample;

public class Data {

    private static String mainDivider = ":";
    private static String checkDivider = ";";
    private static int valuesNo = 19;

    static String toSend = "";

    public static String divideString(String input) {

        input = input.replace("\n", "");

        String returnValue = "";

        toSend = toSend + input;

        if (toSend.split(mainDivider).length == 3) {
            parseString(toSend.split(mainDivider)[1]);
            toSend = toSend.split(mainDivider)[2];
        }
//        for(String part: names){
//            System.out.println(part);
//
////            toSend = toSend + part;
////            if(toSend.charAt(toSend.length()-1) == checkDivider){
////                String buffer = toSend;
////                toSend = "";
////                if(buffer.charAt(0) == checkDivider){
////                    parseString(buffer);
////                }
////            }
////            else if(toSend.charAt(0) == checkDivider){
////                returnValue = toSend;
////            }
//
//
//        }

        return returnValue;
    }

    private static void parseString(String input) {

        String values[] = input.split(checkDivider);

        if (values.length == valuesNo+1) {
            System.out.print("\n");
            for (String value : values) {
                System.out.print(value + " ");
            }
        }
    }
}
