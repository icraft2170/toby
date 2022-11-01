package me.hyeonho.toby.user.dao;

import me.hyeonho.toby.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao{
    private List<User> users;
    private List<User> updated = new ArrayList<>();

    public MockUserDao(List<User> users) {
        this.users = users;
    }

    public List<User> getUpdated() {
        return updated;
    }

    @Override
    public List<User> getAll() {
        // Mock 기능 제공
        return this.users;
    }

    @Override
    public void update(User user) {
        // Stub 기능 제공
        updated.add(user);
    }

    @Override
    public void add(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User get(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException();
    }
}
