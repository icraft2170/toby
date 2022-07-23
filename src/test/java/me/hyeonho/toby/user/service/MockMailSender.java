package me.hyeonho.toby.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.List;

public class MockMailSender implements MailSender {
    public List<String> requests = new ArrayList<>();

    public List<String> getRequests() {
        return requests;
    }

    @Override
    public void send(SimpleMailMessage mailMessage) throws MailException {
        requests.add(mailMessage.getTo()[0]);
    }

    @Override
    public void send(SimpleMailMessage[] mailMessage) throws MailException {

    }
}