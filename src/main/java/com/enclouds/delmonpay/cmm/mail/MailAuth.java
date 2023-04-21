package com.enclouds.delmonpay.cmm.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuth extends Authenticator{

    PasswordAuthentication pa;

    public MailAuth() {
        String mail_id = "gminea0823@gmail.com";
        String mail_pw = "wlalsdld!!12";

        pa = new PasswordAuthentication(mail_id, mail_pw);
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return pa;
    }

}
