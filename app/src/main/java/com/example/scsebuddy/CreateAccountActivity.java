package com.example.scsebuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scsebuddy.requestsresults.ConstantVariables;

import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.scsebuddy.requestsresults.RetrofitInterface;

public class CreateAccountActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    String valid_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); //

        EditText emailInput = findViewById(R.id.emailEditText);
        EditText passwordInput = findViewById(R.id.passwordEditText);
        EditText fNameInput = findViewById(R.id.firstNameEditText);
        EditText lNameInput = findViewById(R.id.lastNameEditText);
        EditText password1Input = findViewById(R.id.password1EditText);

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                is_Valid_Email(emailInput);
            }

            public void is_Valid_Email(EditText edt) {
                if (edt.getText().toString() == null) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                } else if (isEmailValid(edt.getText().toString()) == false) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                } else {
                    valid_email = edt.getText().toString();
                }
            }

            boolean isEmailValid(CharSequence email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches();
            } // end of TextWatcher (email)
        });


        this.findViewById(R.id.createAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordInput.getText().toString().trim();
                String password1 = password1Input.getText().toString().trim();
                if (valid_email != null && password1.equals(password)) {
                    HashMap<String, String> map = new HashMap<>();
                    String email = emailInput.getText().toString().trim();
                    String fName = fNameInput.getText().toString().trim();
                    String lName = lNameInput.getText().toString().trim();
                    Random ran = new Random();

                    //password
                    if (!fName.isEmpty()) {
                        if (!lName.isEmpty()) {
                            if (isValidPassword(password)) {
                                int codeVerify = ran.nextInt(899999) + 100000;

                                map.put("email", email);
                                map.put("password", password);
                                map.put("fName", fName);
                                map.put("lName", lName);
                                map.put("codeVerify", codeVerify + "");

                                Call<Void> call = retrofitInterface.executeSignup(map);

                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.code() == 200) {
                                            Intent intent = new Intent(v.getContext(), VerificationActivity.class);
                                            intent.putExtra("codeVerify", codeVerify);
                                            intent.putExtra("email", email);
                                            intent.putExtra("password", password);
                                            intent.putExtra("fName", fName);
                                            intent.putExtra("lName", lName);
                                            startActivity(intent);
                                        } else if (response.code() == 404) {
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateAccountActivity.this);
                                            builder1.setMessage(valid_email + " is used.");
                                            builder1.setCancelable(true);
                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(CreateAccountActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                AlertDialog.Builder builder4 = new AlertDialog.Builder(CreateAccountActivity.this);
                                builder4.setMessage("Password does not contain 1 digit or 1 lower or 1 upper or 1 special character.\n" +
                                        "or\n" +
                                        "It does not have 8 characters.");
                                builder4.setCancelable(true);
                                AlertDialog alert4 = builder4.create();
                                alert4.show();
                            }

                        } else {
                            AlertDialog.Builder builder3 = new AlertDialog.Builder(CreateAccountActivity.this);
                            builder3.setMessage("Empty Last Name");
                            builder3.setCancelable(true);
                            AlertDialog alert3 = builder3.create();
                            alert3.show();
                        }
                    } else {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(CreateAccountActivity.this);
                        builder2.setMessage("Empty First Name");
                        builder2.setCancelable(true);
                        AlertDialog alert2 = builder2.create();
                        alert2.show();
                    }
                } else {
                    if (!password1.equals(password)) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateAccountActivity.this);
                        builder1.setMessage("Password does not match!");
                        builder1.setCancelable(true);
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateAccountActivity.this);
                        builder1.setMessage("Email Not Valid");
                        builder1.setCancelable(true);
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }
        });
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void loginTextView(View v) {
        Intent intent = new Intent(v.getContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}