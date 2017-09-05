package shivang.manage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class validates the user Login details.
 * Created by SHIVVVV on 8/21/2017.
 */
public class Login_Validation {

    private static String token;
    private static String username;
    private static String password;

    /**
     * Validate.
     *
     * @param str str
     */
    public static void Validate(String str){
        token = str;
        JSON_Data.jsonData(str,"users",null);
    }

    /**
     * Validate json.
     * Function to validate Username and Password.
     * @param jsonArray the json array
     * @throws JSONException the json exception
     */
    public static void ValidateJSON(JSONArray jsonArray) throws JSONException {

        boolean flag = false;

        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject arr = (JSONObject) jsonArray.get(i);
            JSONObject obj = (JSONObject) arr.get("record");

            if(obj.get("user_name").equals(username) && obj.get("password").equals(password)){
                flag=true;
                break;
            }
        }

        Login_Activity.action(token,flag);

    }

    /**
     * Set username.
     *
     * @param uName the username
     */
    public static void setUsername(String uName){
        username = uName;
    }

    /**
     * Set password.
     *
     * @param pass the password
     */
    public static void setPassword(String pass){
        password = pass;
    }
}

