package shivang.manage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.util.concurrent.ExecutionException;


/**
 * Login activity. The application starts with this activity.
 */
public class Login_Activity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button loginSubmit;
    private static ProgressBar update;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        Login_Activity.context = getApplicationContext();

        userName = (EditText)findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        loginSubmit = (Button)findViewById(R.id.login);
        update = (ProgressBar) findViewById(R.id.progressBar2);

        update.setVisibility(View.GONE);

        loginSubmit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Runnable show_toast = new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(Login_Activity.this, "Internet is off!", Toast.LENGTH_SHORT).show();
                    }
                };

                if(!isOnline()) {
                    show_toast.run();
                }else {

                    update.setVisibility(View.VISIBLE);
                    String[] str = new String[2];
                    str[0] = userName.getText().toString();
                    str[1] = password.getText().toString();

                    Login_Validation.setUsername(str[0]);
                    Login_Validation.setPassword(str[1]);

                    try {
                        GenerateAccessToken.getToken("validate");

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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

    /**
     * Action.
     *
     * @param token The access token received by validating JWT.
     * @param flag  The flag. If TRUE then login credentials are validated.
     */
    public static void action(String token, boolean flag){
        update.setVisibility(View.GONE);

        if(flag){
            Intent intent = new Intent(Login_Activity.context, List_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("token", token);
            Login_Activity.context.startActivity(intent);
        }
        else{
            Toast.makeText(context,"Invalid Credentials!",Toast.LENGTH_SHORT).show();
        }
    }
}
