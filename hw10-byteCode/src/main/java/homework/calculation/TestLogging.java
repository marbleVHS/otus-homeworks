package homework.calculation;


import homework.annotation.Log;

import java.util.Random;

public class TestLogging {

    @Log
    public Object calculation(int param1) {
        return null;
    }

    public Object calculation(int param1, int param2) {
        return null;
    }

    @Log
    public int calculation(int param1, int param2, String param3) {
        int magicNumber = 191;
        int randomInt = new Random().nextInt(3, 17);
        System.out.println(randomInt);
        magicNumber = magicNumber + randomInt;
        System.out.println(magicNumber);
        return magicNumber;
    }

    @Log
    public String calculation(Object obj, int param1, int param2, String param3, String var5) {
        String result = param2 + param3 + var5 + param1;
        System.out.println(result);
        return result;
    }

}
