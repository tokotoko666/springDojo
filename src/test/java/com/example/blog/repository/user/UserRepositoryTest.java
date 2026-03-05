package com.example.blog.repository.user;

import com.example.blog.config.MybatisDefaultDataSourceTest;
import com.example.blog.service.user.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisDefaultDataSourceTest
class UserRepositoryTest {

    @Autowired
    private UserRepository cut;

    @Test
    void successAutowired() {
        assertThat(cut).isNotNull();
    }

    @Test
    @DisplayName("selectByUsername: 指定されたユーザー名のユーザーが存在するとき、Optional<UserEntity>を返す")
    @Sql(statements = {
            "INSERT INTO users(id, username, password, enabled)VALUES (999, 'test_user_1', 'test_user_1_pass', true);",
            "INSERT INTO users(id, username, password, enabled) VALUES(998, 'test_user_2', 'test_user_2_pass', true);"
    })
    void selectByUsername_success() {
        // ## Arrange ##

        // ## Act ##
        var actual = cut.selectByUsername("test_user_1");

        // ## Assert ##
        assertThat(actual).hasValueSatisfying(actualEntity -> {
            assertThat(actualEntity.getId()).isEqualTo(999L);
            assertThat(actualEntity.getUsername()).isEqualTo("test_user_1");
            assertThat(actualEntity.getPassword()).isEqualTo("test_user_1_pass");
            assertThat(actualEntity.isEnabled()).isTrue();
        });
    }

    @Test
    @DisplayName("selectByUsername: 指定されたユーザー名のユーザーが存在しないとき、Optional.emptyを返す")
    @Sql(statements = {
            "INSERT INTO users(id, username, password, enabled)VALUES (999, 'test_user_1', 'test_user_1_pass', true);"
    })
    void selectByUsername_returnEmpty() {
        // ## Arrange ##

        // ## Act ##
        var actual = cut.selectByUsername("invalid_user");

        // ## Assert ##
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("selectByUsername: ユーザー名にnullが指定されたとき、Optional.emptyを返す")
    @Sql(statements = {
            "INSERT INTO users(id, username, password, enabled)VALUES (999, 'null', 'test_user_1_pass', true);"
    })
    void selectByUsername_returnEmpty_whenNullsGiven() {
        // ## Arrange ##

        // ## Act ##
        var actual = cut.selectByUsername(null);

        // ## Assert ##
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("insert: Userを登録することができる")
    void insert_success() {
        // ## Arrange ##
        var newRecord = new UserEntity(null, "test_user_1", "test_user_1_pass", true);

        // ## Act ##
        cut.insert(newRecord);

        // ## Assert ##
        assertThat(newRecord.getId())
                .describedAs("AUTO INCREMENT で設定された id が entity の id フィールドに設定されている")
                .isGreaterThanOrEqualTo(1);

        var actual = cut.selectByUsername("test_user_1");
        assertThat(actual).isNotEmpty()
                .hasValueSatisfying(actualEntity -> {
                    assertThat(actualEntity.getId()).isGreaterThanOrEqualTo(1);
                    assertThat(actualEntity.getUsername()).isEqualTo("test_user_1");
                    assertThat(actualEntity.getUsername()).isEqualTo("test_user_1");
                    assertThat(actualEntity.getPassword()).isEqualTo("test_user_1_pass");
                    assertThat(actualEntity.isEnabled()).isTrue();
                });
    }

    @Test
    @DisplayName("update: UserEntity を更新することができる")
    void update_success() {
        // #Arrange ##
        var existingUser = new UserEntity(1L, "user_1", "password_1", "path", true);
        cut.insert(existingUser);

        var userToUpdate = new UserEntity(
                existingUser.getId(),
                existingUser.getUsername(),
                existingUser.getPassword(),
                existingUser.getImagePath() + "_updated",
                !existingUser.isEnabled()
        );
        // #Act ##
        cut.update(userToUpdate.getImagePath(), userToUpdate.isEnabled(), userToUpdate.getId());

        // #Assert ##
        var userEntity = cut.selectByUsername(existingUser.getUsername());
        assertThat(cut.selectByUsername(existingUser.getUsername())).contains(userToUpdate);
    }
}