package shivang.manage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * List Activity  will contain functions to get the IDs of records
 * Created by SHIVVVV on 8/21/2017.
 */
public class List_Activity extends AppCompatActivity {

    private static ListView listView;
    private static Context context;
    private static ProgressBar progressBar;
    private static TextView progressText;
    private long TIMEOUT = new Long(3500); //3500 sec timeout for safe buffer of 100 sec

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=getApplicationContext();
        setContentView(R.layout.id_layout);
        listView = (ListView)findViewById(R.id.listView);

        Intent intent = getIntent();
        final String token = intent.getStringExtra("token");

        progressBar = (ProgressBar)findViewById(R.id.updateBar);
        progressText = (TextView)findViewById(R.id.updateText);
        progressBar.setVisibility(View.VISIBLE);
        progressText.setText("Loading...");

        JSON_Data.jsonData(token,"list",null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Runnable show_toast = new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(List_Activity.this, "Internet is off!", Toast.LENGTH_SHORT).show();
                    }
                };

                if(!isOnline()) {
                    show_toast.run();
                }else{
                    long unixTime = System.currentTimeMillis() / 1000L;
                    if(unixTime- Issue_Time.getIssueTime() >TIMEOUT)  //Verify access timeout ie 3600 seconds
                    {
                        listView.setAdapter(null);
                        progressBar.setVisibility(View.VISIBLE);
                        progressText.setText("Token Expired! Generating new token");
                        try {
                            GenerateAccessToken.getToken("list");
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        String value = (String) adapter.getItemAtPosition(position);
                        Intent intent = new Intent(getApplicationContext(), Data_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("token", token);
                        intent.putExtra("ID", value);
                        getApplicationContext().startActivity(intent);
                    }
                }
            }
        });


    }

    /**
     * Isonline. This function checks if the Internet connectivity is active or not.
     *
     * @return the boolean
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
    }


    /**
     * Action. Function to receive JSON containing record IDs and set List View.
     *
     * @param jsonObject the json object
     * @throws JSONException the json exception
     */
    public static void action(JSONObject jsonObject) throws JSONException {


        JSONArray jsonArray = jsonObject.getJSONArray("RECORDS");
        List<String> list = new ArrayList<>();

        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject tmp = jsonArray.getJSONObject(i);
            list.add(tmp.get("ID").toString());
            //Log.v("LL Resp 1 : ",tmp.get("ID").toString());
        }

        Collections.reverse(list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,list);
        progressBar.setVisibility(View.GONE);
        progressText.setText("");
        listView.setAdapter(arrayAdapter);
    }

}
