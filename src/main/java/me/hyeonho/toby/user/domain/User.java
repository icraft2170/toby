package me.hyeonho.toby.user.domain;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    private String password;
    Level level;
    int login;
    int recommend;

    @Builder
    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }
}
