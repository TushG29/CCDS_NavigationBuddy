package com.example.scsebuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class VerificationActivity extends AppCompatActivity {
    private String codeVerify;
    private String email, password, fName, lName;
    EditText verifyCodeEditText;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        context = this;
        verifyCodeEditText = this.findViewById(R.id.verifyCodeEditText);
        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            codeVerify = b.get("codeVerify") + "";
            email = b.get("email") + "";
            password = b.get("password") + "";
            fName = b.get("fName") + "";
            lName = b.get("lName") + "";
        }
    }

    public void verifyCode(View v) {
        Log.e("TEST", codeVerify + "");
        Log.e("TEST", verifyCodeEditText.getText().toString() + "");
        if (verifyCodeEditText.getText().toString().equals(codeVerify)) {
            retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
            retrofitInterface = retrofit.create(RetrofitInterface.class);

            HashMap<String, String> map = new HashMap<>();
            map.put("email", email);
            map.put("password", password);
            map.put("fName", fName);
            map.put("lName", lName);

            Call<Void> call = retrofitInterface.executeCode(map);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Intent i = new Intent(context, LoginActivity.class);
                        startActivity(i);
                    } else if (response.code() == 404) {
                        Toast.makeText(VerificationActivity.this, "Wrong Credentials!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(VerificationActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(VerificationActivity.this, "TRY AGAIN!", Toast.LENGTH_LONG).show();
        }
    }
}