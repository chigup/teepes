package com.key.myapplication.adapters;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.key.myapplication.R;
import com.key.myapplication.decode_avatar;
import com.key.myapplication.login.MainActivity;
import com.key.myapplication.login.splash_screen;
import com.key.myapplication.main_screen.feed.feed_adapter;
import com.key.myapplication.main_screen.feed.other_user_comment_bottom_sheet;
import com.key.myapplication.main_screen.swipe.report_user;
import com.key.myapplication.main_screen.swipe.swipe_activity;
import com.key.myapplication.match_pop_up;
import com.key.myapplication.models.matched_user_model;
import com.key.myapplication.models.profiles;
import com.key.myapplication.notifications.user_address;
import com.key.myapplication.pop_up_questions.questions_pop_up_screen;
import com.key.myapplication.sending_gift.explaination;
import com.key.myapplication.user_details;
import com.scottyab.aescrypt.AESCrypt;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.key.myapplication.login.signup.MEDIA_TYPE;

public class profiles_adapter extends RecyclerView.Adapter<profiles_adapter.MyViewHolder> {
    Context context;
    List<profiles> items;
    SharedPreferences prefs;
    private String TOKEN, USER_ID;
    private String FEED_TOKEN;
    private String FEED_PROFILE_ID;
    private ClickListener clicklistener;
    RecyclerView recyclerView;
    CircularProgressDrawable pic_indicator;
    public profiles_adapter(Context context, List<profiles> items, RecyclerView recyclerView) {
        this.context = context;

        this.recyclerView = recyclerView;
        this.items = items;
    }


    @NonNull
    @Override
    public profiles_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards, parent, false);
        return new profiles_adapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final profiles_adapter.MyViewHolder holder, int position) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        TOKEN = prefs.getString("TOKEN", "NO_TOKEN_PRESENT");
        USER_ID = prefs.getString("USER_ID", "USER_ID_NOT_PRESENT");
        FEED_TOKEN = prefs.getString("FEED_TOKEN", "NO_TOKEN_PRESENT");
        FEED_PROFILE_ID = prefs.getString("FEED_PROFILE_ID", "NO_TOKEN_PRESENT");
        profiles profile = items.get(position);
        String base = context.getResources().getString(R.string.BASE_URL);
        String url1 = "";
         pic_indicator= new CircularProgressDrawable(context);
        pic_indicator.setStrokeWidth(5f);
        pic_indicator.setCenterRadius(70f);
        pic_indicator.setProgressRotation(100f);
        pic_indicator.setColorSchemeColors(context.getColor(R.color.pink));
        pic_indicator.start();
        if (profile.getImages()[0].isEmpty()) {
            // call a sexy function to decode the string of bitmoji
            url1 = base + profile.getImages()[1].substring(1);
            Glide.with(context)
                    .load(url1) //passing your url to load image.
                    .thumbnail(.1f)
                    .placeholder(pic_indicator)
                    .error(R.drawable.ic_user)
                    .transition(DrawableTransitionOptions.withCrossFade()) // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                    .into(holder.profile_pic);

          //  Picasso.get().load(url1).into(holder.profile_pic);

        } else {
            url1 = base + profile.getImages()[0].substring(1);
            Glide.with(context)
                    .load(url1) //passing your url to load image.
                    .thumbnail(.1f)
                    .error(R.drawable.ic_user)
                    .placeholder(pic_indicator)
                    .transition(DrawableTransitionOptions.withCrossFade()) // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                    .into(holder.profile_pic);

        }
        holder.desc.setText(profile.getDescription().getAnswer());
        holder.card_name.setText(profile.getPet_name());
        holder.card_age.setText(String.valueOf(profile.getAge()));
        holder.college_name.setText(profile.getCollege());
        holder.percentage_match.setCurrentProgress(profile.getMatch());
        holder.percentage_match.setProgressTextAdapter(new CircularProgressIndicator.ProgressTextAdapter() {
            @NonNull
            @Override
            public String formatText(double currentProgress) {
                // percentage_match_text.setText((int)(currentProgress) +" %");
                return (((int) currentProgress + " %"));
            }
        });
        //interest
       try{
           holder.in1.setText("# " + profile.getInterests().get(0).getName());
           holder.in2.setText("# " + profile.getInterests().get(1).getName());
           holder.in3.setText("# " + profile.getInterests().get(2).getName());
       } catch (Exception e) {
           e.printStackTrace();
       }
        holder.more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, user_details.class);
                // add data for the user
                intent.putExtra("profile_id", profile.getProfile_id());
                context.startActivity(intent);

                //bundle.putBundle("profile", profile);

            }
        });
        holder.share_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_img(holder.profile_pic, profile);
            }
        });


        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  recyclerView.removeOnItemTouchListener(disabler);
                if (!items.isEmpty()) {

                    holder.white_heart.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.Pulse).duration(500).playOn(holder.white_heart);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.white_heart.setVisibility(View.INVISIBLE);
                        }
                    }, 400);
//

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (position != items.size() - 1) {
                                recyclerView.smoothScrollToPosition(position + 1);
                            }
                        }
                    }, 400);
                  //  recyclerView.addOnItemTouchListener(disabler);
                    postData(profile.getProfile_id(), "swipe/swipe_right/");


                }

            }
        });

        holder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!items.isEmpty()) {
                  //  recyclerView.removeOnItemTouchListener(disabler);
                    holder.white_cross.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.Pulse).duration(500).playOn(holder.white_cross);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.white_cross.setVisibility(View.INVISIBLE);
                        }
                    }, 400);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (position != items.size() - 1) {
                                recyclerView.smoothScrollToPosition(position + 1);
                            }
                        }
                    }, 400);
                  //  recyclerView.addOnItemTouchListener(disabler);
                    postData(profile.getProfile_id(), "swipe/swipe_left/");

//
                }

            }
        });

        holder.b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean is_allowed = items.get(0).isAllow_gifts();
                if (!items.isEmpty()) {
                    if (is_allowed) {
                        Intent intent = new Intent(context, explaination.class);
                        // add data for the user;
                        Gson gson = new Gson();
                        intent.putExtra("profile", gson.toJson(items.get(position)));
                        context.startActivity(intent);
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setMessage("This user is not accepting anonymous messages.");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }

                }

            }
        });

        holder.report_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_user bs = new report_user();
                Bundle args = new Bundle();

                args.putString("profile_id", profile.getProfile_id());

                bs.setArguments(args);
                bs.show(((AppCompatActivity) context).getSupportFragmentManager(), "my_post_bottom_sheet");
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position, String swipe_type);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_pic;
        TextView card_name;
        TextView card_age;
        TextView college_name;
        CircularProgressIndicator percentage_match;
        TextView in1;
        TextView in2;
        TextView in3;
        ImageView more_info, white_heart, white_cross;
        ImageView share_user, report_user;
        FloatingActionButton b1, b2, b3;
        TextView desc;

        public MyViewHolder(View convertView) {
            super(convertView);
            // text views
            profile_pic = convertView.findViewById(R.id.userImageViewPager);
            card_name = convertView.findViewById(R.id.card_name);
            card_age = convertView.findViewById(R.id.card_age);
            college_name = convertView.findViewById(R.id.college_name);
            percentage_match = convertView.findViewById(R.id.percentage_match);
            in1 = convertView.findViewById(R.id.in1);
            in2 = convertView.findViewById(R.id.in2);
            in3 = convertView.findViewById(R.id.in3);
            more_info = convertView.findViewById(R.id.more_info);
            share_user = convertView.findViewById(R.id.share_user);
            b1 = convertView.findViewById(R.id.right_swipe);
            b2 = convertView.findViewById(R.id.left_swipe);
            b3 = convertView.findViewById(R.id.super_like);
            white_heart = convertView.findViewById(R.id.white_heart);
            white_cross = convertView.findViewById(R.id.white_cross);
            desc = convertView.findViewById(R.id.desc);
            report_user = convertView.findViewById(R.id.report_user);

        }
    }


    //     for sharing image
//     for sharing image
    private void share_img(View content, profiles model) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Bitmap bitmap = getBitmapFromView(content);
        try {
            File file = new File(context.getExternalCacheDir(), model.getPet_name() + ".png");

            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            //start share image with intent
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            // get the dynamic link for the users
            String deep_link =generate_deeplink( model);
           // generate_deeplink(model);
            intent.putExtra(Intent.EXTRA_TEXT, "Hey I found  " + model.getPet_name() + " @closerdating_in app go check it out at "+deep_link);
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, "Share profile via"));
        }    catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String generate_deeplink(profiles model) throws GeneralSecurityException {
        String password ="123456789";
        String id = AESCrypt.encrypt(password, model.getProfile_id());
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                // .setLink(dynamicLinkUri)

                .setLink(Uri.parse("https://closerdating.in/profile_id="+id))
                .setDomainUriPrefix("https://app.closerdating.in")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .buildDynamicLink();

       return  dynamicLink.getUri().toString();



    }

    // for adding water mark in image
    public static Bitmap mark(Bitmap src, String watermark, Point location) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        Canvas canvas = new Canvas(result);
        // drawing image
        canvas.drawBitmap(src, 0, 0, paint);


        Paint watermarkPaint = new Paint();
        Paint.FontMetrics fm = new Paint.FontMetrics();
        watermarkPaint.setColor(Color.BLACK);
        watermarkPaint.getFontMetrics(fm);

        watermarkPaint.setAlpha(150);
        watermarkPaint.setTextSize(50);
        watermarkPaint.setTextAlign(Paint.Align.RIGHT);
        watermarkPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        // drawing background
        int margin = 5;
        canvas.drawRect(0, h - 75, w, h, watermarkPaint);

        watermarkPaint.setColor(Color.WHITE);
        // drawing text
        canvas.drawText(watermark, location.x, location.y, watermarkPaint);


        return result;
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        //or use #getDrawable with ImageView
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);

        return mark(returnedBitmap, "Closer", new Point(returnedBitmap.getWidth() - 20, returnedBitmap.getHeight() - 20));
    }

    public void postData(String swiped_user_id, String swipe_url) {

        Bundle fb = new Bundle();
        fb.putString("swiped", swipe_url.split("/")[1]);
        FirebaseAnalytics.getInstance(context).logEvent("number_of_swipes", fb);

        final OkHttpClient client = new OkHttpClient();
        Map<String, String> params = new HashMap();
        params.put("token", TOKEN);
        params.put("profile_id", swiped_user_id
        );
        JSONObject parameters = new JSONObject(params);
        String url = context.getResources().getString(R.string.BASE_URL) + swipe_url;
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
//                 Toast.makeText(get_user_details.this, "" + mMessage, Toast.LENGTH_SHORT).show();
//                call.cancel();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String mMessage = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(mMessage);


                        boolean success = json.getBoolean("success");
                        try {
                            if (success) {
                                boolean matched = json.getBoolean("matched");
                                if (matched) {
                                    // if the user right swiped get matched
                                    // pass users
                                    JSONObject user1 = json.getJSONObject("user1");
                                    JSONObject user2 = json.getJSONObject("user2");
                                    int match_id = json.getInt("match_id");
                                    //TODO make a group of 2 poeple on open fire

                                    matched_user_model model1 = new Gson().fromJson(String.valueOf(user1), matched_user_model.class);
                                    matched_user_model model2 = new Gson().fromJson(String.valueOf(user2), matched_user_model.class);
                                    show_match_popup(model1, model2, match_id);

                                }
                                //show match pop
                            }

                        } catch (Exception e) {
                        }
                        try {
                            if (success) {
                                // these are for getting questions
                                JSONArray questions = json.getJSONArray("questions");
                                if (questions.length() > 0) {
                                    questions_pop_up_screen dialog = new questions_pop_up_screen();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("questions", json.toString());
                                    dialog.setArguments(bundle);
                                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager());
                                }

                                //make questions pop-up
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });

    }

    private void show_match_popup(matched_user_model u1, matched_user_model u2, int match_id) {

        FirebaseDatabase.getInstance().getReference().child("MATCH_ID").child(String.valueOf(match_id));
        match_pop_up dialog = new match_pop_up(match_id, u1, u2);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager());


    }


}



