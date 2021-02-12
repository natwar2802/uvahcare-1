package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class loginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText editTextMobile,editTextcountryCode;
    Button sendotp;
    CountryCodePicker ccp;
    ProgressBar loadingimage;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        editTextMobile=(EditText)findViewById(R.id.editTextMobile);
        sendotp=(Button) findViewById(R.id.buttonContinue);
        // ccp=(CountryCodePicker)findViewById(R.id.ccp);
        auth=FirebaseAuth.getInstance();
        ccp=findViewById(R.id.ccp);

        loadingimage= (ProgressBar) findViewById(R.id.progressbar);
        loadingimage.setVisibility(View.GONE);



        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   String countryCode=editTextcountryCode.getText().toString().trim();
                String countryCode=ccp.getSelectedCountryCode().toString().trim();
                String mobile = editTextMobile.getText().toString().trim();
                String phoneNumber="+" + countryCode + "" + mobile;
                //String phoneNumber=ccp.registerPhoneNumberTextView(edtPhoneNumber);
                // String phoneNumber=ccp.getFullNumberWithPlus().replace(" ","").toString();

                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }
                else {

                    loadingimage.setVisibility(View.VISIBLE);

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(auth)
                                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(loginActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(mcallback)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                    Intent intentO=new Intent(loginActivity.this,OtpVerification.class);
                    startActivity(intentO);
                    finish();


                }


            }
        });

        mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


                signInWithPhoneAuthCredential(phoneAuthCredential);
                loadingimage.setVisibility(View.GONE);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      //  Intent intentOtp=new Intent(loginActivity.this,OtpVerification.class);

                       // intentOtp.putExtra("otp",s);
                       // startActivity(intentOtp);
                    }
                },10000);

                //   mVerificationId = s;
            }
        };


    }

   // @Override
   /* protected void onStart() {
        super.onStart();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(loginActivity.this,MainActivity.class));
            finish();

        }
    }*/
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            // FirebaseUser user = task.getResult().getUser();
                            // check=true;
                            Intent intent = new Intent(loginActivity.this, EditProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                             finish();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });


    }
    @Override
    public void onBackPressed() { }

}