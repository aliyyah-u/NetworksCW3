public class Account {
    private String email;
    private String recipient;
    private String password;

    //add getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Account(String email, String recipient, String password) {
        this.email = email;
        this.recipient = recipient;
        this.password = password;
    }
}

