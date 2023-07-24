import java.util.HashMap;

public class MyHashmap {

    public static void main(String[] args) {
        // Create a HashMap object called umailEmails
        HashMap<String, String> umailEmails = new HashMap<String, String>();

        // Add keys and values (From, To)
        umailEmails.put("glenda@umail.com", "tom.hanks@umail.com");
        umailEmails.put("brendon@umail.com", "berlin@umail.com");
        umailEmails.put("tom.hanks@umail.com", "berlin@umail.com");
        umailEmails.put("debra@umail.com", "kant@umail.com");
        umailEmails.put("berlin@umail.com", "glenda@umail.com");
        umailEmails.put("kant@umail.com", "fernanda@umail.com");
        umailEmails.put("ruby@umail.com", "debra@umail.com");
        umailEmails.put("joe@umail.com", "fernanda@umail.com\"");
        umailEmails.put("jack@umail.com", "ruby@umail.com");
        umailEmails.put("fernanda@umail.com", "ruby@umail.com");
        System.out.println(umailEmails);
    }
}

