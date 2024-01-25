package ru.otus.services;

public class UserAuthServiceImpl implements UserAuthService {

    public UserAuthServiceImpl() {
    }

    @Override
    public boolean authenticate(String login, String password) {
        return login.equals("admin") && password.equals("admin");
    }
}
