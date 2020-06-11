package com.qing.tea.utils;

import com.qing.tea.entity.Email;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
@Component
public class MailUtils {
    /**
     * 邮件发送者
     */
    @Value("${spring.mail.username}")
    private String MAIL_SENDER;

    @Autowired
    private JavaMailSender javaMailSender;


    private Logger logger = LoggerFactory.getLogger(MailUtils.class);

    private int retryNumber = 0;

    /**
     * 发送文本邮件
     *
     * @param email
     */
    @Async
    public void sendSimpleMail(Email email) {
        logger.info("开始发送邮件");
        long start = System.currentTimeMillis();
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(MAIL_SENDER);
            mailMessage.setTo(email.getRecipient());
            mailMessage.setSubject(email.getSubject());
            mailMessage.setText(email.getContent());
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            logger.error("邮件发送失败，100毫秒后将进行重试操作", e.getMessage());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (retryNumber < 10){
                retryNumber++;
                sendSimpleMail(email);
            } else {
                retryNumber = 0;
            }
            return;
        }
        logger.info("邮件发送成功，耗时" + (System.currentTimeMillis() - start) + "毫秒");
    }
}
