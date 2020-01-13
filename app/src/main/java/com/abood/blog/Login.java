package com.abood.blog;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    public static String userName;
    public static String userProfile;

    EditText username,password;
    Button login,rigester;

    private static final String MyPREFERENCES = "Abood";
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

            finish();
            moveTaskToBack(true);
            return true;

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        String name = sharedpreferences.getString("username", null);


        if(name != null) {

            Intent intent = new Intent(Login.this,PostListFragment.class);
            intent.putExtra("name",name);
            startActivity(intent);
            finish();
        }


        username = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        rigester = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!username.getText().toString().equals("") && !password.getText().toString().equals("")){

                    final String name = username.getText().toString();
                    final String pass = password.getText().toString();
                    checkUser(name, pass);

            } else {
                    Toast.makeText(Login.this, "Enter UserName and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        rigester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(Login.this,Register.class);
                startActivity(intent1);

            }
        });

    }


    public void checkUser(final String name, final String pass){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppServer.IP+"login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    userName = jsonObject.getString("u_name");
                    userProfile = jsonObject.getString("u_profile");
                    Toast.makeText(Login.this, "Welcome "+name, Toast.LENGTH_SHORT).show();

                    editor.putString("username", name); // Storing string
                    editor.commit();

                    Intent intent = new Intent(Login.this,PostListFragment.class);
                    intent.putExtra("name",name);
                    startActivity(intent);

                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Login.this, "connection Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> info = new HashMap<>();
                info.put("username",name);
                info.put("password",pass);

                return info;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);


    }



}
