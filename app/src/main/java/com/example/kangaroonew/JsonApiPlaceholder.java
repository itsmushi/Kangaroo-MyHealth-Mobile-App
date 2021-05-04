package com.example.kangaroonew;

import com.example.kangaroonew.models.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface JsonApiPlaceholder {

    @POST("login")
    Call<User> validateUser(@Body User user);

    @GET("allHospitals")
    Call<List<Hospital>> allHospital();

    @POST("createFeedback")
    Call<Comment> newPost(@Body Comment comment);

    @GET("hospitalStaffs/{id}")
    Call<List<Staff>> hospitalStaffs(@Path("id")int id);

    @POST("createAppointment")
    Call<AppointmentClass> newAppointment(@Body AppointmentClass appointment);

    @GET("appointmentStatus/{id}")
    Call<AppointmentClass> appointmentStatus(@Path("id")int id);

}
