package com.example.blog.service.article;

import com.example.blog.config.MybatisDefaultDataSourceTest;
import com.example.blog.repository.article.ArticleRepository;
import com.example.blog.repository.user.UserRepository;
import com.example.blog.service.DateTimeService;
import com.example.blog.service.user.UserEntity;
import com.example.blog.util.TestDateTimeUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MybatisDefaultDataSourceTest
@Import(ArticleService.class)
class ArticleServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService cut;
    @MockBean
    private DateTimeService mockDateTimeService;

    @Test
    void setup() {
        assertThat(userRepository).isNotNull();
        assertThat(articleRepository).isNotNull();
        assertThat(cut).isNotNull();
    }

    @Test
    @DisplayName("create: 記事の登録に成功するとデータベースの articles テーブルにレコードが insert される")
    void create_success() {
        // ## Arrange ##
        var expectedUser = new UserEntity(null, "test_user1", "test_password1", true);
        userRepository.insert(expectedUser);

        var expectedCurrentDateTime = TestDateTimeUtil.of(2020, 1, 2, 10, 20, 30);
        when(mockDateTimeService.now()).thenReturn(expectedCurrentDateTime);

        var expectedTitle = "test_article_title";
        var expectedBody = "test_article_body";

        // ## Act ##
        var actual = cut.create(expectedUser.getId(), expectedTitle, expectedBody);

        // ## Assert ##

        // assertion for return value
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(expectedTitle);
        assertThat(actual.getBody()).isEqualTo(expectedBody);
        assertThat(actual.getAuthor().getId()).isEqualTo(expectedUser.getId());
        assertThat(actual.getAuthor().getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(actual.getAuthor().getPassword()).isNull();
        assertThat(actual.getAuthor().isEnabled()).isEqualTo(expectedUser.isEnabled());
        assertThat(actual.getCreatedAt()).isEqualTo(expectedCurrentDateTime);
        assertThat(actual.getUpdatedAt()).isEqualTo(expectedCurrentDateTime);
    }

    @Test
    @DisplayName("findAll: 記事が1件も存在しないとき、空のリストを返す")
    @Sql(statements = """
            DELETE FROM articles;
            """)
    void findAll_returnEmptyList() {
        // ## Arrange ##

        // ## Act ##
        var actual = cut.findAll();

        // ## Assert ##
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("findAll: 記事が存在するとき、リストを返す")
    @Sql(statements = """
            DELETE FROM articles;
            """)
    void findAll_returnNonEmptyList() {
        // ## Arrange ##
        var user1 = new UserEntity();
        user1.setUsername("test_username1");
        user1.setPassword("test_password1");
        user1.setEnabled(true);
        userRepository.insert(user1);

        when(mockDateTimeService.now())
                .thenReturn(TestDateTimeUtil.of(2020, 1, 10, 10, 10, 10))
                .thenReturn(TestDateTimeUtil.of(2021, 1, 10, 10, 10, 10));
        var expectedArticle1 = cut.create(user1.getId(), "test_title1", "test_body1");
        var expectedArticle2 = cut.create(user1.getId(), "test_title2", "test_body2");

        // ## Act ##
        var actual = cut.findAll();

        // ## Assert ##
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).isEqualTo(expectedArticle2);
        assertThat(actual.get(1)).isEqualTo(expectedArticle1);
    }
}