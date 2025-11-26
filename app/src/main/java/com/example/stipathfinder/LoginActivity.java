package com.example.stipathfinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class LoginActivity extends AppCompatActivity {

    private ISingleAccountPublicClientApplication msalApp;
    private ProgressBar progressBar;
    private Button loginBtn;
    private Button menuButton1, menuButton2;
    private LinearLayout popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.progressBar);
        loginBtn = findViewById(R.id.loginButton);
        popupMenu = findViewById(R.id.popupMenu);
        menuButton1 = popupMenu.findViewById(R.id.menuButton1);
        menuButton2 = popupMenu.findViewById(R.id.menuButton2);

        // Initially hide login button and show progress bar
        loginBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // Initialize MSAL
        PublicClientApplication.createSingleAccountPublicClientApplication(
                this,
                R.raw.auth_config,
                new PublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        msalApp = application;

                        msalApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
                            @Override
                            public void onAccountLoaded(IAccount activeAccount) {
                                progressBar.setVisibility(View.GONE);
                                if (activeAccount != null) {
                                    String email = activeAccount.getUsername();
                                    Toast.makeText(LoginActivity.this,
                                            "Already logged in as: " + email,
                                            Toast.LENGTH_LONG).show();
                                    // Launch Godot after login
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    loginBtn.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onAccountChanged(IAccount priorAccount, IAccount currentAccount) { }

                            @Override
                            public void onError(MsalException exception) {
                                progressBar.setVisibility(View.GONE);
                                loginBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this,
                                        "Failed to check account: " + exception.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onError(MsalException exception) {
                        progressBar.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this,
                                "MSAL init failed: " + exception.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Login button click listener
        loginBtn.setOnClickListener(v -> {
            if (msalApp == null) return;

            msalApp.signIn(
                    LoginActivity.this,
                    null,
                    new String[]{"User.Read"},
                    new AuthenticationCallback() {
                        @Override
                        public void onSuccess(IAuthenticationResult result) {
                            Toast.makeText(LoginActivity.this,
                                    "Logged in: " + result.getAccount().getUsername(),
                                    Toast.LENGTH_LONG).show();
                            // Launch Godot after successful login
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(MsalException exception) {
                            Toast.makeText(LoginActivity.this,
                                    "Login failed: " + exception.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(LoginActivity.this,
                                    "Login canceled",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });

        // ===== AUTO-SCROLL IMAGE SLIDER =====
        int[] sliderImages = { R.drawable.banner_one, R.drawable.banner_two, R.drawable.banner_three };
        ViewPager2 slider = findViewById(R.id.imageSlider);
        ImageAdapter adapter = new ImageAdapter(this, sliderImages);
        slider.setAdapter(adapter);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int next = slider.getCurrentItem() + 1;
                if (next >= sliderImages.length) next = 0;
                slider.setCurrentItem(next, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

        // ===== MENU BUTTON LOGIC =====
        Button menuButton = findViewById(R.id.threelinesImage);
        menuButton.setOnClickListener(v -> {
            popupMenu.setVisibility(popupMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        // Open PathfinderDeveloperActivity when first menu button is clicked
        menuButton1.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, PathfinderDeveloperActivity.class));
            popupMenu.setVisibility(View.GONE);
        });

        // Handle second menu button click
        menuButton2.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, PathfinderDescriptionActivity.class));
            popupMenu.setVisibility(View.GONE);
        });
    }
}
