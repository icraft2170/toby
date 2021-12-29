package me.hyeonho.toby.user.domain;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;

    @Builder
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
