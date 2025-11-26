package com.example.stipathfinder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Handler handler = new Handler();
    private int currentPage = 0;
    private ISingleAccountPublicClientApplication mSingleAccountApp;

    private int[] images = {
            R.drawable.main_one,
            R.drawable.main_two,
            R.drawable.main_three,
            R.drawable.main_four,
            R.drawable.main_five
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup carousel
        viewPager = findViewById(R.id.imageCarousel);
        MainImageAdapter adapter = new MainImageAdapter(this, images);
        viewPager.setAdapter(adapter);
        autoScroll();

        // Setup colored welcome text
        TextView welcomeText = findViewById(R.id.welcomeText);
        if (welcomeText != null) {
            String text = "WELCOME STIers";
            SpannableString spannable = new SpannableString(text);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFF00")), 0, 8, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#0072B2")), 8, 11, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFF00")), 11, text.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            welcomeText.setText(spannable);
        }

        // Initialize MSAL
        PublicClientApplication.createSingleAccountPublicClientApplication(
                this,
                R.raw.auth_config,
                new PublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        mSingleAccountApp = application;
                    }

                    @Override
                    public void onError(MsalException exception) {
                        exception.printStackTrace();
                    }
                }
        );

        // Setup popup menu
        Button menuIcon = findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(v -> showPopupMenu(v));

        // Setup profile button to open ProfileActivity
        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Setup navigateButton to open NavigationActivity
        Button navigateButton = findViewById(R.id.navigateButton);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void autoScroll() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == images.length) currentPage = 0;
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void showPopupMenu(View anchor) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu, null);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                true
        );

        // Log Out button inside popup
        Button popupButton = popupView.findViewById(R.id.popupButton);
        popupButton.setOnClickListener(v -> {
            popupWindow.dismiss();
            if (mSingleAccountApp != null) {
                mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                    @Override
                    public void onSignOut() {
                        Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(@NonNull MsalException exception) {
                        Toast.makeText(MainActivity.this, "Logout failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        popupWindow.showAsDropDown(anchor, 0, 10);
    }
}
