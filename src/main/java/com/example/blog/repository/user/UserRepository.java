package com.example.blog.repository.user;

import com.example.blog.service.user.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface UserRepository {

    default Optional<UserEntity> selectByUsername(String username){
        return Optional.ofNullable(username)
                .flatMap(this::selectByUsernameInternal);
    }

    @Select("""
            SELECT
                u.id
              , u.username
              , u.password
              , u.image_path
              , u.enabled
            FROM users u
            WHERE u.username = #{username}
            """)
    @Result(column = "image_path", property = "imagePath")
    Optional<UserEntity> selectByUsernameInternal(String username);

    @Insert("""
            INSERT INTO users(username, password, image_path, enabled)
            VALUES(#{username}, #{password}, #{imagePath}, #{enabled})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserEntity entity);

    @Delete("""
            DELETE FROM users u
            WHERE u.username = #{username}
            """)
    void deleteByUsername(String username);

    @Update("""
            UPDATE users u
            SET u.image_path = #{imagePath}
              , u.enabled = #{enabled}
            WHERE u.id = #{id}
            """)
    void update(String imagePath, boolean enabled, long id);
}
