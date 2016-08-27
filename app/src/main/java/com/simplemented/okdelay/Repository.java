package com.simplemented.okdelay;

import com.google.gson.annotations.SerializedName;

public class Repository {

    private final String name;
    @SerializedName("html_url")
    private final String url;

    public Repository(final String name, final String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
