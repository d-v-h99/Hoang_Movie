package com.example.hoang_movie.login;
import android.content.Context;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class TokenManager {
    private static final String TOKEN_KEY = "access_token";
    private static final String PREFS_NAME = "secure_prefs";
    private static TokenManager instance;
    private final EncryptedSharedPreferences prefs;

    private TokenManager(Context context) throws Exception {
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        prefs = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                PREFS_NAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            try {
                instance = new TokenManager(context);
            } catch (Exception e) {
                throw new RuntimeException("Error initializing TokenManager", e);
            }
        }
        return instance;
    }

    public void saveToken(String token) {
        prefs.edit().putString(TOKEN_KEY, token).apply();
    }

    public String getToken() {
        return prefs.getString(TOKEN_KEY, null);
    }

    public void clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply();
    }
}
