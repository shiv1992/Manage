package shivang.manage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This activity process the requested data and displays it in a new page.
 * Created by SHIVVVV on 8/22/2017.
 */
public class Data_Activity extends AppCompatActivity {

    private static ImageView image;
    private static TextView name;
    private static TextView age;
    private static TextView phone;
    private static TextView date;

    private static TextView tagName;
    private static TextView tagAge;
    private static TextView tagPhone;
    private static TextView tagDate;
    private static ProgressBar progressBar;
    private static TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.display_layout);

        image = (ImageView)findViewById(R.id.imageView);
        name = (TextView)findViewById(R.id.name);
        age = (TextView)findViewById(R.id.age);
        phone = (TextView)findViewById(R.id.phone);
        date = (TextView)findViewById(R.id.date);

        tagName = (TextView)findViewById(R.id.nameTag);
        tagAge = (TextView)findViewById(R.id.ageTag);
        tagPhone = (TextView)findViewById(R.id.phoneTag);
        tagDate = (TextView)findViewById(R.id.dateTag);

        progressBar = (ProgressBar)findViewById(R.id.dataProgressBar);
        progressText = (TextView)findViewById(R.id.dataProgressText);

        Intent intent = getIntent();
        String ID = intent.getStringExtra("ID");
        String token = intent.getStringExtra("token");

        tagName.setText("");
        tagAge.setText("");
        tagPhone.setText("");
        tagDate.setText("");

        progressBar.setVisibility(View.VISIBLE);
        progressText.setText("Loading...");
        JSON_Data.jsonData(token,"data",ID);

    }

    /**
     * Action. Processes JSON data and Bitmap image.
     *
     * @param jsonArray the json array containing requested data.
     * @param bmp       the Bitmap image
     * @throws JSONException the json exception
     */
    public static void action(JSONArray jsonArray, Bitmap bmp) throws JSONException {

        if(bmp ==null){
        JSONObject arr = (JSONObject) jsonArray.get(0);
        JSONObject obj = (JSONObject) arr.get("record");


        String img = obj.getString("data_image");
        new GetImage().execute(img);

        String nam = obj.getString("data_name");
        tagName.setText("Name : ");
        name.setText(nam);

        String pho = obj.getString("data_phone");
        tagPhone.setText("Phone : ");
        phone.setText(pho);

        String dat = obj.getString("data_date");
        tagDate.setText("Date : ");
        date.setText(dat);

        String ag = obj.getString("data_age");
        tagAge.setText("Age : ");
        age.setText(ag);

            progressBar.setVisibility(View.GONE);
            progressText.setText("");
        }
        else{
            image.setImageBitmap(bmp);
        }

    }

}

/**
 * Gets image through AsyncTask.
 */
class GetImage extends AsyncTask<String, Void, Bitmap> {

    protected Bitmap doInBackground(String... urls) {

        URL url = null;
        Bitmap myBitmap = null;
        try {
            url = new URL(urls[0]);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myBitmap;

    }

    protected void onPostExecute(Bitmap str) {
        try {
            Data_Activity.action(null,str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

