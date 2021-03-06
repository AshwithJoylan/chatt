package com.facebook.chatt.utils;

public class Users {
    private String id;
    private String display_name;
    private String status;
    private String profile_image;
    private String thumb_image;

    public Users() {
    }

    public Users(String id, String display_name, String status, String profile_image, String thumb_image) {
        this.id = id;
        this.display_name = display_name;
        this.status = status;
        this.profile_image = profile_image;
        this.thumb_image = thumb_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", display_name='" + display_name + '\'' +
                ", status='" + status + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", thumb_image='" + thumb_image + '\'' +
                '}';
    }
}
