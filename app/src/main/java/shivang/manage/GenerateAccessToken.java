package shivang.manage;


import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


/**
 * This class Generates JSON Web Token which is comsumed to receive Access Token for iFormBuilder.
 * Created by SHIVVVV on 8/18/2017.
 */
public class GenerateAccessToken {

    private static String clientKey = "6a8f90fe963ed5b8748f24aebf733a1ff5f9d10b";
    private static String clientSecret = "fd56f661757694e5de901c324a67d51192b27de1";
    private static String url = "https://app.iformbuilder.com/exzact/api/oauth/token";


    /**
     * Gets JSON Web Token.
     *
     * @param fun This parameter represents which function is calling and can have "validate" and "list" values
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     */
    public static void getToken(String fun) throws ExecutionException, InterruptedException {

        LinkedHashMap<String,Object> header = new LinkedHashMap<>();
        header.put("alg","HS256");
        header.put("typ","JWT");
        LinkedHashMap<String, Object> claimSet = new LinkedHashMap<>();
        claimSet.put("aud",url);
        claimSet.put("iss",clientKey);
        long unixTime = System.currentTimeMillis() / 1000L; // Get current UNIX time
        Issue_Time.setIssueTime(unixTime); // Set Issue time
        claimSet.put("exp", unixTime + 600);
        claimSet.put("iat", unixTime);

        String jwt = Jwts.builder().setHeader(header).setClaims(claimSet).signWith(SignatureAlgorithm.HS256,clientSecret.getBytes()).compact();

        String[] str = new String[2];
        str[0] = jwt;
        str[1] = fun;
        new GetTokenAsync().execute(str);

    }

}

/**
 * AsyncTask to send JWT and receive access token.
 */
class GetTokenAsync extends AsyncTask<String[], Void, String[]> {

    /**
     * The Access Token.
     */
   static String token=null;

   protected String[] doInBackground(String[]... urls) {

        String[] tmp = urls[0];
        String url = "https://app.iformbuilder.com/exzact/api/oauth/token";

        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("Content-Type","application/x-www-form-urlencoded");
        params.add("grant_type","urn:ietf:params:oauth:grant-type:jwt-bearer");
        params.add("assertion", tmp[0]);


        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    token = object.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                token="0";
            }
        });
        tmp[0] = token;
        return tmp;
    }

    protected void onPostExecute(String[] feed) {

        if(feed[1].equals("validate")){
            Login_Validation.Validate(feed[0]);
        }
        else if(feed[1].equals("list")){
                JSON_Data.jsonData(feed[0],feed[1],null);
        }

    }
}