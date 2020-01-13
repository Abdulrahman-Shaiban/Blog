package com.abood.blog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {


    EditText username,password;
    Button register;
    private ImageView profileImage,camera,gallary;
    int CAMERAS = 0;
    int GALLERY = 1;
    String ConvertImage ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.r_username);
        password = findViewById(R.id.r_password);

        camera = findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAMERAS);

            }
        });

        gallary = findViewById(R.id.gallary);
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , GALLERY);

            }
        });

        profileImage = findViewById(R.id.profile_image);

        register = findViewById(R.id.r_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!username.getText().toString().equals("") && !password.getText().toString().equals("")){

                    final String name = username.getText().toString();
                    final String pass = password.getText().toString();
                    addUser(name,pass);

                    Intent intent = new Intent(Register.this,Login.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(Register.this, "Enter UserName and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:

                if (resultCode == RESULT_OK && imageReturnedIntent != null) {
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    profileImage.setImageBitmap(selectedImage);

                    ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
                    byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                    ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

                    Log.d("Abood3",ConvertImage);

                }
                break;

            case 1:

                if(resultCode == RESULT_OK){

                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        profileImage.setImageURI(selectedImage);

                        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
                        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                        ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

                        Log.d("Abood3",ConvertImage);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }
                break;
        }
    }

    public void addUser(final String name, final String pass){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppServer.IP+"register.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(response);
                    Toast.makeText(Register.this, "Welcome "+name, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Register.this, "connection Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> info = new HashMap<>();
                info.put("username",name);
                info.put("password",pass);
                info.put("image",ConvertImage);


                return info;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
        requestQueue.add(stringRequest);


    }

}
