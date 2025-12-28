package com.example.blog.repository.user;

import com.example.blog.service.user.UserEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    void insert(String username, String password, boolean enabled);

    @Delete("""
            DELETE FROM users u
            WHERE u.username = #{username}
            """)
    void deleteByUsername(String username);
}
