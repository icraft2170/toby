package me.hyeonho.toby.user.domain;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;

    private Level level;
    private int login;
    private int recommend;
    private String email;

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    @Builder
    public User(String id, String name, String password, Level level, int login, int recommend,
        String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
        this.email = email;
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        } else {
            this.level = nextLevel;
        }
    }
}
