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

    @GET("/oneStaff/{id}")
    Call<Staff> staffDetails(@Path("id")int id);

    @POST("createAppointment")
    Call<AppointmentClass> newAppointment(@Body AppointmentClass appointment);

    @GET("appointmentStatus/{id}")
    Call<AppointmentClass> appointmentStatus(@Path("id")int id);

    @GET("oneAppointment/{id}")
    Call<AppointmentClass> oneAppointment(@Path("id")int id);

    @GET("userAppointments/{id}")
    Call<List<AppointmentClass>> userAppointments(@Path("id")int id);

    @GET("userAppointmentsFull/{id}")
    Call<List<AppointmentWithName>> userAppointmentsFull(@Path("id")int id);

    @GET("userBraceletData/{id}")
    Call<List<BraceletData>> braceletData(@Path("id")int id);

    @GET("userDosage/{id}")
    Call<List<DosageData>> dosageData(@Path("id")int id);

    @GET("getRecommendation/{id}")
    Call<List<Recommendation>> recommendation(@Path("id")int id);

    @GET("getPregnancyDate/{id}")
    Call<PregnancyDate> getPregDate(@Path("id")int id);
}
