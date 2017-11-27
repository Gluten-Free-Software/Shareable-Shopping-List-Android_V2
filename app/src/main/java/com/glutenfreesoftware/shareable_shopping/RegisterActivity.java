package com.glutenfreesoftware.shareable_shopping;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    String usernameString;
    String passwordString;
    String emailString;
    Button registerUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUser = (Button) findViewById(R.id.registerbtn);

    }

    /*
    Registers an user to database, does not login.
     */
    public void registerUser(View v){
        EditText username = (EditText) findViewById(R.id.username);
        usernameString = username.getText().toString();

        EditText password = (EditText) findViewById(R.id.password);
        passwordString = password.getText().toString();

        EditText email = (EditText) findViewById(R.id.email);
        emailString = email.getText().toString();

        //Put details on server
        if(usernameString != null && passwordString != null && emailString != null){
            //System.out.println(usernameString + passwordString + emailString);

            String sha256hex = new String(Hex.encodeHex(DigestUtils.sha(passwordString)));
            registerToServer(v, usernameString, sha256hex, emailString);


            //registerToServer(v, usernameString, passwordString, emailString);
        }


    }

    /**
     * Registers to server
     */
    private void registerToServer(View v, String username, String password, String email) {
        //Context context = getApplicationContext();

        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        String url = "http://192.168.1.43:8080/ssl-fk-sharing/api/users/registerUser?username=" + username + "&email=" + email + "&password=" + password;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finishRegister();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }

        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", " application/json");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void finishRegister(){
        System.out.println("finishRegister***********************");
        finish();



    }




}
