package com.key.myapplication.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.key.myapplication.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.shaohui.bottomdialog.BaseBottomDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.key.myapplication.login.signup.MEDIA_TYPE;

public class forget_password extends BaseBottomDialog {
    EditText forget_user_name;
    Button reset_next;
    SharedPreferences prefs;// 0 - for private mode
    private String TOKEN = "";

        LottieAnimationView hloader;

    public int getLayoutRes() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void bindView(View v) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TOKEN = prefs.getString("TOKEN", "NO_TOKEN_PRESENT");
          hloader = v.findViewById(R.id.hloader);
        forget_user_name = v.findViewById(R.id.forget_user_name);
        forget_user_name.post(new Runnable() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(forget_user_name, 0);
            }

        });

        reset_next = v.findViewById(R.id.reset_next);
        reset_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!forget_user_name.getText().toString().isEmpty())
                    sendMail();
            }
        });
    }

    private void sendMail() {
        hloader.setVisibility(View.VISIBLE);
        hloader.playAnimation();
        Bundle fb = new Bundle();
        fb.putString("forget_password", "forget");
        FirebaseAnalytics.getInstance(getActivity()).logEvent("forget_password", fb);

        final OkHttpClient client = new OkHttpClient();
        Map<String, String> params = new HashMap<>();
        params.put("user_mail", forget_user_name.getText().toString());
        JSONObject parameters = new JSONObject(params);
        String url = getResources().getString(R.string.BASE_URL) + "forgot_password/";
        RequestBody body = RequestBody.create(MEDIA_TYPE, parameters.toString());

        final okhttp3.Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hloader.setVisibility(View.INVISIBLE);
                            hloader.cancelAnimation();
                        }
                    });
                    //Toast.makeText(search.this, "Something Is Wrong", Toast.LENGTH_SHORT).show();
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hloader.setVisibility(View.INVISIBLE);
                            hloader.cancelAnimation();
                            Toast.makeText(getContext(), "This email is not registered with us", Toast.LENGTH_SHORT).show();
                        }
                    });
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
