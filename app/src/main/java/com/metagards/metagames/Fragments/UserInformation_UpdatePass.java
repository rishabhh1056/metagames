package com.metagards.metagames.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.FileUtils;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class UserInformation_UpdatePass extends BottomSheetDialogFragment {


    public UserInformation_UpdatePass() {
        // Required empty public constructor
    }

    Callback callback;
    public UserInformation_UpdatePass(Callback callback) {
        // Required empty public constructor
        this.callback = callback;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };


    View contentView;

    EditText edtText;
    Activity context;
    private String user_id, name, mobile, profile_pic, referral_code, wallet, game_played, bank_detail, adhar_card, upi;
    ProgressDialog progressDialog;
    String base_64 = "", pan_base64="", aadhar_ase64="";


    @NonNull
    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }

    CoordinatorLayout.Behavior behavior;
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
        contentView = View.inflate(getContext(), R.layout.dialog_user_update_pass, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        context = getActivity();

        Intilization();

    }

    ImageView img_diaProfile,img_verified, img_pan, img_aadhar;
    EditText edtUsername,edtUserbank,edtUserupi,edtUseradhar , edt_pan, edt_aadar, edt_oldpass, edt_newpass;
    TextView txt_diaName, txt_status, txt_reason;
    TextView txt_diaPhone;
    TextView txt_bank;
    TextView txt_adhar;
    TextView txt_upi ;
    RadioButton radioBank;
    RadioButton radioUpi;
    Spinner spUserTpye;
    ArrayList<String> UserTpyeList;
    Button upload_pan, upload_aadhar;
    String str_chk="";
    private void Intilization(){

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        // Log.d(TAG, msg);
                        // Funtions.showToast(Homepage.this, token);
                        UserProfile();

                    }
                });


        ((View) contentView.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        img_diaProfile = contentView.findViewById(R.id.img_diaProfile);
        img_verified = contentView.findViewById(R.id.verified);
        txt_diaName = contentView.findViewById(R.id.txt_diaName);
        txt_diaPhone = contentView.findViewById(R.id.txt_diaPhone);
        txt_bank = contentView.findViewById(R.id.txt_bank);
        txt_adhar = contentView.findViewById(R.id.txt_adhar);
        txt_upi = contentView.findViewById(R.id.txt_upi);
        txt_status = contentView.findViewById(R.id.txt_status);
        txt_reason = contentView.findViewById(R.id.txt_reason);

        img_pan = contentView.findViewById(R.id.img_pan);
        img_aadhar = contentView.findViewById(R.id.img_aadhar);


        edtUsername = contentView.findViewById(R.id.edtUsername);
        edt_pan = contentView.findViewById(R.id.edt_pan);
        edt_aadar = contentView.findViewById(R.id.edt_aadar);

        edt_oldpass = contentView.findViewById(R.id.edt_oldpass);
        edt_newpass = contentView.findViewById(R.id.edt_newpass);

        edtUserbank = contentView.findViewById(R.id.edtUserbank);

        edtUserupi = contentView.findViewById(R.id.edtUserupi);
        edtUseradhar = contentView.findViewById(R.id.edtUseradhar);

        upload_pan = contentView.findViewById(R.id.upload_pan);
        upload_aadhar = contentView.findViewById(R.id.upload_aadhar);

        final LinearLayout lnrUserinfo = contentView.findViewById(R.id.lnr_userinfo);
        final LinearLayout lnr_updateuser = contentView.findViewById(R.id.lnr_updateuser);
        lnrUserinfo.setVisibility(View.GONE);
        lnr_updateuser.setVisibility(View.VISIBLE);

        upload_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_chk="0";
                selectImage();
            }
        });

        upload_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_chk="1";
                selectImage();
            }
        });

        img_diaProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ((ImageView) contentView.findViewById(R.id.imgsub)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edt_oldpass.getText().toString().equals("")){
                    Toast.makeText(context, "Please enter your Old Password", Toast.LENGTH_SHORT).show();
                } else if(edt_newpass.getText().toString().equals("")){
                    Toast.makeText(context, "Please enter your New Password", Toast.LENGTH_SHORT).show();
                }
                else {
//                    Functions.showToast(context, "Input field in empty!");
                    UserUpdateProfile();
                }

            }
        });



    }

    public String token = "";
    int version = 0 ;

    private void UserProfile() {

        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("id", prefs.getString("user_id", ""));
        params.put("fcm", token);

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        params.put("app_version", version + "");
        params.put("token", prefs.getString("token", ""));
        Log.d("user_params_", String.valueOf(params));
        ApiRequest.Call_Api(context, Const.PROFILE, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {
//                    ParseResponse(resp);
                }

            }
        });

    }

    private void ParseResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("profile_res", response);
            String code = jsonObject.getString("code");
            if (code.equalsIgnoreCase("200")) {
                JSONObject jsonObject0 = jsonObject.getJSONArray("user_data").getJSONObject(0);
                user_id = jsonObject0.getString("id");
                name = jsonObject0.optString("name");
                mobile = jsonObject0.optString("mobile");
                profile_pic = jsonObject0.optString("profile_pic");
                referral_code = jsonObject0.optString("referral_code");
                wallet = jsonObject0.optString("wallet");
                game_played = jsonObject0.optString("game_played");
                bank_detail = jsonObject0.optString("bank_detail");
                upi = jsonObject0.optString("upi");
                adhar_card = jsonObject0.optString("adhar_card");
                // txtName.setText("Welcome Back "+name);
//                long numberamount = Long.parseLong(wallet);

                edtUsername.setText("" + name);
                edtUserbank.setText("" + bank_detail);
                edtUserupi.setText("" + upi);
                edtUseradhar.setText("" + adhar_card);


                String setting = jsonObject.optString("setting");
                JSONObject jsonObjectSetting = new JSONObject(setting);

                String bank_detail_field = jsonObjectSetting.optString("bank_detail_field");
                String adhar_card_field = jsonObjectSetting.optString("adhar_card_field");
                String upi_field = jsonObjectSetting.optString("upi_field");

                SharePref.getInstance().putString(SharePref.Profile_Field3,bank_detail_field);
                SharePref.getInstance().putString(SharePref.Profile_Field4,adhar_card_field);
                SharePref.getInstance().putString(SharePref.Profile_Field5,upi_field);

                JSONArray user_kyc = jsonObject.getJSONArray("user_kyc");
                for (int i = 0; i < user_kyc.length(); i++) {
                    JSONObject jsonObject1 = user_kyc.getJSONObject(i);
                    String pan = jsonObject1.getString("pan_no");
                    String aadhar = jsonObject1.getString("aadhar_no");
                    String status = jsonObject1.getString("status");
//                    Toast.makeText(context, ""+status, Toast.LENGTH_SHORT).show();
                    String reason = jsonObject1.getString("reason");
                    String imgpan = jsonObject1.getString("pan_img");
                    String imgaadhar = jsonObject1.getString("aadhar_img");
                    edt_pan.setText(pan);
                    edt_aadar.setText(aadhar);
                    if (status.equals("1")){    //approved
                        img_verified.setVisibility(View.VISIBLE);
                        img_verified.setBackgroundResource(R.drawable.green_tick);
                        txt_status.setVisibility(View.VISIBLE);
                        txt_status.setText("Docs Status: Approved");
                        txt_status.setTextColor(Color.parseColor("#00FF00"));
                        edt_pan.setEnabled(false);
                        edt_pan.setEnabled(false);
                        upload_pan.setEnabled(false);
                        upload_aadhar.setEnabled(false);
                        ((ImageView) contentView.findViewById(R.id.imgsub)).setEnabled(false);
                    }
                    if (status.equals("2")){    //rejected
                        img_verified.setVisibility(View.VISIBLE);
                        img_verified.setBackgroundResource(R.drawable.red_reject);
                        txt_status.setVisibility(View.VISIBLE);
                        txt_status.setText("Docs Status: Rejected");
                        txt_status.setTextColor(Color.parseColor("#FF0000"));
                        txt_reason.setVisibility(View.VISIBLE);
                        txt_reason.setText(reason);
                    }
                    if (status.equals("0")){    //pending
                        txt_status.setVisibility(View.VISIBLE);
                        txt_status.setText("Docs Status: Pending");
                        txt_status.setTextColor(Color.parseColor("#FFDF00"));
                    }

                    Glide.with(context).load(Const.IMGAE_PATH + imgpan).into(img_pan);
                    Glide.with(context).load(Const.IMGAE_PATH + imgaadhar).into(img_aadhar);
                }

                txt_diaName.setText(name);
                txt_diaPhone.setText(mobile);
                txt_bank.setText(bank_detail);
                txt_adhar.setText(adhar_card);
                txt_upi.setText(upi);

                Glide.with(context).load(Const.IMGAE_PATH + profile_pic).into(img_diaProfile);


                edtUsername.setHint("Enter "+SharePref.getInstance().getString(SharePref.Profile_Field1,"Name"));
//                edtUsername.setHint("Enter "+SharePref.getInstance().getString(SharePref.Profile_Field2,"Ph.No"));
                edtUserupi.setHint("Enter "+SharePref.getInstance().getString(SharePref.Profile_Field3,"UPI ID"));
                edtUserbank.setHint("Enter "+SharePref.getInstance().getString(SharePref.Profile_Field4,"Bank Details"));
                edtUseradhar.setHint("Enter "+SharePref.getInstance().getString(SharePref.Profile_Field5,"Aadhar card no"));

                ((TextView) contentView.findViewById(R.id.Headingfiled1))
                        .setText(SharePref.getInstance().getString(SharePref.Profile_Field1,"Name:"));
                ((TextView) contentView.findViewById(R.id.Headingfiled2))
                        .setText(SharePref.getInstance().getString(SharePref.Profile_Field2,"Mobile no:"));
                ((TextView) contentView.findViewById(R.id.Headingfiled3))
                        .setText(SharePref.getInstance().getString(SharePref.Profile_Field3,"UPI ID:"));
                ((TextView) contentView.findViewById(R.id.Headingfiled4))
                        .setText(SharePref.getInstance().getString(SharePref.Profile_Field4,"Bank Details:"));
                ((TextView) contentView.findViewById(R.id.Headingfiled5))
                        .setText(SharePref.getInstance().getString(SharePref.Profile_Field5,"Aadhar card no:"));


                Functions.LOGD("UserInformation","profile_pic : "+Const.IMGAE_PATH + profile_pic);
                Glide.with(context).load(Const.IMGAE_PATH + profile_pic)
                        .placeholder(R.drawable.avatar).into(img_diaProfile);



                SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("name", name);
                editor.putString("profile_pic", profile_pic);
                editor.putString("bank_detail", bank_detail);
                editor.putString("upi", upi);
                editor.putString("adhar_card", adhar_card);
                editor.putString("mobile", mobile);
                editor.putString("referal_code", referral_code);
                editor.putString("img_name", profile_pic);
                editor.putString("wallet", wallet);
                editor.apply();


            } else if (code.equals("411")) {


            } else {

                if (jsonObject.has("message")) {
                    String message = jsonObject.getString("message");
                    Functions.showToast(context, message);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void selectImage() {

//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Choose Avaitor","Cancel" };
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {
                    if(Functions.check_permissions(context))
                        openCameraIntent();
                }

                else if (options[item].equals("Choose from Gallery"))
                {

                    if(Functions.check_permissions(context)) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                }
//                else if(options[item].equals("Choose Avaitor"))
//                {
//                    DialogSelectAvaitars.getInstance(context).returnCallback(new Callback() {
//                        @Override
//                        public void Responce(String resp, String type, Bundle bundle) {
//                            callback.Responce(resp,type,bundle);
//                            UserUpdateProfile();
//                        }
//                    }).show();
//                }
                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    // below three method is related with taking the picture from camera
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(context.getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        context.getPackageName()+".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 1);
            }
        }
    }

    String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public  String getPath(Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    File image_file, image_file_two;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode == RESULT_OK) {
//
//            if (requestCode == 1) {
//                Matrix matrix = new Matrix();
//                try {
//                    ExifInterface exif = new ExifInterface(imageFilePath);
//                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//                    switch (orientation) {
//                        case ExifInterface.ORIENTATION_ROTATE_90:
//                            matrix.postRotate(90);
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_180:
//                            matrix.postRotate(180);
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_270:
//                            matrix.postRotate(270);
//                            break;
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                image_file = new File(imageFilePath);
//                Uri selectedImage = (Uri.fromFile(image_file));
//
//                InputStream imageStream = null;
//                try {
//                    imageStream = context.getContentResolver().openInputStream(selectedImage);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
//                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
//
//                Bitmap resized = Bitmap.createScaledBitmap(rotatedBitmap, (int) (rotatedBitmap.getWidth() * 0.7), (int) (rotatedBitmap.getHeight() * 0.7), true);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                resized.compress(Bitmap.CompressFormat.JPEG, 20, baos);
//
//
//                base_64 = Functions.Bitmap_to_base64(context, resized);
//
//                if (image_file != null) ;
//                Glide.with(this).load(resized).into(img_diaProfile);
//
//                UserUpdateProfile();
//            }
//
//            else if (requestCode == 2) {
//                Uri selectedImage = data.getData();
//
//                progressDialog.show();
//                new ImageSendingAsync().execute(selectedImage);
//            }
        if (requestCode == 1 && str_chk.equals("0")) {
//                Toast.makeText(context, ""+str_chk, Toast.LENGTH_SHORT).show();
            Matrix matrix = new Matrix();
            try {
                ExifInterface exif = new ExifInterface(imageFilePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            image_file=new File(imageFilePath);
            Uri selectedImage =(Uri.fromFile(image_file));

            InputStream imageStream = null;
            try {
                imageStream =context.getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
            Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

            Bitmap  resized = Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.7), (int)(rotatedBitmap.getHeight()*0.7), true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 50, baos);

            pan_base64= Functions.Bitmap_to_base64(context,resized);
            Log.d("base_pan_cam", pan_base64);
            Glide.with(context).load(resized).into(img_pan);
//                if(image_file!=null);
//                Glide.with(this).load(resized).into(img_diaProfile);

        }  else if (requestCode == 2 && str_chk.equals("0")) {
            Uri selectedImage = data.getData();
//                Toast.makeText(context, ""+selectedImage, Toast.LENGTH_SHORT).show();
            progressDialog.show();
            new ImageSendingAsync().execute(selectedImage);

        }
        if (requestCode == 1 && str_chk.equals("1")) {
//            Toast.makeText(context, ""+str_chk, Toast.LENGTH_SHORT).show();
            Matrix matrix = new Matrix();
            try {
                ExifInterface exif = new ExifInterface(imageFilePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            image_file_two=new File(imageFilePath);
            Uri selectedImage =(Uri.fromFile(image_file_two));

            InputStream imageStream = null;
            try {
                imageStream =context.getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
            Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

            Bitmap  resized = Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.7), (int)(rotatedBitmap.getHeight()*0.7), true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 50, baos);


            aadhar_ase64= Functions.Bitmap_to_base64(context,resized);
//            Toast.makeText(context, ""+pan_base64, Toast.LENGTH_SHORT).show();
//            if(image_file!=null);
            Glide.with(this).load(resized).into(img_aadhar);

        }  else if (requestCode == 2 && str_chk.equals("1")) {
            Uri selectedImage = data.getData();

            progressDialog.show();
            new ImageSendingAsync_aadhar().execute(selectedImage);

        }

    }

    public class ImageSendingAsync extends AsyncTask<Uri, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Uri... params) {
            Uri selectedImage = params[0];

            try {
                image_file= FileUtils.getFileFromUri(context,selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            InputStream imageStream = null;
            try {
                imageStream =context.getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

            String path=getPath(selectedImage);
            Matrix matrix = new Matrix();
            ExifInterface exif = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                try {
                    exif = new ExifInterface(path);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

            Bitmap  resized = Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.5), (int)(rotatedBitmap.getHeight()*0.5), true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 50, baos);

            return resized;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressDialog.dismiss();


            pan_base64= Functions.Bitmap_to_base64(context,bitmap);
            Log.d("base_pan_", pan_base64);
//            if(image_file!=null)
            Glide.with(context).load(bitmap).into(img_pan);

//            UserUpdateProfile();

            super.onPostExecute(bitmap);
        }
    }
    public class ImageSendingAsync_aadhar extends AsyncTask<Uri, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Uri... params) {
            Uri selectedImage = params[0];

            try {
                image_file_two= FileUtils.getFileFromUri(context,selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            InputStream imageStream = null;
            try {
                imageStream =context.getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

            String path=getPath(selectedImage);
            Matrix matrix = new Matrix();
            ExifInterface exif = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                try {
                    exif = new ExifInterface(path);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

            Bitmap  resized = Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.5), (int)(rotatedBitmap.getHeight()*0.5), true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 50, baos);

            return resized;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressDialog.dismiss();

            aadhar_ase64= Functions.Bitmap_to_base64(context,bitmap);

//            if(image_file!=null)
            Glide.with(context).load(bitmap).into(img_aadhar);

//            UserUpdateProfile();

            super.onPostExecute(bitmap);
        }
    }

    private void UserUpdateProfile() {      //update KYC
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_CHANGE_PASSWORD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("DATA_CHECK", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {

                                android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(context);
//                builder.setTitle("Location")
                                builder.setMessage("Updated!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
//                                                Functions.showToast(context, "success");
                                                if(callback != null)
                                                    callback.Responce("update","",null);
                                                progressDialog.dismiss();
                                                dismiss();
                                            }
                                        });
//                                        .setNegativeButton("MAPS", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                                                        Uri.parse("http://maps.google.co.in/maps?q="+liveStrength.getLocation()));
//                                                context.startActivity(intent);
//                                            }
//                                        });
                                android.app.AlertDialog alert = builder.create();
                                alert.setTitle("Password");
                                alert.show();

//                                Functions.showToast(context, "success");
//                                if(callback != null)
//                                    callback.Responce("update","",null);
//                                dismiss();
                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(context, message);
                                    progressDialog.dismiss();
                                }

                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("old_password", Functions.getStringFromEdit(edt_oldpass));
                params.put("new_password",Functions.getStringFromEdit(edt_newpass));
                params.put("token", prefs.getString("token", ""));
                Log.d("paremter_java_pass", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}
