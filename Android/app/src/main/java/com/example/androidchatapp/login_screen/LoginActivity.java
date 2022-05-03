package com.example.androidchatapp.login_screen;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidchatapp.R;
import com.example.androidchatapp.Services.TestService;
import com.example.androidchatapp.Services.UserService;
import com.example.androidchatapp.registration_screen.RegistrationActivity;

public class LoginActivity extends AppCompatActivity {

    TextView register_tv, username_tv, password_tv;
    Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register_tv = (TextView) findViewById(R.id.newuser_tv);
        login_btn = (Button) findViewById(R.id.login_btn);
        password_tv = (TextView) findViewById(R.id.password_tv);
        username_tv = (TextView) findViewById(R.id.user_tv);
/*
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
        }
*/
        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckUsernameAndPasswordFilled()){
                    String username = username_tv.getText().toString();
                    String password = password_tv.getText().toString();

                    UserService.login(getApplicationContext(), username, password);
                }
            }
        });

        createNotificationChannel();
        /*Intent serviceIntent = new Intent(this, TestService.class);
        Log.e("service", "intent start service");
        startService(serviceIntent);*/
    }

    private boolean CheckUsernameAndPasswordFilled(){
        if(username_tv.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "username field can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password_tv.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "password field can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String CHANNEL_ID = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}