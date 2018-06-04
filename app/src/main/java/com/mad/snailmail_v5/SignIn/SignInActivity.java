package com.mad.snailmail_v5.SignIn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.snailmail_v5.R;

public class SignInActivity extends AppCompatActivity implements SignInContract.View {

    private Button mSignInButton;
    private EditText mSignInET;
    private Button mRegisterButton;
    private EditText mRegisterET;
    private SignInPresenter mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSignInButton = findViewById(R.id.sign_in_button);
        mSignInET = findViewById(R.id.sign_in_username);
        mRegisterButton = findViewById(R.id.register_button);
        mRegisterET = findViewById(R.id.register_username);

        mPresenter = new SignInPresenter(this, SignInActivity.this);

    }

    @Override
    public void setPresenter(SignInContract.Presenter presenter) {

    }

    @Override
    public void displayDialogue(AlertDialog dialogue) {

    }

    @Override
    public void requestPermissions(String[] permissions) {

    }

    @Override
    public void displayToast(Toast toast) {
        toast.show();
    }

    @Override
    public void setDataLoading(boolean dataLoading) {

    }
}
