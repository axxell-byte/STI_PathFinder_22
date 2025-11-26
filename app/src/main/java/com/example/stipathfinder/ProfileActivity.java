package com.example.stipathfinder;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.identity.client.AcquireTokenSilentParameters;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private TextView userNameTextView;
    private ImageView profileImageView;
    private IAccount cachedAccount; // <-- store the account object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userNameTextView = findViewById(R.id.userName);
        profileImageView = findViewById(R.id.profileImageView);

        // Initialize MSAL
        PublicClientApplication.createSingleAccountPublicClientApplication(
                getApplicationContext(),
                R.raw.auth_config,
                new PublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        mSingleAccountApp = application;
                        loadProfile();
                    }

                    @Override
                    public void onError(MsalException exception) {
                        exception.printStackTrace();
                        userNameTextView.setText("Error initializing MSAL");
                    }
                }
        );
    }

    private void loadProfile() {
        if (mSingleAccountApp == null) return;

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(IAccount account) {
                if (account != null) {
                    cachedAccount = account;
                    acquireTokenSilent(account);  // <-- FIXED
                } else {
                    userNameTextView.setText("Not logged in");
                }
            }

            @Override
            public void onAccountChanged(IAccount priorAccount, IAccount currentAccount) { }

            @Override
            public void onError(MsalException exception) {
                exception.printStackTrace();
                userNameTextView.setText("Error loading account");
            }
        });
    }

    private void acquireTokenSilent(IAccount account) {
        if (mSingleAccountApp == null || account == null) return;

        String[] scopes = {"User.Read"};

        AcquireTokenSilentParameters silentParams =
                new AcquireTokenSilentParameters.Builder()
                        .forAccount(account)
                        .fromAuthority(account.getAuthority())
                        .withScopes(Arrays.asList(scopes))
                        .withCallback(new SilentAuthenticationCallback() {
                            @Override
                            public void onSuccess(IAuthenticationResult authenticationResult) {
                                fetchUserProfile(authenticationResult.getAccessToken());
                            }

                            @Override
                            public void onError(MsalException exception) {
                                // DO NOT log in again — show error
                                userNameTextView.setText("Unable to load profile");
                                exception.printStackTrace();
                            }
                        })
                        .build();

        mSingleAccountApp.acquireTokenSilentAsync(silentParams);
    }

    private void fetchUserProfile(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://graph.microsoft.com/v1.0/me")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> userNameTextView.setText("Failed to load profile"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        final String displayName = json.getString("displayName");
                        runOnUiThread(() -> userNameTextView.setText(displayName));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> userNameTextView.setText("Failed to parse profile"));
                    }
                } else {
                    runOnUiThread(() -> userNameTextView.setText("Failed to load profile"));
                }
            }
        });
    }
}
