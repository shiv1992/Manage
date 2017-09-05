package shivang.manage;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class retrieve all JSON data.
 * Created by SHIVVVV on 8/21/2017.
 */
public class JSON_Data {

    /**
     * Json data. This function is called with the mentioned parameters for fetching JSON data.
     *
     * @param token  The token
     * @param fun    This parameter is identified to which function is calling to retrieve JSON data and can have values"list", "data" and "users"
     * @param dataID The record ID to be processed
     */
    public static void jsonData(String token, String fun, String dataID){

        String[] str = new String[3];
        str[0] = token;
        str[1] = fun;
        if(fun.equals("data")){
            str[2] = dataID;
        }
        new GetJSONAsync().execute(str);
    }
}


/**
 * AsyncTask to get JSON data.
 */
class GetJSONAsync extends AsyncTask<String[], Void, String[]> {

    /**
     * The Response.
     */
    static String response;

    protected String[] doInBackground(String[]... urls) {

        String[] str = urls[0];

        String url = null;

        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        client.addHeader("Content-Type","application/json");
        client.addHeader("Authorization", "Bearer "+str[0]);

        if(str[1].equals("list")){
            url = "https://app.iformbuilder.com/exzact/api/profiles/470115/pages/3639810/records?FILTER[0][KEY]=ID&FILTER[0][CONDITION]=%3E";
        }
        else if(str[1].equals("data")){
            url = "https://app.iformbuilder.com/exzact/api/profiles/470115/pages/3639810/records/"+str[2]+"/feed";
            params.add("FORMAT","JSON");
        }
        else if(str[1].equals("users")){
            url = "https://app.iformbuilder.com/exzact/api/profiles/470115/pages/3639969/feed";
            params.add("FORMAT","JSON");
        }

        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                response = new String(responseBody);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                response="0";
            }
        });

        String[] tmp = new String[2];
        tmp[0] = response;
        tmp[1] = str[1];
        return tmp;
    }

    protected void onPostExecute(String[] str) {

        if(str[1].equals("list")){
            try {
                List_Activity.action(new JSONObject(str[0]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(str[1].equals("data"))
        {
            try {
                Data_Activity.action(new JSONArray(str[0]),null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(str[1].equals("users"))
        {
            try {
                Login_Validation.ValidateJSON(new JSONArray(str[0]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}