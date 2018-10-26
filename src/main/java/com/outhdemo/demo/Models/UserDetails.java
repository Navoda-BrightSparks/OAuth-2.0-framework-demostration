package com.outhdemo.demo.Models;

public class UserDetails {

   private String emailAddress,historyId;
   private int messagesTotal,threadsTotal;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public int getMessagesTotal() {
        return messagesTotal;
    }

    public void setMessagesTotal(int messagesTotal) {
        this.messagesTotal = messagesTotal;
    }

    public int getThreadsTotal() {
        return threadsTotal;
    }

    public void setThreadsTotal(int threadsTotal) {
        this.threadsTotal = threadsTotal;
    }

    public UserDetails(String emailAddress, String historyId, int messagesTotal, int threadsTotal) {
        this.emailAddress = emailAddress;
        this.historyId = historyId;
        this.messagesTotal = messagesTotal;
        this.threadsTotal = threadsTotal;
    }
}
