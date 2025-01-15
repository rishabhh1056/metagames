package com.metagards.metagames.account;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.metagards.metagames.Activity.Homepage;
import com.metagards.metagames.BaseActivity;
import com.metagards.metagames.Comman.DialogForgetPassword;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.ImageTakenSingleTone;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class LoginWithPasswordScreen_A extends BaseActivity {

    private static final String MY_PREFS_NAME = "Login_data";
    EditText edtPhoneLogin;
    EditText edtPasswordLogin;
    TextView tv;
    int pStatus = 0;
    private Handler handler = new Handler();
    AlertDialog dialog;
    EditText edit_OTP;
    String verificationID;
    boolean isSelected = false;
    RadioButton genderradioButton;
    ImageView imgBackground, imgBackgroundlogin;
    Activity context = this;
    public BottomSheetBehavior sheetBehavior;
    public View bottom_sheet;


    private final String LOGIN = "login";
    private final String REGISTER = "register";
    private Bitmap panBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_withpasswrod_activity);


        imgBackground = findViewById(R.id.imgBackground);
        imgBackgroundlogin = findViewById(R.id.imgBackgroundlogin);

//        Picasso.with(context).load(imageResource1).into(imgBackground);
        // Picasso.with(context).load(imageResource2).into(imgBackgroundlogin);

        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        edtPhoneLogin = findViewById(R.id.edtPhoneLogin);






/*
        imglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

*/
/*
                if(isValid())
                {
                    if(signupView.getCurrentView() == lnrFirstScreen)
                    {
                        if (CommonFunctions.isNetworkConnected(context)) {
                            //phoneLogin();
                            // Funtions.showToast(context,genderradioButton.getText(),
                            //Toast.LENGTH_SHORT).show();
                            // Funtions.showToast(context, rb.getText());
                            if (isSelected) {

                                RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                                if(rb != null)
                                {
                                    login(rb.getText() + "");
                                }
                                else {
                                    login("male");
                                }

                            } else {

                                Functions.showToast(context, "Please select Gender first ?");

                            }


                        }
                        else {
                            CommonFunctions.showNoInternetDialog(context);
                        }
                    }
                    else {
                        SingupNextView();
                    }
                }
*//*




            }
        });
*/


        SocialLogin();

        test();

        initBottomDialog();

        imageTakenSingleTone = new ImageTakenSingleTone(context, new OnItemClickListener() {
            @Override
            public void Response(View v, int position, Object object) {

                Bundle bundle = (Bundle) object;

                Bitmap profileImage = bundle.getParcelable("bitmap");
                String strimage_file = bundle.getString("image_file");


                File image_file = new File(strimage_file);

                panBitmap = profileImage;

                String imagename = image_file.toString().substring(image_file.toString().lastIndexOf("/") + 1);
                ((TextView) findViewById(R.id.tv_uploadname)).setText("" + imagename);


            }
        }, false);

    }

/*
    private boolean isValid() {

        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());


        if(signupView.getCurrentView() == lnrFirstScreen)
        {
            if (edtname.getText().toString().isEmpty()) {
                return showToastNReturnFalse("Enter Name");
            }
            else if(!Functions.checkisStringValid(Functions.getStringFromEdit(edtPassword)))
            {
                return showToastNReturnFalse("Enter Password");
            }
            else
            if (!Functions.checkisStringValid(Functions.getStringFromEdit(edtPhone))) {
                return showToastNReturnFalse("Enter Mobile Number");
            }
            else
            if(radioButton == null || radioButton.getId() == -1)
            {
                return showToastNReturnFalse("Select Gender");
            }
//            else if(!Funtions.checkisStringValid(Funtions.getStringFromEdit(edtPanCard)))
//            {
//                return showToastNReturnFalse("Enter Pan Card");
//            }
//            else if(panBitmap == null)
//            {
//                return showToastNReturnFalse("Select Pan Card Image");
//            }
        }
        else if(signupView.getCurrentView() == lnrSeconScreen)
        {

            if (!Functions.checkisStringValid(Functions.getStringFromEdit(edtPhone))) {
                return showToastNReturnFalse("Enter Mobile Number");
            }
            else
            if(radioButton == null || radioButton.getId() == -1)
            {
                return showToastNReturnFalse("Select Gender");
            }
            else if(!Functions.checkisStringValid(Functions.getStringFromEdit(edtdob)))
            {
                return showToastNReturnFalse("Enter Date of birth");
            }
            else if(!Functions.checkisStringValid(Functions.getStringFromEdit(edtState)))
            {
                return showToastNReturnFalse("Enter State");
            }
        }
        else if(signupView.getCurrentView() == lnrThirdScreen)
        {
            if (!Functions.checkisStringValid(Functions.getStringFromEdit(edtPhone))) {
                return showToastNReturnFalse("Enter Mobile Number");
            }
        }

        return true;
    }
*/

/*
    private void SingupNextView() {
        signupView.showNext();

        if(signupView.getCurrentView() == lnrThirdScreen)
        {
            findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
            findViewById(R.id.lnrlastView).setVisibility(View.VISIBLE);
            imglogin.setText("Sign Up");
        }
        else
        {
            findViewById(R.id.ivBack).setVisibility(View.GONE);
            findViewById(R.id.lnrlastView).setVisibility(View.GONE);
            imglogin.setText("Next");
        }

    }
*/

    String reffer_code = "";
    RadioGroup bottomradioGroup;

    private void initBottomDialog() {
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        bottomradioGroup = findViewById(R.id.bottomradioGroup);
        sheetBehavior.setDraggable(false);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btn_bottom_sheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btn_bottom_sheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        findViewById(R.id.bottom_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


        final EditText edt_reffer = findViewById(R.id.edt_reffer);

        findViewById(R.id.btnrefer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = bottomradioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);


                if (!edt_reffer.getText().toString().trim().equals("")) {
                    reffer_code = edt_reffer.getText().toString().trim();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                } else {

                    reffer_code = "";
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                CALL_API_TO_VERIFY_SOCIELLOGIN(social_id, social_name, social_email, radioButton.getText().toString().trim());

            }
        });

    }


    private void test() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                Funtions.showToast(this, ""+Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    CallbackManager callbackManager;

    private void SocialLogin() {

        FacebookSdk.sdkInitialize(this);

        callbackManager = CallbackManager.Factory.create();

        initFacebook();

/*
        findViewById(R.id.imgfacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_type = 2;

                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(context ,
                        Arrays.asList("public_profile" , "email"));

            }
        });
*/

/*
        findViewById(R.id.imggoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_type = 1;

                Sign_in_with_gmail();
            }
        });
*/

        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                if (currentAccessToken == null) {

                } else {
                    loadUserProfile(currentAccessToken);
                }
            }
        };


    }

    //google Implimentation
    GoogleSignInClient mGoogleSignInClient;

    public void Sign_in_with_gmail() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String id = account.getId();
            String fname = "" + account.getGivenName();
            String lname = "" + account.getFamilyName();
            String email = "" + account.getEmail();

            String pic_url;
            if (account.getPhotoUrl() != null) {
                pic_url = account.getPhotoUrl().toString();
            } else {
                pic_url = "null";
            }

            if (fname.equals("") || fname.equals("null"))
                fname = getResources().getString(R.string.app_name);

            if (lname.equals("") || lname.equals("null"))
                lname = "User";


            issocial = true;

            social_name = fname + " " + lname;
            social_email = email;
            social_id = id;

            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

//            if(!txtreffer.getText().toString().trim().equals("Enter Reffer code"))
//            CALL_API_TO_VERIFY_SOCIELLOGIN(id, fname +" "+ lname, email);

        } else {

            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 123);

        }

    }

    int login_type = -1;

    private void CALL_API_TO_VERIFY_SOCIELLOGIN(String id, String name, String email, String gender) {

        HashMap params = new HashMap();
        params.put("email", "" + email);
        params.put("name", "" + name);
        params.put("gender", "" + gender);
        params.put("source", "" + login_type);
        params.put("referral_code", "" + reffer_code);

        ApiRequest.Call_Api(this, Const.email_login, params, new Callback() {
            @Override
            public void Responce(String response, String type, Bundle bundle) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");


                    if (code.equalsIgnoreCase("201")) {

                        String token = jsonObject.getString("token");

                        if (jsonObject.has("user")) {
                            JSONObject jsonObject1 = jsonObject.getJSONArray("user").getJSONObject(0);
                            String id = jsonObject1.getString("id");
                            String name = jsonObject1.getString("name");
                            String mobile = jsonObject1.getString("mobile");


                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("user_id", id);
                            editor.putString("name", name);
                            editor.putString("mobile", mobile);
                            editor.putString("token", token);
                            editor.apply();

                            Intent i = new Intent(context, Homepage.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Functions.showToast(context, "Login Successful");
                        } else {

                            if (jsonObject.has("message")) {
                                String message = jsonObject.getString("message");
                                Functions.showToast(context, "Wrong mobile number or password");
                            }

                        }


                    } else if (code.equalsIgnoreCase("200")) {
                        String token = jsonObject.getString("token");
                        String user_id = jsonObject.getString("user_id");

                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("user_id", user_id);
                        editor.putString("token", token);

                        editor.apply();

                        Intent i = new Intent(context, Homepage.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        Functions.showToast(context, "Login Successful");
//
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
        });


    }

    boolean issocial = false;

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String id = account.getId();
                String fname = "" + account.getGivenName();
                String lname = "" + account.getFamilyName();
                String email = "" + account.getEmail();


                // if we do not get the picture of user then we will use default profile picture

                String pic_url;
                if (account.getPhotoUrl() != null) {
                    pic_url = account.getPhotoUrl().toString();
                } else {
                    pic_url = "null";
                }


                if (fname.equals("") || fname.equals("null"))
                    fname = getResources().getString(R.string.app_name);

                if (lname.equals("") || lname.equals("null"))
                    lname = "User";

                issocial = true;

                social_name = fname + " " + lname;
                social_email = email;
                social_id = id;

                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


//                if(!txtreffer.getText().toString().trim().equals("Enter Reffer code"))
//                CALL_API_TO_VERIFY_SOCIELLOGIN(id, fname +" "+ lname, email);

//                loginform(id, fname, lname, pic_url, "gmail");


            }
        } catch (ApiException e) {
            Log.w("Error message", "signInResult:failed code=" + e.getStatusCode());
        }

    }

    String social_name = "", social_email = "", social_id = "";

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                if (object != null) {
                    try {
                        String first_name = object.optString("first_name");
                        String last_name = object.optString("last_name");
                        String email = object.optString("email");
//                    String bithday = object.getString("user_birthday");
                        String id;

                        id = object.getString("id");
                        String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

//                    PrefMnger.putString(getActivity(),PrefMnger.NAME,first_name + " " +last_name);
//                    PrefMnger.putString(getActivity(),PrefMnger.EMAIL,email);
//                    PrefMnger.putString(getActivity(),PrefMnger.image_url,image_url);

//                    Intent intent = new Intent(LoginActivity.this , )

                        issocial = true;
                        social_name = first_name + " " + last_name;
                        social_email = email;
                        social_id = id;

//                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

//                    if(!txtreffer.getText().toString().trim().equals("Enter Reffer code"))
                        CALL_API_TO_VERIFY_SOCIELLOGIN(id, first_name + " " + last_name, email, "Male");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Functions.showToast(context, "Server Error!");
                }


            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }


    private void initFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                    }

                    @Override
                    public void onCancel() {
                        Functions.showToast(context, "Login Cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Functions.showToast(context, exception.getMessage());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (imageTakenSingleTone != null)
            imageTakenSingleTone.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (imageTakenSingleTone != null)
            imageTakenSingleTone.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void login(final String value) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();
        Functions.setDialogParams(dialog);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.SEND_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        handleResponse(response, value);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", edtPhoneLogin.getText().toString());

                String view_type = LOGIN;

                if (!isLoginView) {
                    view_type = REGISTER;
                    //   params.put("mobile", edtPhone.getText().toString());
                }

                params.put("type", view_type);


                Functions.LOGE("LoginScreen", "params : " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void loginwithPassword() {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();
        Functions.setDialogParams(dialog);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.login_withpassword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Functions.LOGE("LoginScreen", Const.login_withpassword + "\n" + response);
                        progressDialog.dismiss();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String code = jsonObject.getString("code");


                            if (code.equalsIgnoreCase("201")) {

                                String token = jsonObject.getString("token");

                                if (jsonObject.has("user")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONArray("user").getJSONObject(0);
                                    String id = jsonObject1.getString("id");
                                    String name = jsonObject1.getString("name");
                                    String mobile = jsonObject1.getString("mobile");


                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("user_id", id);
                                    editor.putString("name", name);
                                    editor.putString("mobile", mobile);
                                    editor.putString("token", token);
                                    editor.apply();

                                    Intent i = new Intent(context, Homepage.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    Functions.showToast(context, "Login Successful");
                                } else {

                                    if (jsonObject.has("message")) {
                                        String message = jsonObject.getString("message");
                                        Functions.showToast(context, "Wrong mobile number or password");
                                    }

                                }


                            } else if (code.equalsIgnoreCase("200")) {
                                JSONArray jsonArray = jsonObject.optJSONArray("user_data");
                                jsonObject = jsonArray.optJSONObject(0);
                                String token = jsonObject.getString("token");
                                String user_id = jsonObject.getString("id");

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("user_id", user_id);
                                editor.putString("token", token);

                                editor.apply();

                                Intent i = new Intent(context, Homepage.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                Functions.showToast(context, "Login Successful");
//
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

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", edtPhoneLogin.getText().toString());
                params.put("password", edtPasswordLogin.getText().toString());

                String view_type = LOGIN;

                params.put("type", view_type);


                Functions.LOGE("LoginScreen", "params : " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    private boolean showToastNReturnFalse(String text) {
        Functions.showToast(this, text);
        return false;
    }


    private void handleResponse(String response, String value) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            String code = jsonObject.getString("code");


            if (code.equalsIgnoreCase("200")) {

                String otp_id = jsonObject.getString("otp_id");
                phoneLogin(otp_id, value);

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


    public void phoneLogin(final String otp_id, final String value) {
        // String phoneNumber= "+91"+edtPhone.getText().toString().trim();
        //SendVerificationCode(phoneNumber);
        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.dialog_subimt);
        dialog.setTitle("Title...");
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        edit_OTP = (EditText) dialog.findViewById(R.id.edit_OTP);

        dialog.findViewById(R.id.imgclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.findViewById(R.id.imglogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_OTP.getText().toString().length() > 0) {
                    String verify_code = edit_OTP.getText().toString();
                    VerifyCode(verify_code, otp_id, value);
                } else {
                    Functions.showToast(getApplicationContext(), "Please Enter OTP");

                }

            }
        });

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        Functions.setDialogParams(dialog);

    }


    private void VerifyCode(final String code, final String otp_id, final String value) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("otp", edit_OTP.getText().toString());
//                params.put("name", edtname.getText().toString());
        params.put("otp_id", otp_id.trim());
        params.put("mobile", edtPhoneLogin.getText().toString());

        String view_type = LOGIN;
        if (!isLoginView) {
            view_type = REGISTER;
/*
                    params.put("mobile", edtPhone.getText().toString());
                    params.put("name", edtname.getText().toString());
                    params.put("pan_card_no", edtPanCard.getText().toString());
                    params.put("dob", edtdob.getText().toString());
                    params.put("state", edtState.getText().toString());
                    params.put("password", edtPassword.getText().toString());
                    params.put("gender", value.trim());
                    params.put("referral_code", edtReferalCode.getText().toString().trim());
                    if (panBitmap != null)
                        params.put("pan_card", Functions.Bitmap_to_base64(context, panBitmap));*/
        }

        params.put("type", view_type);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();

        ApiRequest.Call_Api(context, Const.REGISTER, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                progressDialog.dismiss();
                if(resp != null)
                {
                    try {

                        JSONObject jsonObject = new JSONObject(resp);

                        String code = jsonObject.getString("code");


                        if (code.equalsIgnoreCase("201")) {

                            String token = jsonObject.getString("token");

                            if (jsonObject.has("user")) {
                                JSONObject jsonObject1 = jsonObject.getJSONArray("user").getJSONObject(0);
                                String id = jsonObject1.getString("id");
                                String name = jsonObject1.getString("name");
                                String mobile = jsonObject1.getString("mobile");


                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("user_id", id);
                                editor.putString("name", name);
                                editor.putString("mobile", mobile);
                                editor.putString("token", token);
                                editor.apply();

                                Intent i = new Intent(context, Homepage.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                Functions.showToast(context, "Login Successful");
                            } else {

                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(context, "Wrong mobile number or password");
                                }

                            }


                        } else if (code.equalsIgnoreCase("200")) {
                            String token = jsonObject.getString("token");
                            String user_id = jsonObject.getString("user_id");

                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("user_id", user_id);
                            editor.putString("token", token);

                            editor.apply();

                            Intent i = new Intent(context, Homepage.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Functions.showToast(context, "Login Successful");
//
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
            }
        });

    }


    boolean isLoginView = true;

    public void registerBtnClick(View view) {
        isLoginView = false;
        startActivity(new Intent(getApplicationContext(), Register_Activity.class));
    }

    public void LoginBtnClick(View view) {
        isLoginView = true;

    }

    ImageTakenSingleTone imageTakenSingleTone;

    public void OpenGallery(View view) {
        imageTakenSingleTone.selectImage(context, false);
    }

    public void SendOtp(View view) {

        if (!Functions.isStringValid(Functions.getStringFromEdit(edtPhoneLogin))) {
            Functions.showToast(context, "Enter Mobile Number.");
            return;
        } else if (!Functions.isStringValid(Functions.getStringFromEdit(edtPasswordLogin))) {
            Functions.showToast(context, "Enter Password.");
            return;
        }

        loginwithPassword();
    }

    public void forgetBtnClick(View view) {

        DialogForgetPassword dialogForgetPassword = new DialogForgetPassword(context);

        dialogForgetPassword.showReportDialog();

    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }
}