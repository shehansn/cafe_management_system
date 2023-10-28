package com.sn.shehan_n.cafe_management_system.email.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {
    @Autowired
    private JavaMailSender javaMailSender;
    private String fromEmail = "shehannishara18@gmail.com";

    public void sendSimpleMessage(String toEmail, String subject, String text, List<String> listOfEmails) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        if (listOfEmails != null && listOfEmails.size() > 0) {
            message.setCc(getCCArray(listOfEmails));
        }
        javaMailSender.send(message);
    }

    private String[] getCCArray(List<String> ccList) {
        String[] cc = new String[ccList.size()];
        for (int i = 0; i < ccList.size(); i++) {
            cc[i] = ccList.get(i);
        }
        return cc;
    }
}
