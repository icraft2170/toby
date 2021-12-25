package me.hyeonho.toby.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker{
    private ConnectionMaker target;
    private volatile int count = 0;

    public CountingConnectionMaker(ConnectionMaker target) {
        this.target = target;
    }

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        count++;
        return target.makeConnection();
    }

    public int getCount() {
        return count;
    }
}
