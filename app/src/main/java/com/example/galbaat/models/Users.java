package com.example.galbaat.models;
// this is basically for fetching data to realtime database
public class Users {
    String profilePic;
    String username;
    String mail;
    String password;
    String userid;
    String lastMessage;
    String Status;

    // firstly we create an empty constructor that is used by the fire base that is important
    public Users() {

    }

    public Users(String profilePic, String username, String mail, String password, String userid, String lastMessage, String status) {
        this.profilePic = profilePic;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.userid = userid;
        this.lastMessage = lastMessage;
        Status = status;
    }


    // know create constructor for username,mail,password
    public Users(String username, String mail, String password) {
          this.username = username;
          this.mail = mail;
          this.password = password;
    }
// know we need getter and setter
    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}