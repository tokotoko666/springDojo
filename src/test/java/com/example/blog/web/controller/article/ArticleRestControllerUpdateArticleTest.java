package com.example.blog.web.controller.article;

import com.example.blog.security.LoggedInUser;
import com.example.blog.service.DateTimeService;
import com.example.blog.service.article.ArticleService;
import com.example.blog.service.user.UserService;
import com.example.blog.util.TestDateTimeUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ArticleRestControllerUpdateArticleTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @MockBean
    private DateTimeService mockDateTimeService;

    @Test
    void setUp() {
        assertThat(mockMvc).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(articleService).isNotNull();
    }

    @Test
    @DisplayName("PUT /articles/{articleId}: 記事の編集に成功する")
    void updateArticles_200OK() throws Exception {
        // ## Arrange ##
        when(mockDateTimeService.now())
                .thenReturn(TestDateTimeUtil.of(2020, 1, 1, 10, 20, 30))
                .thenReturn(TestDateTimeUtil.of(2020, 2, 1, 10, 20, 30));
        var newUser = userService.register("test_username", "test_password");
        var expectedUser = new LoggedInUser(newUser.getId(), newUser.getUsername(), newUser.getPassword(), newUser.isEnabled());
        var existingArticle = articleService.create(newUser.getId(), "test_title", "test_body");
        var expectedTitle = "test_title_updated";
        var expectedBody = "test_body_updated";
        var bodyJson = """
                {
                  "title": "%s",
                  "body": "%s"
                }
                """.formatted(expectedTitle, expectedBody);

        // ## Act ##
        var actual = mockMvc.perform(
                put("/articles/{articleId}", existingArticle.getId())
                        .with(csrf())
                        .with(user(expectedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson)
        );

        // ## Assert ##
        actual
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existingArticle.getId()))
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.body").value(expectedBody))
                .andExpect(jsonPath("$.author.id").value(expectedUser.getUserId()))
                .andExpect(jsonPath("$.author.username").value(expectedUser.getUsername()))
                .andExpect(jsonPath("$.createdAt").value(existingArticle.getCreatedAt().toString()))
                .andExpect(jsonPath("$.updatedAt", greaterThan(existingArticle.getCreatedAt().toString())))
        ;
    }

    @Test
    @DisplayName("PUT /articles/{articleId}: 指定されたIDの記事が存在しないとき、404を返す")
    void updateArticles_404OK() throws Exception {
        // ## Arrange ##
        var newUser = userService.register("test_username", "test_password");
        var expectedUser = new LoggedInUser(newUser.getId(), newUser.getUsername(), newUser.getPassword(), newUser.isEnabled());
        var invalidArticleId = 0;
        var bodyJson = """
                {
                  "title": "test_title_updated",
                  "body": "test_body_updated"
                }
                """;

        // ## Act ##
        var actual = mockMvc.perform(
                put("/articles/{articleId}", invalidArticleId)
                        .with(csrf())
                        .with(user(expectedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson)
        );

        // ## Assert ##
        actual
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("リソースが見つかりません"))
                .andExpect(jsonPath("$.instance").value("/articles/" + invalidArticleId))
        ;
    }
}