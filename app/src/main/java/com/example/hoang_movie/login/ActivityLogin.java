package com.example.hoang_movie.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hoang_movie.R;
import com.example.hoang_movie.databinding.ActivityLoginBinding;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {
    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getTask();

    }

    public void LoginForm(View view) {
        LoginRequest request = new LoginRequest(binding.username.getText().toString(), binding.password.getText().toString());
        ApiClient.getAuthService().login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    Log.d("API", "Token: " + token);
                    TokenManager.getInstance(getApplicationContext()).saveToken(token);
                    Toast.makeText(ActivityLogin.this, token, Toast.LENGTH_SHORT).show();
                    // su dung token
                    //String token = TokenManager.getInstance(context).getToken();
                    //if (token != null) {
                    //    String bearerToken = "Bearer " + token;
                    //    // set bearerToken vào Authorization header khi gọi API
                    //}
                } else {
                    Log.e("API", "Login failed: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
            }
        });
    }
    public void getTask(){
        String token = TokenManager.getInstance(this).getToken();
        if (token != null) {
            String bearerToken = "Bearer " + token;

            ApiClient.getAuthService().getTasks(bearerToken, new DateRequest("2025-04-24")).enqueue(new Callback<TasksResponse>() {
                @Override
                public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                    Log.d("API", "Status code: " + response.code());
                    if (response.isSuccessful()) {
                        TasksResponse taskResponse = response.body();
                        if (taskResponse != null) {
                            List<Task> tasks = taskResponse.getTasks();
                            if (tasks != null) {
                                for (Task task : tasks) {
                                    Log.d("API", "Task: " + task.getTitle());
                                }
                            } else {
                                Log.e("API", "Task list is null");
                            }
                        } else {
                            Log.e("API", "Response body is null");
                        }
                    } else {
                        Log.e("API", "Failed: " + response.code() + " | " + response.message());
                        try {
                            Log.e("API", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                @Override
                public void onFailure(Call<TasksResponse> call, Throwable t) {
                    // Xử lý lỗi
                }
            });

        }
    }
}