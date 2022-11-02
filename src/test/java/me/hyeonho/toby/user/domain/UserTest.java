package me.hyeonho.toby.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
  User user;

  @BeforeEach
  void setUp() {
    user = new User();
  }

  @Test
  @DisplayName("User의 레벨 업그레이드 기능이 정상 작동하는지 여부를 테스트한다.")
  void upgradeLevel() {
    Level[] levels = Level.values();
    for (Level level : levels) {
      if (level.nextLevel() == null) continue;
      user.setLevel(level);
      user.upgradeLevel();
      assertThat(user.getLevel() == level.nextLevel()).isTrue();
    }
  }

  @Test
  @DisplayName("마지막 레벨에 대한 업그레이드 테스트")
  void cannotUpgradeLevel() {
    Level[] levels = Level.values();
    for (Level level : levels) {
      if (level.nextLevel() != null) continue;
      user.setLevel(level);
      assertThatCode(() -> user.upgradeLevel())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage(level + "은 업그레이드가 불가능합니다.");
    }
  }
}