package com.example.blog;

import org.junit.jupiter.api.Test;

import java.util.Base64;

public class SpringSessionBase64Test {

    @Test
    void test() {
        var uuid = "a8aab199-fb23-4506-bc3d-cc56e7a7a64b";
        var base64Bytes = Base64.getEncoder().encode(uuid.getBytes());
        var base64String = new String(base64Bytes);
        System.out.println(base64String);
    }
}
