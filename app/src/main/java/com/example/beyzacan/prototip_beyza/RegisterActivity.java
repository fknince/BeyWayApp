package com.example.beyzacan.prototip_beyza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName,mEmail,mPassword;
    private Button mCreateBtn;
    private FirebaseModule firebaseModule;
    private View register_activity;
    private ProgressDialog mRegProgress;

    public ProgressDialog getmRegProgress() {
        return mRegProgress;
    }
    public View getRegister_activity() {
        return register_activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDisplayName=(TextInputLayout)findViewById(R.id.reg_display_name);
        mEmail=(TextInputLayout)findViewById(R.id.reg_email);
        mPassword=(TextInputLayout)findViewById(R.id.reg_password);
        register_activity=findViewById(R.id.reg_activity);
        firebaseModule=new FirebaseModule();
        mCreateBtn=(Button)findViewById(R.id.create_account_button);
        final RegisterActivity myClass=this;
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name=mDisplayName.getEditText().getText().toString();
                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();
                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email)||!TextUtils.isEmpty(password))
                {
                    mRegProgress.setTitle("Hesap oluşturuluyor.");
                    mRegProgress.setMessage("Lütfen bekleyiniz...");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    firebaseModule.createNewUser(email,password,myClass);
                }

            }
        });

        //TextInputLayout's OnFocuseChange Functions
        mDisplayName.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String display_name=mDisplayName.getEditText().getText().toString();
                if(TextUtils.isEmpty(display_name))
                {
                    mDisplayName.setErrorEnabled(true);
                    mDisplayName.setError("Lütfen isminizi giriniz!");
                }
                else
                    mDisplayName.setErrorEnabled(false);
            }
        });

        mEmail.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String email=mEmail.getEditText().getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    mEmail.setErrorEnabled(true);
                    mEmail.setError("Lütfen emailinizi giriniz!");
                }
                else
                    mEmail.setErrorEnabled(false);
            }
        });

        mPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String password=mPassword.getEditText().getText().toString();
                if(TextUtils.isEmpty(password))
                {
                    mPassword.setErrorEnabled(true);
                    mPassword.setError("Lütfen şifrenizi giriniz!");
                }
                else
                    mPassword.setErrorEnabled(false);
            }
        });



        //Progress Dialog
        mRegProgress=new ProgressDialog(this);

    }

    public void gecisYap() {
        //push register informations
        String display_name=mDisplayName.getEditText().getText().toString();
        firebaseModule.push_name(firebaseModule.getCurrentUser().getUid(),display_name);

        mRegProgress.dismiss();
        Intent mainIntent=new Intent(getBaseContext(), MenuActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getBaseContext().startActivity(mainIntent);
        finish();
    }
}
