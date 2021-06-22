package com.cybertek.service;

import com.cybertek.entity.ConfirmationToken;
import com.cybertek.exception.TicketingProjectException;
import org.springframework.mail.SimpleMailMessage;

public interface ConfirmationTokenService {

    ConfirmationToken save(ConfirmationToken confirmationToken);    //we have the token

    void sendEmail(SimpleMailMessage email);                        // we send the mail

    ConfirmationToken readByToken(String token) throws TicketingProjectException;   // make sure token  is confirmed

    void delete(ConfirmationToken confirmationToken);   //after mission is completed DELETE it





}
