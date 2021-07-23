package io.github.willqi.pizzaserver.commons.utils;

import java.util.Random;

public class Util {

    public static final char[] RANDOM_IDENTIFIER_CHARACTERS = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};

    public static String generateRandomIdentifier(int minlength, int variation){
        int length = minlength + (variation > 0 ? new Random().nextInt(variation) : 0);
        String fstr = "";
        for(int i = 0; i < length; i++){
            Random r = new Random();
            fstr = fstr.concat(String.valueOf(RANDOM_IDENTIFIER_CHARACTERS[r.nextInt(RANDOM_IDENTIFIER_CHARACTERS.length)]));
        }
        return fstr;
    }

}
