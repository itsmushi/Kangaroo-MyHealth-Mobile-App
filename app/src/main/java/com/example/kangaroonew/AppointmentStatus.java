//package com.example.kangaroonew;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.Log;
//import androidx.annotation.Nullable;
//import com.example.kangaroonew.models.AppointmentClass;
//import com.example.kangaroonew.models.Hospital;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.pusher.client.Pusher;
//import com.pusher.client.PusherOptions;
//import com.pusher.client.channel.Channel;
//import com.pusher.client.channel.PusherEvent;
//import com.pusher.client.channel.SubscriptionEventListener;
//import com.pusher.client.connection.ConnectionEventListener;
//import com.pusher.client.connection.ConnectionState;
//import com.pusher.client.connection.ConnectionStateChange;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//import java.util.List;
//
//
//public class AppointmentStatus extends IntentService {
//
//
//    private static final String ACTION_RESP="";
//    JsonApiPlaceholder jsonPlaceHolder;
//    Channel channel;
//
//    boolean found=false;
//    public boolean toStop;
//
//
//    public AppointmentStatus() {
//        super("AppointmentStatus");
//    }
//
//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//
//        int userID=intent.getExtras().getInt("userID");
//
//
//        Gson gson =new GsonBuilder().serializeNulls().create(); //makes gson take into account nulls when they are mentioned
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://kangaroobackend.herokuapp.com/api/")
//                .addConverterFactory(GsonConverterFactory.create(gson))
////                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//
//        //create interface reference
//        jsonPlaceHolder=retrofit.create(JsonApiPlaceholder.class);
//
//
////        PusherOptions options = new PusherOptions();
////        options.setCluster("ap2");
////
////        Pusher pusher = new Pusher("0142d7b36029ce43702e", options);
////
////        pusher.connect(new ConnectionEventListener() {
////            @Override
////            public void onConnectionStateChange(ConnectionStateChange change) {
////                Log.i("Pusher", "State changed from " + change.getPreviousState() +
////                        " to " + change.getCurrentState());
////            }
////
////            @Override
////            public void onError(String message, String code, Exception e) {
////                Log.i("Pusher", "There was a problem connecting! " +
////                        "\ncode: " + code +
////                        "\nmessage: " + message +
////                        "\nException: " + e
////                );
////            }
////        }, ConnectionState.ALL);
////
////        String channelName="user"+userID;
////        Log.d("Pusher","channel is "+channelName);
////
////
////
////            channel=pusher.subscribe(channelName);
////
////            channel.bind("my-event", new SubscriptionEventListener() {
////                @Override
////                public void onEvent(PusherEvent event) {
////                    Log.i("Pusher", "Received event with data: " + event.toString());
////                }
////            });
//
//
//
//
////        while(true){
////            Log.d("sdf","service running ");
////            final Call<List<AppointmentClass>> appointmentList=jsonPlaceHolder.userAppointments(userID);
////            appointmentList.enqueue(new Callback<List<AppointmentClass>>() {
////                @Override
////                public void onResponse(Call<List<AppointmentClass>> call, Response<List<AppointmentClass>> response) {
////                    if(response.isSuccessful()){
////
////                        List<AppointmentClass> appointmentList=response.body();
////                        for(AppointmentClass appointment: appointmentList){
////
////                            Log.d("Ds","status is "+appointment.getStatus());
////                            if(TextUtils.equals(appointment.getStatus(),"1")){   //the appointment is accepted
////                            Intent broadcastIntent = new Intent();
////                            broadcastIntent.setAction(AppointmentStatus.ACTION_RESP);
////                            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
////                            broadcastIntent.putExtra("resp",appointment.getId());
////                                Log.d("Ds","status in "+appointment.getId());
////
////                            sendBroadcast(broadcastIntent);
//////                            found=true;
//////                            Log.d("SDf","broadcast sent");
//////                            stopSelf();
////                        }
////                        }
////                    }
////                }
////
////                @Override
////                public void onFailure(Call<List<AppointmentClass>> call, Throwable t) {
////
////                }
////            });
////
////            try {
////                Thread.sleep(2000);
////
////
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////
//////            if(found){
//////                break;
//////            }
////        }
////
//
//    }
//}
