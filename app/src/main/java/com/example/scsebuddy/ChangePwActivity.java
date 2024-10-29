package com.example.scsebuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scsebuddy.requestsresults.ConstantVariables;
import com.example.scsebuddy.requestsresults.RetrofitInterface;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePwActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

    }

    public void changePW(View v) {
        System.out.println("I HAVE ARRIVED");
        EditText txtOldPW = findViewById(R.id.txtOldPW);
        EditText txtNewPw = findViewById(R.id.txtNewPw);
        EditText txtReNewPw = findViewById(R.id.txtReNewPw);

        SharedPreferences sp = getSharedPreferences("UserPreferences", Context.MODE_WORLD_READABLE);
        String email = sp.getString("USER_EMAIL", "");

        if (txtNewPw.getText().toString().trim().equals(txtReNewPw.getText().toString().trim())) {
            if (isValidPassword(txtNewPw.getText().toString().trim())) {
                System.out.println("HERE?");
                HashMap<String, String> map = new HashMap<>();
                map.put("oldPW", txtOldPW.getText().toString().trim());
                map.put("newPW", txtNewPw.getText().toString().trim());
                map.put("email", email);

                Call<Void> call = retrofitInterface.executeChangePW(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(ChangePwActivity.this, "Change successfully!", Toast.LENGTH_LONG).show();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else if (response.code() == 404) {
                            Toast.makeText(ChangePwActivity.this, "Wrong Password!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ChangePwActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                AlertDialog.Builder builder4 = new AlertDialog.Builder(ChangePwActivity.this);
                builder4.setMessage("Password does not contain 1 digit or 1 lower or 1 upper or 1 special character.\n" +
                        "or\n" +
                        "It does not have 8 characters.");
                builder4.setCancelable(true);
                AlertDialog alert4 = builder4.create();
                alert4.show();
            }


        } else {
            // toast smt for reenter pw
            Toast.makeText(ChangePwActivity.this, "New Password Not Same!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}