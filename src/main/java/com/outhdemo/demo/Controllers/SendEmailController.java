package com.outhdemo.demo.Controllers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.outhdemo.demo.Models.GmailCredentials;
import com.outhdemo.demo.Models.SenderDetail;
import com.outhdemo.demo.Models.globals;
import com.outhdemo.demo.Services.GmailService;
import com.outhdemo.demo.Services.GmailServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@CrossOrigin(origins = { "http://localhost:9002" })
public class SendEmailController {

    @Value("${gmail.client.clientId}")
    private String clientId;

    @Value("${gmail.client.clientSecret}")
    private String clientSecret;

    @GetMapping("/email")
    public String greetingForm(Model model) {
        model.addAttribute("SenderDetail", new SenderDetail());
        return "email";
    }

    @PostMapping("/email")
    public String greetingSubmit(@ModelAttribute SenderDetail senderDetail) {
        System.out.println(senderDetail.getEmail());
        try {


            GmailService gmailService = new GmailServiceImpl(GoogleNetHttpTransport.newTrustedTransport());
            gmailService.setGmailCredentials(GmailCredentials.builder()
                    .userEmail(globals.getAttributes().getAttribute("Email"))
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .accessToken(globals.getAttributes().getAttribute("Token"))
                    .build());

            gmailService.sendMessage(senderDetail.getEmail(), senderDetail.getSubject(), senderDetail.getMessage());
        } catch (GeneralSecurityException | IOException | MessagingException e) {
            e.printStackTrace();
        }



        return "redirect:/home";
    }
}

