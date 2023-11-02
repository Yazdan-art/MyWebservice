package com.example.mywebservice1.webservice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mywebservice1.R;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Webservice extends AppCompatActivity {
    TextView txt1;
    Button btnGetUserByid;
    Button btnGetAllUser, btnNext;
    ProgressBar prg1;
    EditText edtPersonId, edtLocalHost;
    APIInterface request;

    List<Person> personList = new ArrayList<>();

    private static final int REQUEST_PERMISSION = 1;
    private static final String[] Permissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE};

    public static void VerifyPermissions(Activity activity) {
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission3 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission4 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES);
        int permission5 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        if (permission1 != PackageManager.PERMISSION_GRANTED ||
                permission2 != PackageManager.PERMISSION_GRANTED ||
                permission3 != PackageManager.PERMISSION_GRANTED ||
                permission4 != PackageManager.PERMISSION_GRANTED ||
                permission5 != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(activity, Permissions, REQUEST_PERMISSION);
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webservice);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
        VerifyPermissions(Webservice.this);
        SetupView();
        prg1.setVisibility(View.VISIBLE);

        btnGetUserByid.setOnClickListener(v -> {
            Thread thread = new Thread(() -> {
                request = APIClient.getApiClient(edtLocalHost.getText().toString()).create(APIInterface.class);
                int userId = Integer.parseInt(edtPersonId.getText().toString());
                request.getUserById(userId).enqueue(new Callback<Person>() {


                    @Override
                    public void onResponse(Call<Person> call, Response<Person> response) {
                        if (response.body().getId() == 0) {
                            txt1.setText("Not Found...");
                        } else {
                            String temp = "";
                            Person person = response.body();
                            temp += person.getName() + "   " + person.getFamily() + "   " + person.getAge() + "\n";
                            txt1.setText(temp);
                        }
                    }


                    @Override
                    public void onFailure(Call<Person> call, Throwable t) {
                        Toast.makeText(Webservice.this, "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            });
            thread.start();
        });

        btnGetAllUser.setOnClickListener(v -> {
            txt1.setText("");
            Thread thread = new Thread(() -> {
                request = APIClient.getApiClient(edtLocalHost.getText().toString()).create(APIInterface.class);
                retrofit2.Call<List<Person>> call = request.getAllUser();
                call.enqueue(new retrofit2.Callback<List<Person>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Person>> call, retrofit2.Response<List<Person>> response) {
                        personList = response.body();
                        String temp = "";
                        for (Person person : personList) {
                            temp += person.getName() + "   " + person.getFamily() + "   " + person.getAge() + "\n";
                        }
                        txt1.setText(temp);
                        prg1.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Person>> call, Throwable t) {
                        Toast.makeText(Webservice.this, t.getMessage() + "", Toast.LENGTH_LONG).show();
                    }
                });
            });
            thread.start();
        });

        btnNext.setOnClickListener(view -> {
            Intent intent = new Intent(Webservice.this, AddUserActivity.class);
            startActivity(intent);
        });


        prg1.setVisibility(View.INVISIBLE);


    }

    private void SetupView() {
        txt1 = findViewById(R.id.txt);
        btnGetAllUser = findViewById(R.id.btnGetAllUser);
        btnGetUserByid = findViewById(R.id.btnGetUserByid);
        prg1 = findViewById(R.id.prg1);
        edtPersonId = findViewById(R.id.edtPersonId);
        edtLocalHost = findViewById(R.id.edtLocalHost);
        btnNext = findViewById(R.id.btnNext);

    }


    public void SendRequestByGetMethod(String url) {

    }
}
