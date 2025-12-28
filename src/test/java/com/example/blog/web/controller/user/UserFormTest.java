package com.example.blog.web.controller.user;

import com.example.blog.model.UserForm;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserFormTest {

    private ValidatorFactory validatorFactory;
    private Validator validator;

    @BeforeEach
    void beforeEach() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    void afterEach() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("username のバリデーション：成功")
    void username_success() {
        // ## Arrange ##
        var userForm = new UserForm("username00", "password00");

        // ## Act ##
        var actual = validator.validate(userForm);

        // ## Assert ##
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("username のバリデーション：失敗")
    void username_failure() {
        // ## Arrange ##
        var userForm = new UserForm(null, "password00");

        // ## Act ##
        var actual = validator.validate(userForm);

        // ## Assert ##
        assertThat(actual).isNotEmpty();
        assertThat(actual).anyMatch(violation -> violation.getPropertyPath().toString().equals("username"));
    }
}
