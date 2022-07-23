package me.hyeonho.toby.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("레벨 초기화 테스트")
    void initialLevelSetting() {
        //when
        user.initialLevelSetting();
        //then
        assertThat(user.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    @DisplayName("레벨이 업그레이드 가능한 레벨인지 테스트 - 실버로 업그레이드 불가능할 때")
    void canUpgradeLevel_case_01() {
        //given
        user.setLevel(Level.BASIC);
        user.setLogin(49);
        //when
        boolean canUpgradeLevel = user.canUpgradeLevel();
        //then
        assertThat(canUpgradeLevel).isFalse();
    }
    @Test
    @DisplayName("레벨이 업그레이드 가능한 레벨인지 테스트 - 실버로 업그레이드 가능할 때")
    void canUpgradeLevel_case_02() {
        //given
        user.setLevel(Level.BASIC);
        user.setLogin(50);
        //when
        boolean canUpgradeLevel = user.canUpgradeLevel();
        //then
        assertThat(canUpgradeLevel).isTrue();
    }
    @Test
    @DisplayName("레벨이 업그레이드 가능한 레벨인지 테스트 - 골드로 업그레이드 불가능할 때")
    void canUpgradeLevel_case_03() {
        //given
        user.setLevel(Level.SILVER);
        user.setLogin(50);
        user.setRecommend(29);
        //when
        boolean canUpgradeLevel = user.canUpgradeLevel();
        //then
        assertThat(canUpgradeLevel).isFalse();
    }

    @Test
    @DisplayName("레벨이 업그레이드 가능한 레벨인지 테스트 - 골드로 업그레이드 가능할 때")
    void canUpgradeLevel_case_04() {
        //given
        user.setLevel(Level.SILVER);
        user.setLogin(50);
        user.setRecommend(30);
        //when
        boolean canUpgradeLevel = user.canUpgradeLevel();
        //then
        assertThat(canUpgradeLevel).isTrue();
    }

    @Test
    @DisplayName("레벨이 업그레이드 가능한 레벨인지 테스트 - 현재 레벨이 골드일 때")
    void canUpgradeLevel_case_05() {
        //given
        user.setLevel(Level.GOLD);
        user.setLogin(50);
        user.setRecommend(30);
        //when
        boolean canUpgradeLevel = user.canUpgradeLevel();
        //then
        assertThat(canUpgradeLevel).isFalse();
    }


    @Test
    @DisplayName("다음 레벨로 업그레이드 테스트 ( BASIC -> SILVER ) ")
    void nextLevel_case_01() {
        //given
        user.setLevel(Level.BASIC);
        //when
        user.nextLevel();
        //then
        assertThat(user.getLevel()).isEqualTo(Level.SILVER);
    }

    @Test
    @DisplayName("다음 레벨로 업그레이드 테스트  ( 실버 -> 골드 )")
    void nextLevel_case_02() {
        //given
        user.setLevel(Level.SILVER);
        //when
        user.nextLevel();
        //then
        assertThat(user.getLevel()).isEqualTo(Level.GOLD);
    }

    @Test
    @DisplayName("다음 레벨로 업그레이드 테스트 - nextLevel 이 Null 일 때")
    void nextLevel_case_ex() {
        //given
        user.setLevel(Level.GOLD);
        //when then
        assertThrows(IllegalArgumentException.class, () -> user.nextLevel());
    }
}