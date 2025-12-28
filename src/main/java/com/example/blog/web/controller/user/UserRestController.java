package com.example.blog.web.controller.user;

import com.example.blog.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<String> me(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserForm userform) {
        var newUser = userService.register(userform.username(), userform.password());
        var location = UriComponentsBuilder.fromPath("/users/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
