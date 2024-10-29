package com.example.scsebuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scsebuddy.requestsresults.ConstantVariables;
import com.example.scsebuddy.requestsresults.LoginResult;
import com.example.scsebuddy.requestsresults.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        this.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

                HashMap<String, String> map = new HashMap<>();
                EditText emailInput = findViewById(R.id.editTextEmail);
                EditText passwordInput = findViewById(R.id.editTextPassword);
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                map.put("email", email);
                map.put("password", password);
                Log.e("TEST1", map + "");

                Call<LoginResult> call = retrofitInterface.executeLogin(map);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.code() == 200) {
                            LoginResult result = response.body();

                            String fName = result.getfName();
                            String lName = result.getlName();
                            String email = result.getEmail();

                            System.out.println(fName);
                            System.out.println(lName);
                            System.out.println(email);

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USER_F_NAME", fName);
                            editor.putString("USER_L_NAME", lName);
                            editor.putString("USER_EMAIL", email);
                            editor.commit();

                            Intent intent = new Intent(v.getContext(), MapActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (response.code() == 404) {
                            Toast.makeText(LoginActivity.this, "Wrong Credential", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                            builder1.setMessage("Email or Password is wrong.");
                            builder1.setCancelable(true);
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        this.findViewById(R.id.forgotPasswordTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ForgetPwActivity.class);
                startActivity(intent);
            }
        });

        this.findViewById(R.id.createAccountTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    public void skipLogin(View v) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
        builder1.setMessage("Are you sure you don't want to log in?");
        builder1.setCancelable(true);


        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("USER_EMAIL", null);
                        editor.commit();

                        Intent intent = new Intent(v.getContext(), MapActivity.class);
                        startActivity(intent);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}