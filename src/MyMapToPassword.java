import java.util.HashMap;

public class MyMapToPassword {
    //declaring the field for my map
    private HashMap<Account, String> myMap;

    //getters and setters for my map so that it is accessible from other classes
    public HashMap<Account, String> getMyMap() {
        return myMap;
    }

    public void setMyMap(HashMap<Account, String> myMap) {
        this.myMap = myMap;
    }

    //constructor to create an object for the class to reference to
    public MyMapToPassword() {
        myMap = new HashMap<>();
    }

    //adding accounts to my hashmap database
    public void addElement(Account account){
        myMap.put(account, account.getPassword());
    }
}