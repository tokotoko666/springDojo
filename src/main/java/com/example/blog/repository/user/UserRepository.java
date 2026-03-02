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
              , u.enabled
            FROM users u
            WHERE u.username = #{username}
            """)
    Optional<UserEntity> selectByUsernameInternal(String username);

    @Insert("""
            INSERT INTO users(username, password, enabled)
            VALUES(#{username}, #{password}, #{enabled})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserEntity entity);

    @Delete("""
            DELETE FROM users u
            WHERE u.username = #{username}
            """)
    void deleteByUsername(String username);

    @Update("""
            UPDATE FROM users u
            SET u.image_path = #{imagePath}
            WHERE u.username = #{username}
            """)
    void update(String imagePath, String username);
}
