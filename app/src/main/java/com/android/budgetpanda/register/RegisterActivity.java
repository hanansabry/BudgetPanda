package com.android.budgetpanda.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.budgetpanda.Injection;
import com.android.budgetpanda.R;
import com.android.budgetpanda.backend.authentication.AuthenticationRepository;
import com.android.budgetpanda.control.ControlActivity;
import com.android.budgetpanda.login.LoginActivity;
import com.android.budgetpanda.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements AuthenticationRepository.RegistrationCallback {

    private RegisterPresenter presenter;
    private TextInputLayout nameTextInput, familyNumberTextInput, passwordTextInput, emailTextInput, phoneTextInput;
    private EditText nameEditText, familyNumberEditText, passwordEditText, emailEditText, phoneEditText;
    private ProgressBar progressBar;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        presenter = new RegisterPresenter(this, Injection.provideAuthenticationRepository());
        initializeView();
    }

    private void initializeView() {
        nameTextInput = findViewById(R.id.name_text_input_edittext);
        passwordTextInput = findViewById(R.id.password_text_input_edittext);
        familyNumberTextInput = findViewById(R.id.family_text_input_edittext);
        emailTextInput = findViewById(R.id.email_text_input_edittext);
        phoneTextInput = findViewById(R.id.phone_text_input_edittext);

        nameEditText = findViewById(R.id.name_edit_text);
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nameTextInput.setError(null);
                nameTextInput.setErrorEnabled(false);
            }
        });

        passwordEditText = findViewById(R.id.password_edit_text);
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passwordTextInput.setError(null);
                passwordTextInput.setErrorEnabled(false);
            }
        });
        emailEditText = findViewById(R.id.email_edit_text);
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailTextInput.setError(null);
                emailTextInput.setErrorEnabled(false);
            }
        });
        phoneEditText = findViewById(R.id.phone_edit_text);
        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                phoneTextInput.setError(null);
                phoneTextInput.setErrorEnabled(false);
            }
        });
        familyNumberEditText = findViewById(R.id.family_edit_text);

        progressBar = findViewById(R.id.progress_bar);
        registerButton = findViewById(R.id.register);
    }

    private User getUserInputData() {
        User user = new User();
        user.setName(nameEditText.getText().toString().trim());
        user.setPassword(passwordEditText.getText().toString().trim());
        user.setEmail(emailEditText.getText().toString().trim());
        user.setPhone(phoneEditText.getText().toString().trim());

        String familyNumber = familyNumberEditText.getText().toString().trim();
        user.setFamilyNumber(familyNumber.isEmpty() ? 0 : Integer.valueOf(familyNumber));
        return user;
    }

    public void setNameInputTextErrorMessage() {
        nameTextInput.setError("Name can't be empty");
    }

    public void setPasswordInputTextErrorMessage(String errorMessage) {
        passwordTextInput.setError(errorMessage);
    }

    public void setEmailInputTextErrorMessage() {
        emailTextInput.setError("Email can't be empty");
    }

    public void setPhoneInputTextErrorMessage() {
        phoneTextInput.setError("Phone can't be empty");
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.GONE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        registerButton.setVisibility(View.VISIBLE);
    }

    public void OnRegisterClicked(View view) {
        showProgressBar();
        User user = getUserInputData();
        if (presenter.validateUserData(user)) {
            presenter.registerNewUser(user, this);
        } else {
            hideProgressBar();
        }
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onSuccessfulRegistration(FirebaseUser firebaseUser) {
        hideProgressBar();
        Toast.makeText(this, "Registration is successfully completed\n Welcome " + firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        goToControlActivity();
    }

    @Override
    public void onFailedRegistration(String errmsg) {
        hideProgressBar();
        Toast.makeText(this, "Registration Failed\n" + errmsg, Toast.LENGTH_LONG).show();
    }

    private void goToControlActivity() {
        Intent homeIntent = new Intent(this, ControlActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }
}
