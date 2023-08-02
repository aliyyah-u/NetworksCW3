import java.util.ArrayList;

public class MyEmailList {
    private ArrayList<Account> myList;

    public ArrayList<Account> getMyList() {
        return myList;
    }

    public void setMyList(ArrayList<Account> myList) {
        this.myList = myList;
    }

    public MyEmailList() {
        this.myList = new ArrayList<Account>();
    }

    public void addElement(Account myAccount){
        myList.add(myAccount);
    }
}





