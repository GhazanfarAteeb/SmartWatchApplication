package com.app.smartwatchapplication.Modals;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoSafeLoginApiResponse {
    public boolean error;
    @Nullable
    public String message;

    @SerializedName("user")
    @Expose
    @Nullable
    List<User> user = null;

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}
