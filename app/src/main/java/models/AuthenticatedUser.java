package models;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.anhhnguyen.myapplication.R;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by anhhnguyen on 7/13/2014.
 */
public class AuthenticatedUser {
    private int id;
    private String username;
    private String email;
    private String access_token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public static AuthenticatedUser getCurrentUser(Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.sss_mobile_test), 0);
        String userValue = preferences.getString(context.getResources().getString(R.string.authenticatedUser), "");

        if (!TextUtils.isEmpty(userValue)){
            ObjectMapper objMap = new ObjectMapper();
            try {
                AuthenticatedUser currentUser = objMap.readValue(userValue, AuthenticatedUser.class);
                return currentUser;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        else{
            return null;
        }
    }

    public static boolean isLoggedIn(Context context)
    {
        return getCurrentUser(context) != null ? true : false;
    }

    public static void setCurrentUser(Context context, AuthenticatedUser loggedInUser){
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.sss_mobile_test), 0);
        SharedPreferences.Editor editor = preferences.edit();

        try {
            if (loggedInUser == null){
                editor.remove(context.getString(R.string.authenticatedUser));
            }
            else{
                editor.putString(context.getResources().getString(R.string.authenticatedUser),
                        new ObjectMapper().writeValueAsString(loggedInUser));
            }
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
