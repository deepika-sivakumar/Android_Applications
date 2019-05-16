package edu.gatech.seclass.crypto6300.Helper;

import java.util.Random;

public class EncryptionHelper {

    // Function to remove special characters from a string
    public static String removeSpecialCharacter(String s)
    {
        for (int i = 0; i < s.length(); i++)
        {
            // Finding the character whose ASCII value fall under this range
            if (s.charAt(i) < 'A' || s.charAt(i) > 'Z' &&
                    s.charAt(i) < 'a' || s.charAt(i) > 'z')
            {
                // erase function to erase the character
                s = s.substring(0, i) + s.substring(i + 1);
                i--;
            }
        }
        return s;
    }

    // Function to remove duplicate characters from a string and convert it all to uppercase
    public static String removeDuplicate(String s)
    {
        s = s.toUpperCase();
        String str = new String();
        int len = s.length();

        // loop to traverse the string and check for repeating chars using IndexOf() method in Java
        for (int i = 0; i < len; i++)
        {
            // character at i'th index of s
            char c = s.charAt(i);

            // if c is present in str, it returns the index of c, else it returns -1
            if (str.indexOf(c) < 0)
            {
                // adding c to str if -1 is returned
                str += c;
            }
        }
        return str;
    }

    // Function to generate random number within a range
    public static int getRandomNumber() {
        int min = 1;
        int max = 25;
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    // Function to shift an alphabet
    public static char shiftAlphabet(char alphabet, int shift) {
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //Get position of the input alphabet
        int alphabetPosition = alphabets.indexOf(alphabet);
        //Shift the position
        int keyVal = (shift + alphabetPosition) % 26;
        //Get the alphabet in the new position
        char replaceVal = alphabets.charAt(keyVal);
        return replaceVal;
    }
}
