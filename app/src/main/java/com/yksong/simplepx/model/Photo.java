package com.yksong.simplepx.model;

/**
 * Created by esong on 15-09-08.
 */
public class Photo {
    public int id;
    public String name;
    public String description;
    public String image_url;
    public User user;

    public class User {
        public int id;
        public String username;
        public String fullname;
        public String userpic_url;
        public Avatars avatars;

        public class Avatars{
            public Small small;

            public class Small {
                public String https;
            }
        }
    }
}
