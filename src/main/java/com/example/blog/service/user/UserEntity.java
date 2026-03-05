package com.example.blog.service.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserEntity {
    private Long id;
    private String username;
    private String password;
    private String imagePath;
    private boolean enabled;

    public UserEntity(Long id, String username, String password, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public UserEntity(Long id, String username, String password, String imagePath, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.imagePath = imagePath;
        this.enabled = enabled;
    }
}
