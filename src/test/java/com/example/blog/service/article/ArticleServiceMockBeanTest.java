package com.example.blog.service.article;

import com.example.blog.config.MybatisDefaultDataSourceTest;
import com.example.blog.repository.article.ArticleRepository;
import com.example.blog.util.TestDateTimeUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MybatisDefaultDataSourceTest
@Import(ArticleService.class)
class ArticleServiceMockBeanTest {

    @Autowired
    private ArticleService cut;
    @MockBean
    private ArticleRepository articleRepository;

    @Test
    public void cut() {
        assertThat(cut).isNotNull();
    }

    @Test
    public void mockPractice() {
        when(cut.findById(999L)).thenReturn(Optional.of(
                new ArticleEntity(999L, "", "", null, null, null)
        ));

        assertThat(articleRepository.selectById(999L)).isPresent()
                .hasValueSatisfying(articleEntity -> {
                    assertThat(articleEntity.getId()).isEqualTo(999L);
                });
        assertThat(articleRepository.selectById(111L)).isEmpty();
    }

    @Test
    @DisplayName("findById: 指定された ID の記事が存在するとき、ArticleEntity を返す")
    public void findById_returnArticleEntity() {
        // ## Arrange ##
        when(articleRepository.selectById(999L)).thenReturn(Optional.of(
                new ArticleEntity(
                        999L,
                        "title_999",
                        "body_999",
                        null,
                        TestDateTimeUtil.of(2022, 1, 1, 10, 0, 0),
                        TestDateTimeUtil.of(2022, 2, 1, 11, 0, 0)
                        )
        ));
        // ## Act ##
        var actual = cut.findById(999L);

        // ## Assert ##
        assertThat(actual).isPresent()
                .hasValueSatisfying(articleEntity -> {
                    assertThat(articleEntity.getId()).isEqualTo(999L);
                    assertThat(articleEntity.getTitle()).isEqualTo("title_999");
                    assertThat(articleEntity.getBody()).isEqualTo("body_999");
                    assertThat(articleEntity.getCreatedAt()).isEqualTo("2022-01-01T10:00:00");
                    assertThat(articleEntity.getUpdatedAt()).isEqualTo("2022-02-01T11:00:00");
                });
    }

    @Test
    @DisplayName("findById: 指定された ID の記事が存在しないとき、Optional.Empty を返す")
    public void findById_returnEmpty() {
        // ## Arrange ##
        when(articleRepository.selectById(-999)).thenReturn(Optional.empty());

        // ## Act ##
        var actual = cut.findById(-999L);

        // ## Assert ##
        assertThat(actual).isEmpty();
    }
}