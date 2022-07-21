package me.hyeonho.toby.user.domain;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
public class User {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;
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

    public void initialLevelSetting() {
        if (this.level == null)
        this.level = Level.BASIC;
    }

    public boolean canUpgradeLevel() {
        switch (this.level) {
            case BASIC: return (this.login >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (this.recommend >= MIN_RECCOMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level : " + this.level);
        }
    }

    public void nextLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalArgumentException(this.level + "은 업그레이드가 불가능합니다.");
        } else {
            this.level = nextLevel;
        }
    }
}
