package me.hyeonho.toby.user.service;


import lombok.RequiredArgsConstructor;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@RequiredArgsConstructor
public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy{
  public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
  public static final int MIN_RECCOMEND_FOR_GOLD = 30;

  private final UserDao userDao;
  private final MailSender mailSender;

  @Override
  public void upgradeLevel(User user) {
    user.upgradeLevel();
    userDao.update(user);
    sendUpgradeEmail(user);
  }


  @Override
  public boolean canUpgradeLevel(User user) {
    Level currentLevel = user.getLevel();
    switch (currentLevel) {
      case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
      case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
      case GOLD: return false;
      default: throw new IllegalStateException("Unknown Level: " + currentLevel);
    }
  }

  private void sendUpgradeEmail(User user) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(user.getEmail());
    mailMessage.setFrom("useradmin@ksug.org");
    mailMessage.setSubject("Upgrade 안내");
    mailMessage.setText("사용자 등급이 " + user.getLevel().name());

    mailSender.send(mailMessage);
  }
}
