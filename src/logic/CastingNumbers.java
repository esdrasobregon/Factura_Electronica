/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

/**
 *
 * @author eobregon
 */
public class CastingNumbers {

    /**
     * this function works for string numbers as '123 123 123 123,34'
     */
    
    /*public static void main(String[] args) {
        String n1 = "123 123 123 123,56";
        String n2 = "123,123,123,123.56";
        System.out.println("result n1 "+extractNumericChars(n1));
        System.out.println("result n2 "+extractNumericChars(n2));
    }*/
    public String extractNumericChars(String input) {
        StringBuilder result = new StringBuilder();
        if (input.contains(" ")) {
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (Character.isDigit(c)) {
                    result.append(c);
                } else if (c == ',') {
                    result.append('.');
                }
            }
        } else {
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (Character.isDigit(c) || c== ',' || c=='.') {
                    result.append(c);
                }
            }
        }

        return result.toString();
    }

}
