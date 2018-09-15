package com.example.beyzacan.prototip_beyza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mEmail,mPassword;
    private Button mLoginButton;
    private FirebaseModule firebaseModule;
    private ProgressDialog mLoginProgress;
    private android.support.v7.widget.Toolbar mToolbar;
    private View login_activity;

    public View getLogin_activity() {
        return login_activity;
    }

    public ProgressDialog getmLoginProgress() {
        return mLoginProgress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail=(TextInputLayout) findViewById(R.id.log_email);
        mPassword=(TextInputLayout) findViewById(R.id.log_password);
        mLoginButton=(Button)findViewById(R.id.login_account_button);
        firebaseModule=new FirebaseModule();
        mLoginProgress=new ProgressDialog(this);
        final LoginActivity myClass=this;
        login_activity=findViewById(R.id.login_activity);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();
                if(!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password))
                {
                    mLoginProgress.setTitle("Hesabınıza giriş yapılıyor.");
                    mLoginProgress.setMessage("Lütfen bekleyiniz...");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    firebaseModule.logIn(email,password,myClass);
                }

            }
        });
    }

    public void gecisYap() {



        Intent mainIntent=new Intent(getBaseContext(),MenuActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }
}
