package me.hyeonho.toby.user.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;

public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy{
  public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
  public static final int MIN_RECCOMEND_FOR_GOLD = 30;

  private final UserDao userDao;

  public UserLevelUpgradePolicyImpl(UserDao userDao) {
    this.userDao = userDao;
  }

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
    Properties props = new Properties();
    props.put("mail.stmp.host", "mail.ksug.org");
    Session s = Session.getInstance(props, null);

    MimeMessage message = new MimeMessage(s);
    try {
      message.setFrom(new InternetAddress("useradmin@ksug.org"));
      message.addRecipient(RecipientType.TO,
          new InternetAddress(user.getEmail()));
      message.setSubject("Upgrade 안내");
      message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");

      Transport.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
