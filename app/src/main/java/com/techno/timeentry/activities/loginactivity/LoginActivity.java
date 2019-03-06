package com.techno.timeentry.activities.loginactivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.techno.timeentry.R;
import com.techno.timeentry.activities.mainactivity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    LoginPresenter loginPresenter;

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPass;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginPresenter = new LoginPresenter(this);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        loginPresenter.login(etUsername.getText().toString(), etPass.getText().toString());
    }


    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.text_login_failed), Toast.LENGTH_LONG).show();
        enableLoginButton();
    }

    @Override
    public void disableLoginButton() {
        btnLogin.setEnabled(true);
    }

    @Override
    public void enableLoginButton() {
        btnLogin.setEnabled(true);
    }

    @Override
    public ProgressDialog showProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.DialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.text_authenticating));
        progressDialog.show();
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                loginPresenter.cancelLogin();
            }
        });
        return progressDialog;
    }

    @Override
    public void hideProgressDialog(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }

    @Override
    public void showUserNameError() {
        etUsername.setError(getString(R.string.text_valid_user_name));
    }

    @Override
    public void showPassWordError() {
        etPass.setError(getString(R.string.text_password_error_message));
    }

    @Override
    public void showUserNameNotRegistered() {
        etUsername.setError(getString(R.string.text_username_not_registered));
    }

    @Override
    public void showPassWordIncorrect() {
        etPass.setError(getString(R.string.text_incorrect_password));
    }

    @Override
    public void clearUserNameError() {
        etUsername.setError(null);
    }

    @Override
    public void clearPassWordError() {
        etPass.setError(null);
    }
}

