import java.util.HashMap;
import java.util.Map;

public class Database {
    private Map<String, String> userMap = new HashMap<>();

    public Database() {
        // Initialise the userMap with sample sender addresses and passwords
        userMap.put("kant@umail.com", "crocodile99!");
        userMap.put("ruby@umail.com", "monkey88!");
        userMap.put("joe@umail.com", "tiger772?");
        userMap.put("jack@umail.com", "parrot7831?");
        userMap.put("fernanda@umail.com", "grasshopper87.");
    }

    public boolean verifyCredentials(String senderAddress, String password) {
        String storedPassword = userMap.get(senderAddress);
        return storedPassword != null && storedPassword.equals(password);
    }
}
