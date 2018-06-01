package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String mUsername;
    private ArrayList<Mail> mMailList;

    public User() {
        // Generic constructor
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public ArrayList<Mail> getMailList() {
        return mMailList;
    }

    public void updateMailList(ArrayList<Mail> mailList) {
        this.mMailList = mailList;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> userMap = new HashMap<>();
        // add any values that need to be saved here
        return userMap;
    }
}
