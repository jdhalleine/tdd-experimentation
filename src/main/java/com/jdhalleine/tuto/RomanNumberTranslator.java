package com.jdhalleine.tuto;

import java.util.HashMap;
import java.util.Map;

public class RomanNumberTranslator {

    public static Map<String, Integer> data = new HashMap<>();

    static {
        data.put("M", 1000);
        data.put("X", 10);
        data.put("I", 1);
        data.put("L", 50);
        data.put("C", 100);
        data.put("D", 500);
        data.put("V", 5);
    }


    public static int translate(String s) {

        int ret = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            Integer charValue = data.get(String.valueOf(c));
            ret = ret + charValue;

            if (i + 1 < s.length()) {
                char nextCar = s.charAt(i + 1);
                if ((c == 'I' && nextCar != 'I') || (c == 'X' && nextCar == 'L')) {
                    ret = ret - (charValue * 2);
                }
            }

        }

        return ret;


    }
}
