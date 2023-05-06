package com.example.galbaat.models;

public class messagemodel {

    String uID,message,messageID;
    long Timestamp;
    // know we have to create constructors


    public messagemodel(String uID, String message, String messageID, long timestamp) {
        this.uID = uID;
        this.message = message;
        this.messageID = messageID;
        Timestamp = timestamp;
    }

    public messagemodel(String uID, String message) {
        this.uID = uID;
        this.message = message;

    }

    // know we create an empty constructor for firebase
    public messagemodel(){

    }




    // know we generate getter and setter

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }
}
