package com.example.scsebuddy;

import androidx.appcompat.app.AppCompatActivity;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeAccountDetailsActivity extends AppCompatActivity {
    EditText firstNameEditText, lastNameEditTest;
    String fName, lName, email;
    Context context;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account_details);
        firstNameEditText = this.findViewById(R.id.firstNameEditText);
        lastNameEditTest = this.findViewById(R.id.lastNameEditText);
        context = this;
        sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        fName = sp.getString("USER_F_NAME", "");
        lName = sp.getString("USER_L_NAME", "");
        email = sp.getString("USER_EMAIL", "");
    }

    public void updateDetails(View v) {
        retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();
        if (!firstNameEditText.getText().toString().isEmpty()) {
            map.put("fName", firstNameEditText.getText().toString().trim());
            fName = firstNameEditText.getText().toString().trim();
        } else {
            map.put("fName", fName);
        }
        if (!lastNameEditTest.getText().toString().isEmpty()) {
            map.put("lName", lastNameEditTest.getText().toString().trim());
            lName = lastNameEditTest.getText().toString().trim();
        } else {
            map.put("lName", lName);
        }
        //default loading
        //HashMap<String, String> map = new HashMap<>();
        map.put("email", email);

        updateRV(map);
    }

    private void updateRV(HashMap map) {
        Call<Void> call = retrofitInterface.executeChangeAccDetails(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER_F_NAME", fName);
                    editor.putString("USER_L_NAME", lName);
                    editor.putString("USER_EMAIL", email);
                    editor.commit();

                    Intent i = new Intent(context, ProfileActivity.class);
                    startActivity(i);
                    finish();

                } else if (response.code() == 404) {
                    Toast.makeText(ChangeAccountDetailsActivity.this, "Wrong Credentials!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChangeAccountDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}