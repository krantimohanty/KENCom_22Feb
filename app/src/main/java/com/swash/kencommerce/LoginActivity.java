package com.swash.kencommerce;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerLib;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.swash.kencommerce.GCM.Config;
import com.swash.kencommerce.GCM.NotificationUtils;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.FbLoginRequest;
import com.swash.kencommerce.model.ForgotPasswordRequest;
import com.swash.kencommerce.model.LoginRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class LoginActivity extends AppCompatActivity {

    LinearLayout ll_fb_signin;
    EditText et_email, et_password;
    TextView txt_register;
    Button btn_sign_in;
    LoginManager loginManagerFB;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    String facebook_id,facebook_profile_pic;
    TextView txt_forget_pass;
    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;
    ImageView img_back;

    RestService restService;
    Utils utils;
    SharedPreferences sharedPreferenceUser;
    SharedPreferences.Editor sharedPrefEditior;
    private SharedPreferences shPrefUserSelection;
    private String store_id="";
    ArrayList<HashMap<String, String>> listAddToCartList = new ArrayList<HashMap<String, String>>();
    private AddToCartTable mAddToCartTable;
    private ArrayList<HashMap<String, String>> data;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "19484110841";

    static final String TAG = "FCM";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    String strInt = "", strIntFor = "";
    Class<?> nIntent = null;
    String firebaseRegid = "";
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        restService=new RestService();
        utils = new Utils(LoginActivity.this);
        System.out.println("Key hash::::" + printKeyHash(LoginActivity.this));
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");
        mAddToCartTable=new AddToCartTable(this);
        
        data=mAddToCartTable.getAll();

        Intent intentGet = getIntent();
        if (intentGet.hasExtra("context_act1")){
            //act = intentGet.getExtras().getParcelable("context_act");
            strInt = intentGet.getStringExtra("context_act1");
            //strIntFor = "map";
        }
        try {
            nIntent = Class.forName(strInt);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }
            }
        };
        displayFirebaseRegId();
        initFields();

    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        firebaseRegid = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + firebaseRegid);


    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();
            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


    private void initFields() {
        //mAsyncRunner = new AsyncFacebookRunner(facebook);

        shPrefUserSelection=this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
        sharedPreferenceUser=this.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);

        store_id=shPrefUserSelection.getString(Constants.strShUserStoreID, "");

        sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);

        txt_forget_pass=(TextView)findViewById(R.id.txt_forget_pass);
        txt_forget_pass.setTypeface(typeFaceSegoeuiReg);
        txt_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForgetPassword();
            }
        });
        ll_fb_signin = (LinearLayout) findViewById(R.id.ll_fb_signin);
        et_email = (EditText) findViewById(R.id.et_email);
        et_email.setTypeface(typeFaceSegoeuiReg);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setTypeface(typeFaceSegoeuiReg);
        txt_register = (TextView) findViewById(R.id.txt_register);
        txt_register.setTypeface(typeFaceSegoeuiReg);
        customCheckBoxTextView(txt_register);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_sign_in.setTypeface(typeFaceSegoeuiBold);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        profileTracker = new ProfileTracker() {

            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if(currentProfile != null){
                    facebook_id = currentProfile.getId();
                    facebook_profile_pic=currentProfile.getProfilePictureUri(100,100).toString();

                    Log.e("facebook_profile_pic--", facebook_profile_pic);
                }
            }
        };
        //when new fb user logged in , below code block will be called
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                System.out.println("acesstoken trackercalled");
            }
        };
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nIntent!=null) {
                    Intent intent = new Intent(LoginActivity.this, nIntent);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }else{
                    Intent intent = new Intent(LoginActivity.this, PinCodeActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                LoginActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        callbackManager = CallbackManager.Factory.create();

        loginManagerFB =LoginManager.getInstance();

        loginManagerFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {

                        Profile p = Profile.getCurrentProfile();

                        Log.e("object----", "" + object.toString());
                        try {
                            String strId = object.getString("id");
                            String strFname = object.getString("first_name");
                            String strLname = object.getString("last_name");
                            String strEmail = "";
                            if (object.has("email")) {
                                strEmail = object.getString("email");
                            }
                            JSONArray jsonArray=new JSONArray();
                            JSONObject jObjImages;
                            for(int i=0; i< data.size(); i++){
                                jObjImages=new JSONObject();
                                try {
                                    jObjImages.put("attribute_id", data.get(i).get("productattid"));
                                    jObjImages.put("product_id", data.get(i).get("productid"));
                                    jObjImages.put("option_id", data.get(i).get("productoptionid"));
                                    jObjImages.put("qty",  data.get(i).get("qty"));

                                    jsonArray.put(i,jObjImages);
                                    //Toast.makeText(getApplicationContext(), "JSONDATA: "+jsonArray.toString(), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            doFbLogin(strId, strFname, strLname, strEmail, firebaseRegid, jsonArray.toString());
                            //Toast.makeText(LoginActivity.this, "Thank you " + object.getString("first_name").toString() + " " + object.getString("last_name").toString() + " for using us..", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

                GraphRequest request1 = GraphRequest.newMyFriendsRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONArrayCallback() {

                    @Override
                    public void onCompleted(JSONArray objects, GraphResponse response) {
                        System.out.println(response.toString());
                    }
                });

                Bundle parameters1 = new Bundle();
                parameters1.putString("fields", "id, name , picture");
                request1.setParameters(parameters1);
                request1.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("fb_cancel", "fb_cancel");
                Toast.makeText(getApplicationContext(), "Cancelled by user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                //Log.d("fb_error", exception.getCause().toString());
                Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                Log.d("fb_error", "fb_error");
            }
        });

        ll_fb_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign in with fb
                loginManagerFB.logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "user_friends", "public_profile"/*, "user_birthday"*/));
                //Toast.makeText(LoginActivity.this, "Sign In With Facebook", Toast.LENGTH_LONG).show();
                //getDialogCoverage();
            }
        });
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Register now
                Intent intentRegister = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intentRegister);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign In
                String strEmail = et_email.getText().toString().trim();
                String strPwd = et_password.getText().toString().trim();


                if (strEmail.length() > 0 && strPwd.length() > 0){
                    JSONArray jsonArray=new JSONArray();
                    JSONObject jObjImages;
                    for(int i=0; i< data.size(); i++){
                        jObjImages=new JSONObject();
                        try {
                            jObjImages.put("attribute_id", data.get(i).get("productattid"));
                            jObjImages.put("product_id", data.get(i).get("productid"));
                            jObjImages.put("option_id", data.get(i).get("productoptionid"));
                            jObjImages.put("qty",  data.get(i).get("qty"));

                            jsonArray.put(i,jObjImages);
                            //Toast.makeText(getApplicationContext(), "JSONDATA: "+jsonArray.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (jsonArray.length()>0) {
                        doLogin(strEmail, strPwd, jsonArray.toString());
                    }
                    else {
                        doLogin(strEmail, strPwd, "");
                    }
                }
                else {
                    utils.displayAlert("Please enter Email Address and Password");
                    //Toast.makeText(LoginActivity.this, "Please enter E-mail and Password", Toast.LENGTH_LONG).show();
                }
                //getDialogCoverage();

            }
        });


        for(int i=0; i<data.size(); i++){
            HashMap<String, String> mapShopList = new HashMap<String, String>();
            mapShopList.put("product_id", data.get(i).get("productid"));
            mapShopList.put("attribute_id", data.get(i).get("productattid"));
            mapShopList.put("option_id", data.get(i).get("productoptionid"));
            mapShopList.put("qty", data.get(i).get("qty"));
            listAddToCartList.add(mapShopList);
        }
    }

    private void doLogin(String strEmail, String strPwd, String jsonArray) {
        final ProgressDialog pDialog=new ProgressDialog(LoginActivity.this);
        pDialog.show();
        pDialog.setMessage("Login into your account..");
        restService.getLoginResponse(strEmail, strPwd, jsonArray, firebaseRegid, new RestCallback<LoginRequest>() {

            @Override
            public void success(LoginRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1){
                    sharedPrefEditior = sharedPreferenceUser.edit();
                    sharedPrefEditior.putString(Constants.strShPrefUserId, responce.getData().getCustomerId());
                    sharedPrefEditior.putString(Constants.strShPrefUserFname, responce.getData().getFirstname());
                    sharedPrefEditior.putString(Constants.strShPrefUserLname, responce.getData().getLastname());
                    sharedPrefEditior.putString(Constants.strShPrefUserEmail, responce.getData().getEmail());
                    sharedPrefEditior.putString(Constants.strShPrefUserPhone, responce.getData().getPhone());
                    sharedPrefEditior.commit();
                    mAddToCartTable.deleteAll();
                    displayAlert(responce.getData().getMsg());
                    //displayAlert("Welcome to Kenkart! Enjoy your \"knock, knock\" session!");
                    for(int i=0; i<data.size(); i++){
                        mAddToCartTable.deleteitem(data.get(i).get("productid"));
                    }
                    //Toast.makeText(LoginActivity.this,"Successfull Login", Toast.LENGTH_LONG).show();
                }
                else {
                    utils.displayAlert(responce.getErrorMsg());
                    //Toast.makeText(LoginActivity.this, responce.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }

            @Override
            public void invalid() {
                pDialog.dismiss();
            }

            @Override
            public void failure() {
                pDialog.dismiss();
            }
        });
    }

    private void doFbLogin(final String strFbId, final String strFname, final String strLname, final String strMail, String strDeviceId, String jsonArray) {
        final ProgressDialog pDialog=new ProgressDialog(LoginActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading..");
        restService.getFbLoginResponse(strFbId, firebaseRegid,jsonArray,  new RestCallback<FbLoginRequest>() {

            @Override
            public void success(FbLoginRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200){
                    // User Already Registered : Continue
                    if (reqSuccess == 1) {
                        sharedPrefEditior = sharedPreferenceUser.edit();
                        sharedPrefEditior.putString(Constants.strShPrefUserId, responce.getData().getCustomerId());
                        sharedPrefEditior.putString(Constants.strShPrefUserFname, responce.getData().getFirstname());
                        sharedPrefEditior.putString(Constants.strShPrefUserLname, responce.getData().getLastname());
                        sharedPrefEditior.putString(Constants.strShPrefUserEmail, responce.getData().getEmail());
                        sharedPrefEditior.putString(Constants.strShPrefUserPhone, responce.getData().getPhone());
                        sharedPrefEditior.commit();
                        //utils.displayAlert("You have Successfully loggedin");
                        displayAlert(responce.getData().getMsg());
                        //displayAlert("Welcome to KenKart! Enjoy your \"knock, knock\" session!");
                        for (int i = 0; i < data.size(); i++) {
                            mAddToCartTable.deleteitem(data.get(i).get("productid"));
                        }
                        //Toast.makeText(LoginActivity.this,"Successfull Login", Toast.LENGTH_LONG).show();
                    }
                    // New User : Registration Page
                    if (reqSuccess == 2){
                        Intent intentRegister = new Intent(LoginActivity.this, SignUpActivity.class);
                        intentRegister.putExtra("id", strFbId);
                        intentRegister.putExtra("first_name", strFname);
                        intentRegister.putExtra("last_name", strLname);
                        intentRegister.putExtra("email", strMail);
                        startActivity(intentRegister);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    }
                }
                else {
                    utils.displayAlert(responce.getErrorMsg());
                    //Toast.makeText(LoginActivity.this, responce.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }

            @Override
            public void invalid() {
                pDialog.dismiss();
            }

            @Override
            public void failure() {
                pDialog.dismiss();
            }
        });
    }

    private void customCheckBoxTextView(TextView view) {
        String init = "New User? ";
        String terms = "Sign Up Now";

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                init);
        spanTxt.append(terms);

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //callTermsCondition("file:///android_asset/Terms_and_Conditions.html", "Terms & Conditions");
            }
        }, init.length(), init.length() + terms.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#70B74E")), init.length(), init.length() + terms.length(), 0);

        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // FB
        callbackManager.onActivityResult(requestCode, resultCode, data);
        /*mAsyncRunner.request("me", new RequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                Log.d("Profile", response);
                String json = response;
                try {
                    // Facebook Profile JSON data
                    JSONObject profile = new JSONObject(json);

                    // getting name of the user
                    final String name = profile.getString("name");

                    // getting email of the user
                    final String email = profile.getString("email");

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();
                        }

                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onIOException(IOException e, Object state) {
            }

            @Override
            public void onFileNotFoundException(FileNotFoundException e,
                                                Object state) {
            }

            @Override
            public void onMalformedURLException(MalformedURLException e,
                                                Object state) {
            }

            @Override
            public void onFacebookError(FacebookError e, Object state) {
            }
        });*/
    }
    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        TextView msg=(TextView)dialog.findViewById(R.id.msg);
        Button btn_no=(Button)dialog.findViewById(R.id.btn_no);
        Button btn_yes=(Button)dialog.findViewById(R.id.btn_yes);
        btn_yes.setText("Ok");
        btn_no.setText("Cancel");

        header.setText("KenKart");
        msg.setText("Coming Soon");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void getForgetPassword() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_forget_pass);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final EditText etEmailId=(EditText)dialog.findViewById(R.id.msg);
        etEmailId.setTypeface(typeFaceSegoeuiReg);

        TextView btn_no=(TextView)dialog.findViewById(R.id.btn_cancel);
        TextView btn_yes=(TextView)dialog.findViewById(R.id.btn_ok);
        btn_yes.setText("OK");
        btn_no.setText("Cancel");
        btn_no.setAllCaps(false);
       // msg.setText("Coming Soon");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMail = etEmailId.getText().toString().trim();
                if (strMail.length() > 0) {
                    getPassword(strMail);
                    dialog.dismiss();
                }

            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    private void getPassword(String strMail) {
        final ProgressDialog pDialog=new ProgressDialog(LoginActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading...");
        restService.getPassword(strMail, new RestCallback<ForgotPasswordRequest>() {

            @Override
            public void success(ForgotPasswordRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    //getDialogOK(responce.getData().getSuccessMsg());
                    utils.displayAlert(responce.getData().getSuccessMsg());
                    //Toast.makeText(LoginActivity.this, responce.getData().getSuccessMsg() , Toast.LENGTH_LONG).show();
                } else {
                    //getDialogOK(responce.getErrorMsg());
                    utils.displayAlert(responce.getErrorMsg());
                    //Toast.makeText(LoginActivity.this, responce.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }

            @Override
            public void invalid() {
                pDialog.dismiss();
            }

            @Override
            public void failure() {
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(nIntent!=null) {
            Intent intent = new Intent(LoginActivity.this, nIntent);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        }else{
            Intent intent = new Intent(LoginActivity.this, PinCodeActivity.class);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        LoginActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private SharedPreferences getGcmPreferences(Context context) {

        // For GCM
        return getSharedPreferences("GCM", Context.MODE_PRIVATE);

        // For FIREBASE
        //return getSharedPreferences("ah_firebase", Context.MODE_PRIVATE);
    }
    private void getDialogOK(String strMsg) {
        final Dialog dialog = new Dialog(LoginActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_response_ok);

        TextView txt_header=(TextView)dialog.findViewById(R.id.txt_header);
        TextView txt_msg=(TextView)dialog.findViewById(R.id.txt_msg);
        Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);

        //txt_header.setText(strHeader);
        txt_msg.setText(strMsg);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void displayAlert(String message)
    {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenKart") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if(nIntent!=null) {
                    Intent intent = new Intent(LoginActivity.this, nIntent);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }else{
                    Intent intent = new Intent(LoginActivity.this, PinCodeActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            }
        });
        TextView myMsg = new TextView(LoginActivity.this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(18);
        myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(LoginActivity.this);
        // You Can Customise your Title here
        title.setVisibility(View.GONE);
        title.setText("KenKart");
        title.setBackgroundColor(Color.TRANSPARENT);
        title.setPadding(15, 20, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTypeface(typeFaceSegoeuiBold);
        title.setTextSize(20);

        myMsg.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams positiveButtonLLl = (LinearLayout.LayoutParams) myMsg.getLayoutParams();
        positiveButtonLLl.gravity = Gravity.CENTER|Gravity.CENTER_VERTICAL;
        myMsg.setLayoutParams(positiveButtonLLl);

        alertDialogBuilder.setCustomTitle(title);
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();



        final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER|Gravity.CENTER_VERTICAL;
        positiveButton.setTextColor(Color.parseColor("#048BCD"));
        positiveButton.setLayoutParams(positiveButtonLL);

    }
}
