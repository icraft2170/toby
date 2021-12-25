package me.hyeonho.toby.user.dao;

public class DaoFactory {
    public UserDao userDao(){
        UserDao userDao = new UserDao(connectionMaker());
        return userDao;
    }

    private ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
