package com.example.blog.repository.article;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
            INSERT INTO articles(id, title, body, created_at, updated_at)
            VALUES(999, 'title_999', 'body_999', '2010-10-01 00:00:00', '2010-11-01 00:00:00');
            """)
    public void selectById_returnArticleEntity() {
        // ## Arrange ##

        // ## Act ##
        var actual = cut.selectById(999L);

        // ## Assert ##
        assertThat(actual).isPresent()
                .hasValueSatisfying(articleEntity -> {
                    assertThat(articleEntity.id()).isEqualTo(999L);
                    assertThat(articleEntity.title()).isEqualTo("title_999");
                    assertThat(articleEntity.content()).isEqualTo("body_999");
                    assertThat(articleEntity.createdAt()).isEqualTo("2010-10-01T00:00:00");
                    assertThat(articleEntity.updatedAt()).isEqualTo("2010-11-01T00:00:00");
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