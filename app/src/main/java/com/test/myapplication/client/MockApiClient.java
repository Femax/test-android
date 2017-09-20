package com.test.myapplication.client;

import com.test.myapplication.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 21.09.2017.
 */

public class MockApiClient {

    public static List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList<>();
        photos.add(new Photo("https://image.freepik.com/free-vector/android-boot-logo_634639.jpg", "Android"));
        photos.add(new Photo("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHtlJvjLiCE2ftnmU1A2FroaxswAFIZPv0qRWknANTytP-7lG9", "Android-Settings"));
        photos.add(new Photo("https://upload.wikimedia.org/wikipedia/commons/thumb/d/db/Angry_robot.svg/861px-Angry_robot.svg.png", "Angry-Android"));

        return photos;
    }
}
