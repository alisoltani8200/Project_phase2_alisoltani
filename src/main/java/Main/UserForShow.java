package Main;

import java.lang.ref.PhantomReference;

public class UserForShow {
    private String userName;
    private String name;
    private String accountType;

    public UserForShow(String userName, String name, String accountType) {
        this.userName = userName;
        this.name = name;
        this.accountType = accountType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
