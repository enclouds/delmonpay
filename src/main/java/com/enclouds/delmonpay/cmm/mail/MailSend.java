package com.enclouds.delmonpay.cmm.mail;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MailSend {

    private final String user = "delion1111@naver.com";
    private final String password = "wlalsdld!12";

    //private final String user = "risdi8657@gmail.com";
    //private final String password = "Hjcrew8208!@!";

    public void MailSend(HashMap<String, Object> mailData) {

        Properties prop = System.getProperties();
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.naver.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
       /* prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.naver.com");*/

        Authenticator auth = new MailAuth();

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        MimeMessage msg = new MimeMessage(session);

        try {
            msg.setSentDate(new Date());
            msg.setFrom(new InternetAddress(mailData.get("fromAddress").toString(), "월세페이"));
            InternetAddress to = new InternetAddress(mailData.get("toAddress").toString());
            msg.setRecipient(Message.RecipientType.TO, to);

            String ccAddress = mailData.get("ccAddress").toString();
            Vector receive_list = new Vector();

            StringTokenizer stk = new StringTokenizer(ccAddress, ";");
            while(stk.hasMoreTokens()){
                receive_list.addElement(stk.nextToken());
            }
            InternetAddress[] cc_address = new InternetAddress[receive_list.size()];
            for(int i=0;i<receive_list.size();i++) {
                cc_address[i] = new InternetAddress((String)receive_list.elementAt(i));
            }
            msg.setRecipients(Message.RecipientType.CC, cc_address);

            msg.setSubject(mailData.get("title").toString(), "UTF-8");

            String content = mailData.get("content").toString();

            msg.setText(content, "UTF-8");

            Transport.send(msg);
        } catch(AddressException ae) {
            System.out.println("AddressException : " + ae.getMessage());
        } catch(MessagingException me) {
            System.out.println("MessagingException : " + me.getMessage());
        } catch(UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException : " + e.getMessage());
        }

    }

    public static void main(String[] args) throws Exception {

        MailSend mailSend = new MailSend();

        HashMap<String, Object> mailData = new HashMap<>();
        mailData.put("fromAddress", "delion1111@naver.com");
        mailData.put("toAddress", "delion0823@naver.com");
        mailData.put("ccAddress", "risdi8657@gmail.com;paybank8001@daum.net;jangsos1111@naver.com;moumaleang2@gmail.com;bangmarket@naver.com;delion28@naver.com;delion96@naver.com;tnsgh8@naver.com;baeseongu@naver.com;mmkkop417@gmail.com");
        mailData.put("title", "월세 Mail TEST2");
        mailData.put("content", "월세 Mail TEST CONTENT2 \n test <br> test2");

        mailSend.MailSend(mailData);

    }

}
