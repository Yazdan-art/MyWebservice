package com.example.mywebservice1.webservice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.Objects;
import com.example.mywebservice1.R;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUploadActivity extends AppCompatActivity {
    APIInterface request;
    Button btnUploadImage;
    TextView txtMessage;
    EditText edtLocalHost3;
    ImageView imgUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        SetupView();
        btnUploadImage.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 100);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            uploadFile(selectedImage);

    }}

    private void uploadFile(Uri fileUri) {
        File file = new File(getRealPathFromURI(fileUri));
        //Toast.makeText(this, file+"", Toast.LENGTH_LONG).show();
        RequestBody image = RequestBody.create
                (MediaType.parse(Objects.requireNonNull(getContentResolver().getType(fileUri))), file);

        //Toast.makeText(this, getContentResolver().getType(fileUri)+"", Toast.LENGTH_LONG).show();

        //So far everything is ok
       request=APIClient.getApiClient(edtLocalHost3.getText().toString()).create(APIInterface.class);
        request.uploadImage2(image).enqueue(new Callback<MyResponse>() {
           Response<MyResponse>response2;//this my code
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                response2=response;
              //  Toast.makeText(ImageUploadActivity.this, response.body().error+"", Toast.LENGTH_LONG).show();//
                if (!response.body().error) {
                    txtMessage.setText(response.body().message );
                   // Picasso.get().load(response.body().url).into(imgUpload);
                } else {
                    txtMessage.setText("خطا در آپلود تصویر");
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(ImageUploadActivity.this,response2.body().error+"", Toast.LENGTH_SHORT).show();
                edtLocalHost3.setText(t.toString());
            }
        });

    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    private void SetupView() {
        btnUploadImage=findViewById(R.id.btnUploadImage);
        txtMessage=findViewById(R.id.txtMessage);
        imgUpload=findViewById(R.id.imgUpload);
        edtLocalHost3=findViewById(R.id.edtLocalHost3);
    }
}