package com.example.mywebservice1.webservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mywebservice1.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserActivity extends AppCompatActivity {
    EditText edtName;
    EditText edtFamily;
    EditText edtAge, edtLocalHost2;
    Button btnAddUser, btnNext2 ;
    TextView txtShowUsers1;
    APIInterface request;
    APIClient apiClient = new APIClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        SetupView();

        btnAddUser.setOnClickListener(v -> {
            final Handler handler = new Handler();
            Thread thread = new Thread(() -> {
                String name = edtName.getText().toString();
                String family = edtFamily.getText().toString();
                int age = Integer.parseInt(edtAge.getText().toString());
                Person person = new Person(name, family, age);
                request = apiClient.getApiClient(edtLocalHost2.getText().toString()).create(APIInterface.class);
               // request = APIClient.getApiClient(edtLocalHost2.getText().toString()).create(APIInterface.class);
                request.addUser(person).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        txtShowUsers1.setText(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        txtShowUsers1.setText("Error");
                    }
                });
            });
            thread.start();
        });
        btnNext2.setOnClickListener(view -> {
            Intent intent = new Intent(AddUserActivity.this, ImageUploadActivity.class);
            startActivity(intent);
        });
    }

    public void SetupView() {
        edtName = findViewById(R.id.edtName);
        edtFamily = findViewById(R.id.edtFamily);
        edtAge = findViewById(R.id.edtAge);
        edtLocalHost2 = findViewById(R.id.edtLocalHost2);
        btnAddUser = findViewById(R.id.btnAddUser);
        txtShowUsers1 = findViewById(R.id.txtShowUsers1);
        btnNext2 = findViewById(R.id.btnNext2);
    }
}