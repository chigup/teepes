package com.key.myapplication.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.key.myapplication.R;
import com.key.myapplication.onboarding.get_user_details;
import com.key.myapplication.onboarding.get_user_images;
import com.key.myapplication.onboarding.user_intrests;
import com.key.myapplication.top_screen;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.shaohui.bottomdialog.BaseBottomDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.key.myapplication.login.splash_screen.city;
import static com.key.myapplication.login.splash_screen.country;

public class signup extends BaseBottomDialog {
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    private static final int REQUEST_LOCATION = 10000;
    EditText user_name, user_password, college;
    Button signup;
    String encrypted_password, user_mail;
    login login_class;
    SharedPreferences prefs;// 0 - for private mode
    SharedPreferences.Editor editor;
    //public static final MediaType MEDIA_TYPE = MediaType.parse("form-data");
// firebase sign up
    private FirebaseAuth mAuth;
    private Boolean is_college;
    private Boolean register;
    KProgressHUD progressHUD;
    LocationListener locationListener = null;
    LocationManager locationManager;
    Location location;
    String currentLocation;
    String current_locality;
    LottieAnimationView hloader;
    //user_model user_model;
    Geocoder geocoder;
    private String TAG = "login_tagggggggggg";


    @Override
    public int getLayoutRes() {
        return R.layout.activity_signup;
    }

    @Override
    public void bindView(View v) {
        // ...
// Initialize Firebase Auth
        //  setCurrentLocation();
        mAuth = FirebaseAuth.getInstance();
        hloader = v.findViewById(R.id.hloader);

        user_name = v.findViewById(R.id.signup_username);
        user_name.post(new Runnable() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(user_name, 0);
            }

        });


        user_password = v.findViewById(R.id.signup_password);
        user_password.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(user_password, 0);

            }

        });

        college = v.findViewById(R.id.college);
        college.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        signup = v.findViewById(R.id.sign_up);
        user_mail = user_name.getText().toString();
        login_class = new login();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        editor = prefs.edit();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //signup karao


                //  Log.d("lllllllllllllllllllll", encrypted_login_password);
                if (user_password.length() <= 6) {
                    user_password.setError("password should be greater than 6 characters");
                } else if (user_name.getText().toString().contains(" ")||is_general_mail()||user_name.length() == 0) {
                        user_name.setError("Enter a valid college email ID");
                } else {
                    mAuth.createUserWithEmailAndPassword(user_name.getText().toString(), user_password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //  updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        // Toast.makeText(getActivity(), "User Already Exists", Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });
                    do_signup("register/create_user/");
                }


            }
        });
    }

    private boolean is_general_mail() {
        String mail_ext = user_name.getText().toString().split("@")[1];
        switch (mail_ext) {
            case "gmail.com":
            case "yahoo.com":
            case "outlook.com":
                return true;

        }
        return false;
    }


    @Override
    public float getDimAmount() {
        return 0.9f;

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void do_signup(String sign_up_url) {
        hloader.setVisibility(View.VISIBLE);
        hloader.playAnimation();
        final OkHttpClient client = new OkHttpClient();
        Map<String, String> params = new HashMap();
        params.put("email", user_name.getText().toString());
        params.put("password", user_password.getText().toString());

        JSONObject parameters = new JSONObject(params);
        String url = getResources().getString(R.string.BASE_URL) + sign_up_url;


        RequestBody body = RequestBody.create(MEDIA_TYPE, parameters.toString());

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "form-data")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                //   Toast.makeText(login_class, "" + mMessage, Toast.LENGTH_SHORT).show();
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String mMessage = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(mMessage);
                        //final String serverResponse = json.getString("Your Index");
                        is_college = json.getBoolean("college");
                        register = json.getBoolean("registered");
                        if (!is_college) {

                            // Toast.makeText(getActivity(), "Sorry, we're still not present in your college", Toast.LENGTH_LONG).show();
                            college_not_present dialog = new college_not_present();
                            Bundle bundle = new Bundle();
                            bundle.putString("email_id", user_name.getText().toString());
                            dialog.setArguments(bundle);
                            dialog.show(getFragmentManager());
                            hloader.setVisibility(View.INVISIBLE);
                            hloader.cancelAnimation();


                        } else if (register == false) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "User Already Exists!", Toast.LENGTH_SHORT).show();
                                    hloader.setVisibility(View.INVISIBLE);
                                    hloader.cancelAnimation();
                                }
                            });

                        }else
                         {
                            do_login("register/authenticate/login/");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hloader.setVisibility(View.INVISIBLE);
                            hloader.cancelAnimation();
                            Toast.makeText(getContext(), "something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

    }

    //todo there are two different login function change in both if in one
    private void do_login(String login_url) {
        final OkHttpClient client = new OkHttpClient();
        Map<String, String> params = new HashMap();
        params.put("email", user_name.getText().toString());
        params.put("password", user_password.getText().toString());
        params.put("firebase_notification_id", FirebaseInstanceId.getInstance().getToken());

        JSONObject parameters = new JSONObject(params);
        String url = getResources().getString(R.string.BASE_URL) + login_url;


        RequestBody body = RequestBody.create(MEDIA_TYPE, parameters.toString());

        final Request request = new Request.Builder()
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

                        JSONObject json = new JSONObject(mMessage);
                        String key = json.getString("key");
                        editor.putString("TOKEN", key); // Storing string
                        editor.apply();

                        ttf();

//                        editor.putString("USER_ID",user_id);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hloader.setVisibility(View.INVISIBLE);
                            hloader.cancelAnimation();
                            Toast.makeText(getContext(), "something is wrong", Toast.LENGTH_SHORT).show();
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
        String url = getResources().getString(R.string.BASE_URL) + "personal/get_activity/";
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
                        if (json.getBoolean("success")) {
                            editor.putString("FEED_TOKEN", json.getString("feed_token"));
                            editor.putString("FEED_PROFILE_ID", json.getString("profile_id"));
                            editor.putString("USER_ID", json.getString("user_id"));
                            editor.putString("PHONE_NUMBER", json.getString("phone"));
                            editor.putString("EMAIL", json.getString("email"));
                            editor.putBoolean("ALLOW_ANONYMOUS_MESSAGING", json.getBoolean("allow_gifts"));
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
                        }

                        hloader.setVisibility(View.INVISIBLE);
                        hloader.cancelAnimation();

                    } catch (Exception e) {
                        SharedPreferences settings = getActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE);
                        settings.edit().clear().apply();
                        e.printStackTrace();
                    }


                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hloader.setVisibility(View.INVISIBLE);
                            hloader.cancelAnimation();
                            Toast.makeText(getContext(), "This email is not registered or something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }


}
