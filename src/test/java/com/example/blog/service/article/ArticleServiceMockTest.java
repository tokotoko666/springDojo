package com.example.blog.service.article;

import com.example.blog.repository.article.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceMockTest {

    @InjectMocks
    private ArticleService cut;
    @Mock
    private ArticleRepository articleRepository;

    @Test
    public void cut() {
        assertThat(cut).isNotNull();
    }

    @Test
    public void mockPractice() {
        when(cut.findById(999L)).thenReturn(Optional.of(
                new ArticleEntity(999, "", "", null, null)
        ));

        assertThat(articleRepository.selectById(999L)).isPresent()
                .hasValueSatisfying(articleEntity -> {
                    assertThat(articleEntity.id()).isEqualTo(999L);
                });
        assertThat(articleRepository.selectById(111L)).isEmpty();
    }
}