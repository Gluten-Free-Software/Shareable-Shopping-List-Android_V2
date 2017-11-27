package com.glutenfreesoftware.shareable_shopping;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.glutenfreesoftware.shareable_shopping.dialog.LoginFailedDialogFragment;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button registerUser;
    private Button loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginUser = (Button) findViewById(R.id.login_existing_user);
        registerUser = (Button) findViewById(R.id.register_user);



    }

    /*
    Checks if the user exists. If user exists, logs in and starts a new intent.
     */
    public void loginUser(View v){
        EditText username = (EditText) findViewById(R.id.username_registered_user);
        String usernameString = username.getText().toString();
        EditText password = (EditText) findViewById(R.id.password_registered_user);
        String passwordString = password.getText().toString();

        checkLogin(v,usernameString,passwordString);
    }


    public void checkLogin(View view, String usernameInput, String passwordInput){

        final String username = usernameInput;
        //final String password = passwordInput;
        String password = new String(Hex.encodeHex(DigestUtils.sha(passwordInput)));

        String getUserURL = "http://192.168.1.43:8080/ssl-fk-sharing/api" +
                "/users/getUser?username=" + username + "&password=" + password;
        //String testAuth = "http://158.38.72.37:8080/Shareable-Shopping-List-REST/api";




        if(!username.equals("") && !password.equals("")){
            //System.out.println("It works! OMG");
            try {
                new LoginCheck(new LoginCheck.OnPostExecute() {
                    @Override
                    public void onPostExecute(List<Users> users) {
                        if(users.isEmpty()){
                            Log.d("LoginCheck", "No match in the database");
                            Snackbar loginFailed = Snackbar.make(findViewById(R.id
                                    .login_existing_user), R.string.login_failed, Snackbar
                                    .LENGTH_LONG);
                            loginFailed.show();
                        }
                        for(Users u : users) {
                            Log.d("LoginCheck", "We have a match in the database");
                            if(username.equals(u.getUsername())){
                                Log.d("LoginCheck", "User: " + u.getUsername());
                                Intent loginIntent = new Intent(MainActivity.this, Landing_Activity.class)
                                        .putExtra("username", u.getUsername());
                                startActivity(loginIntent);
                            }
                        }
                    }
                }).execute(new URL(getUserURL));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }



    public void openRegistrationActivity(View v){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}
