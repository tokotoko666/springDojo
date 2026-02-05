package com.example.blog.repository.article;

import com.example.blog.config.MybatisDefaultDataSourceTest;
import com.example.blog.repository.user.UserRepository;
import com.example.blog.service.article.ArticleCommentEntity;
import com.example.blog.service.article.ArticleEntity;
import com.example.blog.service.user.UserEntity;
import com.example.blog.util.TestDateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisDefaultDataSourceTest
class ArticleCommentRepositoryTest {

    @Autowired
    private ArticleCommentRepository cut;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private ArticleCommentEntity article1Comment1;
    private ArticleCommentEntity article1Comment2;
    private ArticleCommentEntity article2Comment1;
    private ArticleEntity article1;

    @BeforeEach
    void beforeEach() {
        // article1 -> comment11, comment12  <- search target
        // article2 -> comment21             <- dummy

        var articleAuthor1 = new UserEntity(null, "test_username1", "test_password1", true);
        userRepository.insert(articleAuthor1);

        article1 = new ArticleEntity(null, "test_title1", "test_body1", articleAuthor1,
                TestDateTimeUtil.of(2020, 1, 1, 10, 30, 40),
                TestDateTimeUtil.of(2021, 1, 1, 10, 30, 40));
        articleRepository.insert(article1);

        var commentAuthor11 = new UserEntity(null, "test_username11", "test_password11", true);
        userRepository.insert(commentAuthor11);

        article1Comment1 = new ArticleCommentEntity(
                null,
                "test_comment_body11",
                article1,
                commentAuthor11,
                TestDateTimeUtil.of(2020, 1, 1, 10, 30, 40)
        );

        var commentAuthor12 = new UserEntity(null, "test_username12", "test_password12", true);
        userRepository.insert(commentAuthor12);

        article1Comment2 = new ArticleCommentEntity(
                null,
                "test_comment_body12",
                article1,
                commentAuthor12,
                TestDateTimeUtil.of(2020, 1, 1, 10, 30, 40)
        );

        var article2 = new ArticleEntity(null, "test_title2", "test_body2", articleAuthor1,
                TestDateTimeUtil.of(2020, 1, 1, 10, 30, 40),
                TestDateTimeUtil.of(2021, 1, 1, 10, 30, 40));
        articleRepository.insert(article2);

        var commentAuthor21 = new UserEntity(null, "test_username21", "test_password21", true);
        userRepository.insert(commentAuthor21);

        article2Comment1 = new ArticleCommentEntity(
                null,
                "test_comment_body21",
                article2,
                commentAuthor21,
                TestDateTimeUtil.of(2020, 1, 1, 10, 30, 40)
        );
    }

    @Test
    @DisplayName("insert: 記事コメントの作成に成功する")
    void insert_success() {
        // ## Arrange ##

        // ## Act ##
        cut.insert(article1Comment1);

        // ## Assert ##
        var actualOpt = cut.selectById(article1Comment1.getId());
        assertThat(actualOpt).hasValueSatisfying(actualEntity -> {
            assertThat(actualEntity)
                    .usingRecursiveComparison()
                    .ignoringFields("author.password", "article.author.password")
                    .isEqualTo(article1Comment1);
        });
    }

    @Test
    @DisplayName("selectById: 指定した記事コメントIDが存在するとき、記事コメントを返す")
    void selectById_success() {
        // ## Arrange ##
        cut.insert(article1Comment1);

        // ## Act ##
        var actualOpt = cut.selectById(article1Comment1.getId());

        // ## Assert ##
        assertThat(actualOpt).hasValueSatisfying(actualEntity -> {
            assertThat(actualEntity)
                    .usingRecursiveComparison()
                    .ignoringFields("author.password", "article.author.password")
                    .isEqualTo(article1Comment1);
        });
    }

    @Test
    @DisplayName("selectById: 指定した記事コメントIDが存在しないとき、空の Optional を返す")
    void selectById_returnEmpty() {
        // ## Arrange ##
        cut.insert(article1Comment1); // dummy record
        var notInsertedId = 0;

        // ## Act ##
        var actualOpt = cut.selectById(notInsertedId);

        // ## Assert ##
        assertThat(actualOpt).isEmpty();
    }

    @Test
    @DisplayName("selectByArticleId: 指定した記事IDにコメントが存在するとき、記事コメントのリストを返す")
    void selectByArticleId_success() {
        // ## Arrange ##
        cut.insert(article1Comment1);
        cut.insert(article1Comment2);
        cut.insert(article2Comment1);

        // ## Act ##
        var actual = cut.selectByArticleId(article1.getId());

        // ## Assert ##
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).usingRecursiveComparison()
                .ignoringFields("author.password", "article.author.password")
                .isEqualTo(article1Comment1);
        assertThat(actual.get(1)).usingRecursiveComparison()
                .ignoringFields("author.password", "article.author.password")
                .isEqualTo(article1Comment2);
    }
}