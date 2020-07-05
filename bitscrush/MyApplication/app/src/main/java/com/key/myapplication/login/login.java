package com.key.myapplication.login;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.key.myapplication.R;
import com.key.myapplication.main_screen.main_screen;
import com.key.myapplication.onboarding.get_user_details;
import com.key.myapplication.onboarding.get_user_images;
import com.key.myapplication.onboarding.user_intrests;
import com.key.myapplication.top_screen;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.shaohui.bottomdialog.BaseBottomDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.key.myapplication.login.signup.MEDIA_TYPE;
import static com.key.myapplication.login.splash_screen.city;
import static com.key.myapplication.login.splash_screen.country;

public class login extends BaseBottomDialog {


    private static final int REQUEST_LOCATION = 10000;
    TextView forget_password;
    Button login;
    String encrypted_login_password;
    EditText username, password;
    SharedPreferences.Editor editor;
    private String TAG = "login_tagggggggggg";
    SharedPreferences prefs;// 0 - for private mode
    LocationListener locationListener = null;
    LocationManager locationManager;
    Location location;
    String currentLocation;
    String current_locality;
    Geocoder geocoder;
    private KProgressHUD progressHUD;
    private String TOKEN;

      LottieAnimationView hloader;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public void bindView(View v) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        TOKEN = prefs.getString("TOKEN", "NO_TOKEN_PRESENT");
        //setCurrentLocation();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefs.edit();
         hloader=v.findViewById(R.id.hloader);

        username = v.findViewById(R.id.login_email);
        username.post(new Runnable() {

            @Override

            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(username, 0);
            }

        });
        password = v.findViewById(R.id.login_password);

        password.post(new Runnable() {

            @Override

            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(password, 0);
            }

        });


        String user_password = password.getText().toString();

        login = v.findViewById(R.id.login_next);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Log.d("lllllllllllllllllllll", encrypted_login_password);
                if(password.length()<=6){
                    password.setError("Password should be greater than 6 characters");
                }else if(username.length()==0 ){
                    username.setError("College mails are only allowed.");
                }else{
                    checkLogin("register/authenticate/login/");
                }
            }
        });

        v.findViewById(R.id.forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.key.myapplication.login.forget_password dialog = new forget_password();
                dialog.show(getActivity().getSupportFragmentManager());
            }
        });
    }




    public void checkLogin(String login_url) {
        hloader.setVisibility(View.VISIBLE);
        hloader.playAnimation();
        final OkHttpClient client = new OkHttpClient();
        Map<String, String> params = new HashMap();
        params.put("email", username.getText().toString());
        params.put("password", password.getText().toString());
        params.put("firebase_notification_id", FirebaseInstanceId.getInstance().getToken());

        JSONObject parameters = new JSONObject(params);
        String url = getResources().getString(R.string.BASE_URL) + login_url;


        RequestBody body = RequestBody.create(MEDIA_TYPE, parameters.toString());

        final okhttp3.Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "form-data")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
//                Toast.makeText(getActivity(), "" + mMessage, Toast.LENGTH_SHORT).show();
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String mMessage = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        // todo add a is_disabled boolean here so that that email can't use the app

                        JSONObject json = new JSONObject(mMessage);
                        String key = json.getString("key");
                        editor.putString("TOKEN", key); // Storing string
                        editor.apply();
                        ttf();


//                        editor.putString("USER_ID",user_id);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
//                }else if(json.getBoolean("disabled")){
//
//                }


                else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hloader.setVisibility(View.INVISIBLE);
                            hloader.cancelAnimation();
                            Toast.makeText(getContext(), "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }



    public void ttf() {
        String TOKEN = prefs.getString("TOKEN", "NO_TOKEN_PRESENT");
        final OkHttpClient client = new OkHttpClient();
        Map<String, String> params = new HashMap();

        params.put("token", TOKEN);
        params.put("device", FirebaseInstanceId.getInstance().getToken());
        params.put("city", city);
        params.put("state", splash_screen.state);
        params.put("country", country);
        JSONObject parameters = new JSONObject(params);
        String url = getResources().getString(R.string.BASE_URL) + "personal/get_activity/";//todo ask url from arpit
        RequestBody body = RequestBody.create(MEDIA_TYPE, parameters.toString());
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                // Toast.makeText(get_user_details.this, "" + mMessage, Toast.LENGTH_SHORT).show();
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String mMessage = response.body().string();
                if (response.isSuccessful()) {

                    try {
                        JSONObject json = new JSONObject(mMessage);
                        if (json.getBoolean("success"))
                        {
                            editor.putBoolean("ALLOW_ANONYMOUS_MESSAGING", json.getBoolean("allow_gifts"));
                            editor.putString("FEED_TOKEN", json.getString("feed_token"));
                            editor.putString("FEED_PROFILE_ID", json.getString("profile_id"));
                            editor.putString("USER_ID", json.getString("user_id"));
                            editor.putString("PHONE_NUMBER", json.getString("phone"));
                            editor.putString("EMAIL", json.getString("email"));
                            editor.apply();
                            if (!json.getBoolean("desc")) {
                                Intent intent = new Intent(getActivity(), get_user_details.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else if (!json.getBoolean("interests")) {
                                Intent intent = new Intent(getActivity(), user_intrests.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else if (!json.getBoolean("image")) {
                                Intent intent = new Intent(getActivity(), get_user_images.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Intent intent = new Intent(getActivity(), top_screen.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            hloader.setVisibility(View.INVISIBLE);
                            hloader.cancelAnimation();
                        }
                        else if (!json.isNull("blocked")) {
                            if (json.getBoolean("blocked")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                        alertDialogBuilder.setMessage("You have been blocked from app.Please contact admin at admin@closerdating.in if you feel this is wrong !");
                                        alertDialogBuilder.setCancelable(false);
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
                                });
                            }

                        } else if (!json.isNull("disabled")) {
                            if (json.getBoolean("disabled")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                        alertDialogBuilder.setMessage("You have disabled your account ");
                                        alertDialogBuilder.setCancelable(false);
                                        alertDialogBuilder.setPositiveButton("Enable it",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {

                                                        enable_profile();
                                                        //todo un disable mail sending send_undisable_mail("register/resend_verification_mail/");

                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
                                });

                            }

                        }

                        } catch(Exception e){
                            SharedPreferences settings = getActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE);
                            settings.edit().clear().apply();
                            e.printStackTrace();
                        }


                }
            }
        });
    }

    private void enable_profile() {
        final OkHttpClient client = new OkHttpClient();
        Map<String, String> params = new HashMap();
        params.put("token", TOKEN);

        JSONObject parameters = new JSONObject(params);
        String url = getResources().getString(R.string.BASE_URL) + "personal/enable_user/";
        RequestBody body = RequestBody.create(MEDIA_TYPE, parameters.toString());
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                // Toast.makeText(get_user_details.this, "" + mMessage, Toast.LENGTH_SHORT).show();
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String mMessage = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(mMessage);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    @Override
    public float getDimAmount() {
        return 0.9f;

    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
