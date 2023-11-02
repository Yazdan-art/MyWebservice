package com.example.mywebservice1.webservice;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface APIInterface {
    @GET("ShowAllUser.php")
    Call<List<Person>> getAllUser();

    @GET("ShowUserByIdRetrofit.php")
    Call<Person> getUserById(@Query("id") int userId);

    @POST("AddUserByPostMethod.php")
    Call<String> addUser(@Body Person person);

    @Multipart
    @POST("imageUpload.php")
    Call<MyResponse> uploadImage2(@Part("avatar\"; filename=\"ali.jpg\" ") RequestBody file);
}
