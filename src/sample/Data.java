package sample;

public class Data {

    private static String mainDivider = ";";
    private static Character checkDivider = ':';
    private static String subDivider = "/";

    public static String divideString(String input){
        String toSend = "";
        String names[] = input.split(mainDivider);
        String returnValue = "";

        for(String part: names){
            toSend = toSend + part;
            if(toSend.charAt(toSend.length()-1) == checkDivider){
                String buffer = toSend;
                toSend = "";
                if(buffer.charAt(0) == checkDivider){
                    parseString(buffer);
                }
            }
            else if(toSend.charAt(0) == checkDivider){
                returnValue = toSend;
            }
        }

        return returnValue;
    }

    private static void parseString(String input){
        System.out.println(input);
    }
}
