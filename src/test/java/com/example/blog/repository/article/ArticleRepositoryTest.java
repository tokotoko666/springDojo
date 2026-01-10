package com.example.blog.repository.article;

import com.example.blog.config.MybatisDefaultDataSourceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisDefaultDataSourceTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository cut;

    @Test
    public void test() {
        assertThat(cut).isNotNull();
    }

    @Test
    @DisplayName("selectById: 指定されたIDの記事が存在するとき、ArticleEntity を返す")
    @Sql(statements = """
            DELETE FROM articles;
            DELETE FROM users;
            
            INSERT INTO users(id, username, password, enabled)
            VALUES(1, 'test_user_1', 'test_password_1', true);
            
            INSERT INTO articles(id, user_id, title, body, created_at, updated_at)
            VALUES(999, 1, 'title_999', 'body_999', '2010-10-01 00:00:00', '2010-11-01 00:00:00');
            """)
    public void selectById_returnArticleEntity() {
        // ## Arrange ##

        // ## Act ##
        var actual = cut.selectById(999L);

        // ## Assert ##
        assertThat(actual).isPresent()
                .hasValueSatisfying(articleEntity -> {
                    assertThat(articleEntity.getId()).isEqualTo(999L);
                    assertThat(articleEntity.getTitle()).isEqualTo("title_999");
                    assertThat(articleEntity.getBody()).isEqualTo("body_999");
                    assertThat(articleEntity.getCreatedAt()).isEqualTo("2010-10-01T00:00:00+09:00");
                    assertThat(articleEntity.getUpdatedAt()).isEqualTo("2010-11-01T00:00:00+09:00");
                });
    }

    @Test
    @DisplayName("selectById: 指定されたIDの記事が存在しないとき、Optional.empty を返す")
    public void selectById_returnEmpty() {
        // ## Arrange ##

        // ## Act ##
        var actual = cut.selectById(-999L);

        // ## Assert ##
        assertThat(actual).isEmpty();
    }
}