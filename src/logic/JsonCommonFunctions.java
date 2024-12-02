/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author eobregon
 */
public class JsonCommonFunctions {

    public static double getJsonDouble(JSONObject jsonResumenFactura, String description) {
        double res = -1;
        try {
            res = jsonResumenFactura.getDouble(description);
        } catch (Exception e) {
            System.out.println("logic.JsonCommonFunctions.getJsonDouble() error " + e.getMessage() + " the function return value 0");
            res = 0;
        }
        return res;
    }

    public static int getJsonInt(JSONObject jsonResumenFactura, String description) {
        int res = -1;
        try {
            res = jsonResumenFactura.getInt(description);
        } catch (Exception e) {
            System.out.println("logic.JsonCommonFunctions.getJsonInt() error " + e.getMessage() + " the function return value 0");
            res = 0;
        }
        return res;
    }

    public static String getJsonString(JSONObject jsonResumenFactura, String description) {
        String res = "";
        try {
            res = jsonResumenFactura.getString(description);
        } catch (Exception e) {
            System.out.println("logic.JsonCommonFunctions.getJsonString() error " + e.getMessage() + " the function return value empty string");
        }
        return res;
    }

    public static JSONObject getJsonObject(JSONObject jsonLinea, String desc) {

        try {
            JSONObject res = jsonLinea.getJSONObject(desc);
            return res;
        } catch (Exception e) {
            System.err.println("logic.JsonCommonFunctions.getJsonObject() error " + e.getMessage());
            return null;
        }
    }

    public static JSONArray getJsonArray(JSONObject json, String description) {
        JSONArray array = null;
        try {
            array = json.getJSONArray(description);
        } catch (Exception e) {
            System.out.println("logic.JsonCommonFunctions.getJsonArray() error " + e.getMessage());
            array = null;
        }
        return array;
    }

}
