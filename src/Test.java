public class Test {
        public static void main(String[] args) {
            // Creating Account objects with emails and passwords
            Account account1 = new Account("kant@umail.com", "", "crocodile99!");
            Account account2 = new Account("ruby@umail.com","", "monkey88!");
            Account account3 = new Account("joe@umail.com","", "tiger772?");
            Account account4 = new Account("jack@umail.com", "","parrot7831?");
            Account account5 = new Account("fernanda@umail.com","", "grasshopper87.");

            MyEmailList list = new MyEmailList();
            list.addElement(account1);
            list.addElement(account2);
            list.addElement(account3);
            list.addElement(account4);
            list.addElement(account5);

            printList(list);

            MyMapToPassword myMap = new MyMapToPassword();
            myMap.addElement(account1);
            myMap.addElement(account2);
            myMap.addElement(account3);
            myMap.addElement(account4);
            myMap.addElement(account5);

            System.out.println(isInValues(myMap,"monkey88!"));
            System.out.println(isInValues(myMap,"grasshopper87."));
        }

    public static void printList(MyEmailList list){
        for (int i = 0; i < list.getMyList().size(); i++) {
            System.out.println(list.getMyList().get(i).getEmail());
        }
    }
    public static Boolean isInValues(MyMapToPassword myMap, String s){
        return myMap.getMyMap().containsValue(s);
    }
}




