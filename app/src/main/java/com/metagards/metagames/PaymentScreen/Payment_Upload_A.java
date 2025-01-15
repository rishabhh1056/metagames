package com.metagards.metagames.PaymentScreen;



import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.metagards.metagames.BaseActivity;

import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.FileUtils;

import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.Variables;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Payment_Upload_A extends BaseActivity {
    String type = "";
    String plan_id = "";
    String chips_details = "";
    String amount = "";
    String RazorPay_ID = "";
    String mobile = "";
    String id = "";
    String base_64 = "";
    String transaction_id = "";
    ImageView image_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_upload);
        ((TextView) findViewById(R.id.toolbr_lbl)).setText("PaymentProof Upload");


        findViewById(R.id.bt_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        image_ = findViewById(R.id.image_);
        Glide.with(Payment_Upload_A.this).load(R.drawable.placeholder_upload).into(image_);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        if (type.equals("home")) {
            UserStatus(intent.getStringExtra("transaction_id"));
        } else {
            plan_id = intent.getStringExtra("plan_id");
            chips_details = intent.getStringExtra("chips_details");
            amount = intent.getStringExtra("amount");
            mobile = intent.getStringExtra("mobile");
            id = intent.getStringExtra("id");
            transaction_id = intent.getStringExtra("transaction_id");
            ((TextView) findViewById(R.id.txt_amount)).setText("Amount:" + " " + Variables.CURRENCY_SYMBOL + amount);
            ((TextView) findViewById(R.id.txn_id)).setText("Transaction Id: " + transaction_id);
            ((TextView) findViewById(R.id.txt_diaPhone)).setText("Mobile: " + mobile);
            Log.e("transaction_id", "onCreate: " + mobile);

            findViewById(R.id.upload_img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
                }
            });
            findViewById(R.id.copy_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyText(Payment_Upload_A.this, transaction_id);
                }
            });
            findViewById(R.id.copy_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyText(Payment_Upload_A.this, mobile);

                }
            });

        }


    }


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(Payment_Upload_A.this, R.style.AlertDialogCustom);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (Functions.check_permissions(Payment_Upload_A.this))
                        openCameraIntent();
                } else if (options[item].equals("Choose from Gallery")) {

                    if (Functions.check_permissions(Payment_Upload_A.this)) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                } else if (options[item].equals("Cancel")) {

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
        if (pictureIntent.resolveActivity(Payment_Upload_A.this.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Payment_Upload_A.this,
                        Payment_Upload_A.this.getPackageName() + ".fileprovider", photoFile);
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
        File storageDir = Payment_Upload_A.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public String getPath(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = Payment_Upload_A.this.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    File image_file;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
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
                image_file = new File(imageFilePath);
                Uri selectedImage = (Uri.fromFile(image_file));

                InputStream imageStream = null;
                try {
                    imageStream = Payment_Upload_A.this.getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

                Bitmap resized = Bitmap.createScaledBitmap(rotatedBitmap, (int) (rotatedBitmap.getWidth() * 0.7), (int) (rotatedBitmap.getHeight() * 0.7), true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 20, baos);


                base_64 = Functions.Bitmap_to_base64(Payment_Upload_A.this, resized);

//                if(image_file!=null);
//                Glide.with(this).load(resized).into(img_diaProfile);

                LoadImageonView(image_file, resized);

                UserUpdateProfile();

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();

                new ImageSendingAsync().execute(selectedImage);

            }

        }

    }

    public class ImageSendingAsync extends AsyncTask<Uri, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Uri... params) {
            Uri selectedImage = params[0];

            try {
                image_file = FileUtils.getFileFromUri(Payment_Upload_A.this, selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            InputStream imageStream = null;
            try {
                imageStream = Payment_Upload_A.this.getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

            String path = getPath(selectedImage);
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

            Bitmap resized = Bitmap.createScaledBitmap(rotatedBitmap, (int) (rotatedBitmap.getWidth() * 0.5), (int) (rotatedBitmap.getHeight() * 0.5), true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 20, baos);

            return resized;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {


            base_64 = Functions.Bitmap_to_base64(Payment_Upload_A.this, bitmap);

//            if(image_file!=null)
//                Glide.with(Payment_Upload_A.this).load(bitmap).into(img_diaProfile);

            LoadImageonView(null, bitmap);

            UserUpdateProfile();


            super.onPostExecute(bitmap);
        }
    }

    private void UserUpdateProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.addPaymentProof,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("DATA_CHECK", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
                                Toast.makeText(Payment_Upload_A.this, "Image Uploaded Successfully ",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(Payment_Upload_A.this, message,
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Payment_Upload_A.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = Payment_Upload_A.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("agent_id", id);
                params.put("payment_pic", "" + base_64);
                params.put("transaction_id", "" + transaction_id);
                params.put("token", prefs.getString("token", ""));
                Log.d("paremter_java", "getParams: " + params);
                Log.e("paremter_java", "getParams: " + Const.addPaymentProof);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(Payment_Upload_A.this).add(stringRequest);

    }

    private void LoadImageonView(File image_file, Bitmap bitmapFile) {
        if (bitmapFile != null) {
            Glide.with(Payment_Upload_A.this).load(bitmapFile).into(image_);

        }
    }

    private void UserStatus(final String txnid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.transaction_status,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("DATA_CHECKuser_id", "onResponse: " + response);
                        try {
                            final JSONObject data;
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
                                data = jsonObject.getJSONObject("data");

                                ((TextView) findViewById(R.id.txn_id)).setText("Transaction Id: " + data.getString("transaction_id"));
                                ((TextView) findViewById(R.id.txt_diaPhone)).setText("Mobile: " + data.getString("mobile"));
                                ((TextView) findViewById(R.id.txt_amount)).setText("Amount:" + " " + Variables.CURRENCY_SYMBOL + data.getString("amount"));

                                /*1 - in progress  2 - complete and 0 - only pic upload */
                                if (data.getString("status").equals("null")) {
                                    View_one_gray();
                                } else if (data.getString("status").equals("0")) {
                                    Glide.with(Payment_Upload_A.this).load(Const.payments_IMGAE_PATH + data.getString("payment_pic")).into((ImageView) findViewById(R.id.image_));
                                    View_one_green();
                                } else if (data.getString("status").equals("1")) {
                                    Glide.with(Payment_Upload_A.this).load(Const.payments_IMGAE_PATH + data.getString("payment_pic")).into((ImageView) findViewById(R.id.image_));
                                    View_one_green();
                                    View_two_green();
                                } else {
                                    Glide.with(Payment_Upload_A.this).load(Const.payments_IMGAE_PATH + data.getString("payment_pic")).into((ImageView) findViewById(R.id.image_));
                                    View_one_green();
                                    View_two_green();
                                    View_three_green();
                                }
                                findViewById(R.id.copy_1).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            copyText(Payment_Upload_A.this, data.getString("transaction_id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                findViewById(R.id.copy_2).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            copyText(Payment_Upload_A.this, data.getString("mobile"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(Payment_Upload_A.this, message,
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Payment_Upload_A.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = Payment_Upload_A.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("transaction_id", "" + txnid);
                params.put("token", prefs.getString("token", ""));
                Log.d("paremter_java", "getParams: " + params);
                Log.e("paremter_java", "getParams: " + Const.addPaymentProof);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(Payment_Upload_A.this).

                add(stringRequest);

    }

    private void View_one_green() {
        Glide.with(Payment_Upload_A.this).load(R.drawable.green_tick).into((ImageView) findViewById(R.id.tick_1));
        LinearLayout view_1 = findViewById(R.id.view_1);
        view_1.setBackgroundColor(Color.parseColor("#01BE7F"));
    }

    private void View_two_green() {
        Glide.with(Payment_Upload_A.this).load(R.drawable.green_tick).into((ImageView) findViewById(R.id.tick_2));
        LinearLayout view_1 = findViewById(R.id.view_2);
        view_1.setBackgroundColor(Color.parseColor("#01BE7F"));
    }

    private void View_three_green() {
        Glide.with(Payment_Upload_A.this).load(R.drawable.green_tick).into((ImageView) findViewById(R.id.tick_3));
        LinearLayout view_1 = findViewById(R.id.view_3);
        view_1.setBackgroundColor(Color.parseColor("#01BE7F"));
    }

    private void View_one_gray() {
        Glide.with(Payment_Upload_A.this).load(R.drawable.gray_tick).into((ImageView) findViewById(R.id.tick_1));
        LinearLayout view_1 = findViewById(R.id.view_1);
        view_1.setBackgroundColor(Color.parseColor("#c4c4c4"));
    }

    private void copyText(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("CopiedText", text);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(getApplicationContext(), "Data Copied", Toast.LENGTH_SHORT).show();

    }

}