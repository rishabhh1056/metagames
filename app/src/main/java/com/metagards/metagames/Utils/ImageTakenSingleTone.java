package com.metagards.metagames.Utils;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.metagards.metagames.Interface.OnItemClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ImageTakenSingleTone {
    Activity context;
    OnItemClickListener callback;

    public  String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    int CAMERA_PICTURE_CODE = 1;
    int GALLERY_PICTURE_CODE = 2;
    boolean isCropEnable = false;
    ProgressDialog progressDialog;

    public ImageTakenSingleTone(Activity context, OnItemClickListener callback, boolean isCropEnable) {
        this.context = context;
        this.callback = callback;
        this.isCropEnable = isCropEnable;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");


    }

    // this method will show the dialog of selete the either take a picture form camera or pick the image from gallary
    public  String TAKE_PHOTO = "Take Photo";
    public  String CHOOSE_FROM_GALLERY = "Choose from Gallery";
    public  String CANCEL = "Cancel";
    boolean isMultipleImage = false;
    boolean isGallery = false;
    public void selectImage(Activity context,boolean isMultipleImage) {
        this.isMultipleImage = isMultipleImage;
        final CharSequence[] options = {TAKE_PHOTO, CHOOSE_FROM_GALLERY, CANCEL};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(TAKE_PHOTO)) {

                    isGallery = false;
                    if (Functions.check_permissions(context))
                        openCameraIntent();
                    else
                        permissionForCamerStorage(false);

                } else if (options[item].equals(CHOOSE_FROM_GALLERY)) {
                    isGallery = true;
                    openGallery(isMultipleImage);
                } else if (options[item].equals(CANCEL)) {

                    dialog.dismiss();

                }

            }

        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isGallery = true;
//                openGallery(isMultipleImage);
//            }
//        },200);

        builder.show();

    }

    ArrayList<Bundle> bundles;
    private void openGallery(boolean multiple_selection) {
        if (Functions.check_permissions(context)) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple_selection);
            context.startActivityForResult(intent, GALLERY_PICTURE_CODE);
            bundles = new ArrayList<>();

        } else
            permissionForCamerStorage(multiple_selection);
    }

    private final int STORAGE_REQUEST_CODE = 2;
    public void permissionForCamerStorage(boolean multiple_selection) {

        if(true)
        {
            return;
        }

        showDialogOK("This permission is important Upload Image.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.requestPermissions(PERMISSIONS, STORAGE_REQUEST_CODE);
                }
            }
        });

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    // below three method is related with taking the picture from camera
    public void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(context.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();

                //            photoFile = new File(FileUtils.getFilename(context));
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(context.getApplicationContext(),
                            context.getPackageName() + ".fileprovider", photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    context.startActivityForResult(pictureIntent, CAMERA_PICTURE_CODE);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */);

        imageFilePath = image.getAbsolutePath();
        image_file = new File(image.getAbsolutePath());
        return image;
    }


    String imageFilePath;
    File image_file;
    int count;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == RESULT_OK) {

                if (requestCode == CAMERA_PICTURE_CODE) {
                    Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));

//                    if(isCropEnable)
//                        CropImage.activity(selectedImage)
//                            .start(context);
//                    else {
                    progressDialog.show();
                    new ImageSendingAsync().execute(selectedImage);
//                    }
                }
                else if (requestCode == GALLERY_PICTURE_CODE) {

                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();

                         count = clipData.getItemCount();

                        progressDialog.show();
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = clipData.getItemAt(i).getUri();
                            new ImageSendingAsync().execute(imageUri);
                        }

                    }
                    else if (data.getData() != null /*&& isCropEnable*/) {

//                        Uri selectedImage = data.getData();
//                        CropImage
//                                .activity(selectedImage)
//                                .setAspectRatio(1, 1)
//                                .start(context);
//
////                        new ImageSendingAsync().execute(selectedImage);
//                    }
//                    else {
                        Uri selectedImage = data.getData();
                        progressDialog.show();
                        new ImageSendingAsync().execute(selectedImage);
//                    }

                }

//                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                    if (resultCode == RESULT_OK) {
//                        Uri resultUri = result.getUri();
//                        progressDialog.show();
//                        new ImageSendingAsync().execute(resultUri);
//                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                        Exception error = result.getError();
//                    }
//                }
            }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do your task here
                    selectImage(context,false);
                } else {
//                    permission_denied();
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public class ImageSendingAsync extends AsyncTask<Uri, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Uri... params) {
            Uri selectedImage = params[0];

            try {
                image_file = FileUtils.getFileFromUri(context, selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
//                image_file = new File(FileUtils.compressImage(image_file.toString(),context));

            Bitmap bm = null;
            try {
                Date date = new Date(FileUtils.getFileFromUri(context, selectedImage).lastModified());
                String text = new SimpleDateFormat("dd:MM:yyyy hh:mm aa").format(date);

                Bitmap myBitmap = BitmapFactory.decodeFile(image_file.getAbsolutePath());
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(String.valueOf(image_file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int rotationInDegrees = exifToDegrees(rotation);
                Matrix matrix = new Matrix();
                if (rotation != 0f) {
                    matrix.preRotate(rotationInDegrees);
                    myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
                }
                 bm = myBitmap
                        .copy(Bitmap.Config.ARGB_8888, true);

//                Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

//                Paint paint = new Paint();
//                paint.setStyle(Paint.Style.FILL);
//                paint.setColor(Color.WHITE);
//                paint.setTypeface(tf);
//                paint.setTextAlign(Paint.Align.CENTER);

//                Rect textRect = new Rect();
//                paint.getTextBounds(text, 0, text.length(), textRect);

//                Canvas canvas = new Canvas(bm);
//                paint.setTextSize(convertToPixels(context, (int) (canvas.getHeight() * 0.02)));
//
//                if (textRect.width() >= (canvas.getWidth() - 4))
//                    paint.setTextSize(convertToPixels(context, 7));
//
//                int xPos = (canvas.getWidth() / 2) - 2;
//
//                int yPos = (int) ((canvas.getHeight() - canvas.getHeight() / 5) - 5);
//
//                canvas.drawText(text, xPos, yPos, paint);

                ImageView selfie_imageview = new ImageView(context);

                selfie_imageview.setImageDrawable(new BitmapDrawable(context.getResources(), bm));

                Bitmap bitmap = ((BitmapDrawable) selfie_imageview.getDrawable()).getBitmap();
                String tm = new SimpleDateFormat("yyyyMMDD-HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path + "/DCIM");
                String imageName = tm + ".jpg";
                File file = new File(dir, imageName);

                OutputStream os;

                try {
                    os = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                    image_file = file;
                    return bitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                    Functions.showToast(context, "Error occured");
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }

            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

//            LoadProfileImageUsing(image_file);
//            CALL_API_UpdatePicture(bitmap);

            if(isGallery && isMultipleImage)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("bitmap",bitmap);
                bundle.putString("image_file", String.valueOf(image_file));

                bundles.add(bundle);

                if(count <= bundles.size())
                {
                    progressDialog.dismiss();
                    callback.Response(null,0,bundles);
                }
            }
            else {
                Bundle bundle = new Bundle();
                bundle.putParcelable("bitmap",bitmap);
                bundle.putString("image_file", String.valueOf(image_file));
                progressDialog.dismiss();
                callback.Response(null,1,bundle);
            }




            super.onPostExecute(bitmap);
        }
    }


    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }


}
