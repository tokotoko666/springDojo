package com.example.blog.repository.user;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserRepository {

    @Select("""
            SELECT
                u.username
              , u.password
              , u.enabled
            FROM users u
            WHERE u.username = #{username}
            """)
    Optional<UserRecord> selectByUsername(String username);

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
