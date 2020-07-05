package com.key.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.key.myapplication.models.settings_model;
import com.key.myapplication.onboarding.get_user_images;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.key.myapplication.login.signup.MEDIA_TYPE;

public class settings extends Fragment implements View.OnClickListener {


    @BindView(R.id.se_college)
    TextView se_college;
    @BindView(R.id.go_to_intrests)
    TextView go_to_intrests;
    @BindView(R.id.se_name)
    TextView se_name;
    @BindView(R.id.se_pet_name)
    TextView se_pet_name;
    @BindView(R.id.se_birth_date)
    TextView se_birth_date;
    @BindView(R.id.se_age_range_min)
    TextView se_age_range_min;
    @BindView(R.id.se_age_range_max)
    TextView se_age_range_max;
    @BindView(R.id.go_to_posts)
    TextView go_to_posts;

    @BindView(R.id.se_summary)
    EditText se_summary;
    @BindView(R.id.se_q1_ans)
    EditText se_q1_ans;
    @BindView(R.id.se_q2_ans)
    EditText se_q2_ans;
    @BindView(R.id.se_q3_ans)
    EditText se_q3_ans;

    @BindView(R.id.se_q1)
    TextView se_q1;
    @BindView(R.id.se_q2)
    TextView se_q2;
    @BindView(R.id.se_q3)
    TextView se_q3;


    @BindView(R.id.se_phone_number)
    TextView se_phone_number;
    @BindView(R.id.se_main_address)
    EditText se_main_address;
    @BindView(R.id.se_state)
    EditText se_state;
    @BindView(R.id.se_pincode)
    EditText se_pincode;


    @BindView(R.id.se_real_image)
    CircleImageView se_real_image;
    ImageView se_real_img_cross;
    ImageView se_bitmoji_img_cross;

    @BindView(R.id.se_show_sex)
    RadioGroup se_show_sex;
    @BindView(R.id.se_user_gender)
    TextView se_user_gender;

    @BindView(R.id.se_show_male)
    RadioButton se_show_male;
    @BindView(R.id.se_show_female)
    RadioButton se_show_female;
    @BindView(R.id.se_show_everyone)
    RadioButton se_show_everyone;


    @BindView(R.id.se_age_range)
    CrystalRangeSeekbar se_age_range;


    @BindView(R.id.save_settings)
    Button save_settings;
    @BindView(R.id.pencil)
    ImageView pencil;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    settings_model user_data;
    ImagePicker imagePicker;
    String fileList = "";
    private String TOKEN;
    boolean ALLOW_ANONYMOUS_MESSAGING;
    boolean allow_gifts;
    String SEX_SELECTED = "";
    private BubbleNavigationLinearView bubbleNavigation;
    private Switch allow_anonymous;
    private String USER_AVATAR;
    private String SHOW_SEX_SELECTED = "";
    String USER_REAL_IMAGE = "", USER_NAME, PET_NAME, USER_SEX, USER_SHOW_SEX, DOB, PHONE_NUMBER;
    private SwipeRefreshLayout refresh_social;
    private Unbinder unbinder;
    View v;
    @BindView(R.id.profile_link)
     TextView profile_link;
    private String USER_SHARE_LINK,USER_ID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_settings, container, false);

        unbinder = ButterKnife.bind(this, v);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = prefs.edit();
        TOKEN = prefs.getString("TOKEN", "NO_TOKEN_PRESENT");
        USER_AVATAR = prefs.getString("USER_AVATAR", "USER_AVATAR");
        USER_REAL_IMAGE = prefs.getString("USER_REAL_IMAGE", "USER_REAL_IMAGE");
        ALLOW_ANONYMOUS_MESSAGING = prefs.getBoolean("ALLOW_ANONYMOUS_MESSAGING", true);
        USER_ID = prefs.getString("USER_ID", "USER_ID_NOT_PRESENT");

        USER_NAME = prefs.getString("USER_NAME", "USER_REAL_IMAGE");
        PET_NAME = prefs.getString("PET_NAME", "USER_REAL_IMAGE");
        USER_SEX = prefs.getString("USER_SEX", "male");
        USER_SHOW_SEX = prefs.getString("USER_SHOW_SEX", "female");
        DOB = prefs.getString("DOB", "USER_REAL_IMAGE");
        PHONE_NUMBER = prefs.getString("PHONE_NUMBER", "USER_REAL_IMAGE");
        USER_SHARE_LINK = prefs.getString("USER_SHARE_LINK","USER_SHARE_LINK");


        // setonclicklisternser
        pencil.setOnClickListener(this);
        go_to_intrests.setOnClickListener(this);
        go_to_posts.setOnClickListener(this);
        se_real_image.setOnClickListener(this);
        profile_link.setOnClickListener(this);

        // create a share link for the user


            generate_deeplink();


        //todo show gender
        se_show_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.se_show_male) {
                    SHOW_SEX_SELECTED = "male";

                    se_show_male.setBackgroundResource(R.drawable.pink_button);
                    se_show_female.setBackgroundResource(R.drawable.red_text_box);
                    se_show_everyone.setBackgroundResource(R.drawable.red_text_box);

                    se_show_male.setTextColor(getContext().getColor(R.color.white));
                    se_show_female.setTextColor(getContext().getColor(R.color.pink));
                    se_show_everyone.setTextColor(getContext().getColor(R.color.pink));
                } else if (checkedId == R.id.se_show_female) {
                    SHOW_SEX_SELECTED = "female";
                    se_show_female.setBackgroundResource(R.drawable.pink_button);
                    se_show_male.setBackgroundResource(R.drawable.red_text_box);
                    se_show_everyone.setBackgroundResource(R.drawable.red_text_box);

                    se_show_male.setTextColor(getContext().getColor(R.color.pink));
                    se_show_female.setTextColor(getContext().getColor(R.color.white));
                    se_show_everyone.setTextColor(getContext().getColor(R.color.pink));
                } else {
                    SHOW_SEX_SELECTED = "everyone";
                    se_show_everyone.setBackgroundResource(R.drawable.pink_button);
                    se_show_female.setBackgroundResource(R.drawable.red_text_box);
                    se_show_male.setBackgroundResource(R.drawable.red_text_box);

                    se_show_male.setTextColor(getContext().getColor(R.color.pink));
                    se_show_female.setTextColor(getContext().getColor(R.color.pink));
                    se_show_everyone.setTextColor(getContext().getColor(R.color.white));
                }
            }

        });

        se_age_range.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                se_age_range_min.setText(String.valueOf(minValue));
                se_age_range_max.setText(String.valueOf(maxValue));
            }
        });
        save_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_settings("personal/update_profile/");
            }
        });


        se_name.setText(prefs.getString("USER_NAME", "USER_NAME"));
        se_pet_name.setText(prefs.getString("PET_NAME", "PET_NAME"));
        se_birth_date.setText(prefs.getString("DOB", "DOB"));

        se_phone_number.setText(prefs.getString("PHONE_NUMBER", "PHONE_NUMBER"));
        get_settings("personal/view/");
        return v;
    }


    private void set_view(View v) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                se_name.setText(prefs.getString("USER_NAME", "USER_NAME"));
                se_pet_name.setText(prefs.getString("PET_NAME", "PET_NAME"));
                se_birth_date.setText(prefs.getString("DOB", "DOB"));

                se_phone_number.setText(prefs.getString("PHONE_NUMBER", "PHONE_NUMBER"));

                se_college.setText(user_data.getCollege());

                se_summary.setText(user_data.getDescription().getAnswer());
                se_age_range_min.setText(user_data.getLow_age());
                se_age_range_max.setText(user_data.getHigh_age());
                se_summary.setText(user_data.getDescription().getAnswer());

                se_q1_ans.setText(user_data.getAnswers()[0].getAnswer());
                se_q2_ans.setText(user_data.getAnswers()[1].getAnswer());
                se_q3_ans.setText(user_data.getAnswers()[2].getAnswer());

                se_q1.setText(user_data.getAnswers()[0].getQuestion());
                se_q2.setText(user_data.getAnswers()[1].getQuestion());
                se_q3.setText(user_data.getAnswers()[2].getQuestion());


                // setting of age range


                se_age_range.setMinStartValue(Float.parseFloat(user_data.getLow_age()))
                        .setMaxStartValue(Float.parseFloat(user_data.getHigh_age())).apply();


                // user gender set
             se_user_gender.setText(USER_SEX);

                // set gender choice
                switch (USER_SHOW_SEX) {
                    case "male":
                        SHOW_SEX_SELECTED = "male";

                        se_show_male.setBackgroundResource(R.drawable.pink_button);
                        se_show_male.setTextColor(getContext().getColor(R.color.white));
                        break;
                    case "female":
                        SHOW_SEX_SELECTED = "female";
                        se_show_female.setBackgroundResource(R.drawable.pink_button);
                        se_show_female.setTextColor(getContext().getColor(R.color.white));
                        break;
                    case "everyone":
                        SHOW_SEX_SELECTED = "everyone";
                        se_show_everyone.setBackgroundResource(R.drawable.pink_button);
                        se_show_everyone.setTextColor(getContext().getColor(R.color.white));

                }


                allow_gifts = user_data.getAllow_gifts();
                allow_anonymous = v.findViewById(R.id.allow_se_anonymous);
                if (ALLOW_ANONYMOUS_MESSAGING) {
                    allow_anonymous.setChecked(true);
                } else {
                    allow_anonymous.setChecked(false);
                }
                allow_anonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        allow_gifts = isChecked;

                    }
                });

                if (user_data.getImages()[0].isEmpty()) {
                    Glide.with(getContext()).load(getContext().getResources().getString(R.string.BASE_URL) + user_data.getImages()[1]).into(se_real_image);


                } else if (user_data.getImages()[1].isEmpty()) {
                    Glide.with(getContext()).load(getContext().getResources().getString(R.string.BASE_URL) + user_data.getImages()[0]).into(se_real_image);

                }
            }

        });


    }


    private void get_settings(String view_url) {

        final OkHttpClient client = new OkHttpClient();
        Map<String, String> params = new HashMap();
        params.put("token", TOKEN);

        JSONObject parameters = new JSONObject(params);
        String url = getResources().getString(R.string.BASE_URL) + view_url;
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


                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                JSONObject json = new JSONObject(mMessage);
                                if (json.getBoolean("success")) {
                                    JSONObject data = json.getJSONObject("data");
                                    user_data = new Gson().fromJson(String.valueOf(data), settings_model.class);
                                    editor.putString("MAIN_ADDRESS", se_main_address.getText().toString()); // Storing string
                                    editor.putString("PINCODE", se_pincode.getText().toString());
                                    editor.putString("STATE", se_state.getText().toString());
                                    editor.putString("PHONE_NUMBER", se_phone_number.getText().toString());
                                    editor.putString("USER_SEX", user_data.getGender());
                                    editor.putString("DOB", user_data.getBirth_date());
                                    editor.putBoolean("ALLOW_ANONYMOUS_MESSAGING", user_data.getAllow_gifts());
                                    editor.putString("USER_NAME", user_data.getName());
                                    editor.putString("PET_NAME", user_data.getPet_name());
                                    editor.apply();
                                    set_view(v);
                                } else {
                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });


                }

            }
        });


    }


    private void save_settings(String view_url) {

        FirebaseAnalytics.getInstance(getContext()).setUserProperty("gender_pref", SHOW_SEX_SELECTED);
        FirebaseAnalytics.getInstance(getContext()).setUserProperty("max_age", se_age_range_max.getText().toString());
        FirebaseAnalytics.getInstance(getContext()).setUserProperty("min_age", se_age_range_min.getText().toString());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final OkHttpClient client = new OkHttpClient();
                Map<String, Object> params = new HashMap();
                params.put("token", TOKEN);
                //address
                params.put("address", se_main_address.getText().toString());
//        params.put("pincode", se_pincode.getText().toString());
                params.put("phone", se_phone_number.getText().toString());

                // age range
                params.put("max_age", se_age_range_max.getText().toString());
                params.put("min_age", se_age_range_min.getText().toString());

                // show gender
                params.put("show", SHOW_SEX_SELECTED);
                editor.putString("USER_SHOW_SEX", SHOW_SEX_SELECTED);
                editor.apply();

                // name pet name desc
                params.put("name", USER_NAME);
                params.put("pet_name", PET_NAME);

                // desc
                Gson gson = new Gson();
                user_data.getDescription().setAnswer(se_summary.getText().toString().replace("'","").trim());
                params.put("description", gson.toJson(user_data.getDescription()));

                // 3 questions
                user_data.getAnswers()[0].setAnswer(se_q1_ans.getText().toString().replace("'","").trim());
                user_data.getAnswers()[1].setAnswer(se_q2_ans.getText().toString().replace("'","").trim());
                user_data.getAnswers()[2].setAnswer(se_q3_ans.getText().toString().replace("'","").trim());
                params.put("answers", gson.toJson(user_data.getAnswers()));


                // allow gifts
                params.put("allow_gifts", allow_gifts);
                editor.putBoolean("ALLOW_ANONYMOUS_MESSAGING", allow_gifts);
                editor.apply();


                JSONObject parameters = new JSONObject(params);
                String url = getResources().getString(R.string.BASE_URL) + view_url;
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

                            Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "Profile Saved", Snackbar.LENGTH_LONG);
                            snackbar.show();


                        }

                    }
                });

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void generate_deeplink() {
        String id = null;
        try {
            String password ="123456789";
            id = AESCrypt.encrypt(password,USER_ID);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                // .setLink(dynamicLinkUri)
                .setLink(Uri.parse("https://closerdating.in/profile_id="+id))
                .setDomainUriPrefix("https://app.closerdating.in")

                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
//                .setSocialMetaTagParameters(
//                        new DynamicLink.SocialMetaTagParameters.Builder()
//                                .setTitle("Closer Social dating app for college kids")
//                                .setDescription("Hi send me anonymous message and I will respond you back")
//                                .build())
                .buildDynamicLink();
                String url =  dynamicLink.getUri().toString();
                editor.putString("USER_SHARE_LINK",url);
                editor.apply();
                // profile_link.setText(USER_SHARE_LINK);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pencil:
            case R.id.se_real_image:
                Intent intent = new Intent(getContext(), get_user_images.class);
                intent.putExtra("from_settings", true);
                startActivity(intent);
                break;
            case R.id.go_to_intrests:
                Intent intent2 = new Intent(getContext(), settings_interests.class);
                Gson gson = new Gson();
                intent2.putExtra("interest", gson.toJson(user_data));
                startActivity(intent2);
                break;
            case R.id.go_to_posts:
                Intent intent3 = new Intent(getContext(), single_profile.class);
                startActivity(intent3);
                break;
            case R.id.profile_link:
                Toast.makeText(getContext(), "Ready to share", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(android.content.Intent.ACTION_SEND);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent4.putExtra(Intent.EXTRA_TEXT,"Message anonymously me on the closer app "+USER_SHARE_LINK);
                intent4.setType("text/plain");
                getContext().startActivity(Intent.createChooser(intent4, "Share profile via"));



        }
    }
}
