package jdbc;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.lang.System;
import java.lang.*;

//interface for the comman users of the application Customer and Owner
interface user
{
        void show_O_C_details();
        void C_reward();

}
//class to manage food items
class Item extends zotato_records
{
        private static int uID;
        final String I_Name;
        final int I_price;
        final int iId;
        private  int I_quantity;
        final int I_discount ;
        private final String I_category;
        Restaurant Distributor;
        Item(String name, int price,int quantity,int discount,String category)
        {
                this.iId= getID()+1;
                setID(this.getiId());
                this.I_Name=name;
                this.I_discount=discount;
                this.I_price=price;
                this.I_quantity=quantity;
                this.I_category=category;
        }
        public String getcategory() {
                return I_category;
        }

        int getiId() {
                return iId;
        }

        protected int getID() {
                return uID;
        }
        public static void setID(int uID) {
                Item.uID = uID;
        }



        protected int getI_discount() {
                return I_discount;
        }

        protected int getI_price() {
                return I_price;
        }

        public int getI_quantity() {
                return I_quantity;
        }

        public String getI_Name() {
                return I_Name;
        }

        public int getuID() {
                return uID;
        }

        public Object getDistributor() {
                return Distributor;
        }

        public void setI_Quantity(int i) {
                I_quantity = i;
        }
}
//class to manage Orders of the customers
class Orders extends zotato_records
{
        final Item item;
        final float Price;
        final int  Quantity;
        Orders(Item name, float money, int quant)
        {
                this.item = name;
                this.Price = money;
                this.Quantity = quant;
        }
        public int getquantity() {
                return Quantity;
        }

        public Item getitem() {
                return item;
        }


        public float getprice() {
                return Price;
        }
}
class ShoppingCart extends zotato_records
{
        protected final ArrayList<Orders> myItems= new ArrayList<>();


        public ArrayList<Orders> getMyItems() {
                return myItems;
        }

        public void add2Cart(Item myitem, int itemquantity)
        {

                int c_Price = myitem.getI_price();
                Orders Myorder = new Orders(myitem, c_Price, itemquantity);
                myItems.add(Myorder);
        }
}
//class to manage customers
class Customer extends zotato_records implements user {

        final String name;
        final String Address;
        int account_balance;
        final int cid;
        final ShoppingCart custCart= new ShoppingCart();
        final String type;
        public ArrayList<String> cart= new ArrayList<>();
        private final ArrayList<Orders> orders= new ArrayList<>();
        int totalrewards;
        private final Float Original_acc;
        int all_orders;

        Customer(String name, String Address, int cid, String type,int rewards)
        {
                this.name = name;
                this.Address = Address;
                this.cid = cid;
                this.type = type;
                this.totalrewards=rewards;
                this.Original_acc = Float.parseFloat("1000");

        }
        public int getall_orders() {

                return all_orders;
        }

        public void setall_orders(int all_orders) {
                this.all_orders = all_orders;
        }
        public void list_increment(Orders o)
        {

                orders.add(0, o);;


                if(orders.size()>10)
                {

                        orders.remove(orders.size()-1);
                }
        }
        public void C_checkout(ShoppingCart custCart, zotato_records zotato)
        {

                for (Orders o : custCart.getMyItems()) {
                        Float current_wallet = getOriginal_acc();
                        Float order_price = o.getprice();
                        if (current_wallet > order_price) {
                                if (o.getquantity() <= o.getitem().getI_quantity()) {
                                        current_wallet = current_wallet - order_price;
                                        o.getitem().setI_Quantity(o.getitem().getI_quantity() - o.getquantity());
                                        System.out.println("Successful purchase done :");
                                        this.list_increment(o);

                                        //calculating the companies profits
                                        Float zotato_price = Float.parseFloat(String.valueOf((o.getprice() / (1.01)) ));
                                        zotato.setnet_earned(zotato.getnet_earned() + zotato_price);
                                        this.setall_orders(getall_orders() + 1);
                                } else {
                                        System.out.println("\n\t Sorry the item is  " + o.getitem().getI_Name() + "no more available  \t");
                                }

                                this.setOriginal_acc(current_wallet);


                        }
                }
        }

        public  Float getOriginal_acc() {
                return Original_acc;
        }
        public void setOriginal_acc(Float Original_acc) {
                Original_acc = Original_acc;
        }

        public ShoppingCart getCustCart() {
                return custCart;
        }
        public int getcid() {
                return cid;
        }

        public String getcname() {
                return name;
        }

        public int getrewards() {
                return totalrewards;
        }
        private String getAddress() {
                return Address;
        }

        public String get_type() {
                return type;
        }

        public void Restaurant_look(zotato_records zotato) throws IOException {

                System.out.println("  Restaurant List   ");
                ArrayList<Restaurant> restaurant = zotato_records.getRestaurant();
                for (int i = 0, restaurantSize = restaurant.size(); i < restaurantSize; i++) {
                        Restaurant re = restaurant.get(i);
                        System.out.println("------" + re.getiid() + "  " + re.getresname() + "(" + re.get_type() + ")");
                }
                Scanner sc = new Scanner(System.in);
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                StringTokenizer st = new StringTokenizer(br.readLine());
                System.out.println();
                ArrayList<Restaurant> cat = zotato_records.getRestaurant();

                for (Restaurant s : cat) {
                        System.out.println("-----" + s.getresname() +s.getCategory());
                }
                String category_choice = sc.nextLine().toUpperCase();

                ArrayList<Item> product_list = zotato_records.getItem_list();
                System.out.println();
                System.out.println("\t ITEMS UNDER YOUR CHOSEN CATEGORY :-");
                for (int i = 0; i < product_list.size(); i++) {
                        Item product = product_list.get(i);
                        if (category_choice.equals(product.getcategory().toUpperCase())) {
                                System.out.print(" Food Id :  " + product.getiId() + " Food Name:  " + product.getI_Name() + " Food price:  " + product.getI_price() + "  ");
                        }


                }
                System.out.println("------------------The menu to move forward in the process----------------------");
                System.out.println("1. Add Food Item to Cart") ;
                System.out.println("2. Exit the current option");
                int mchoice=sc.nextInt();
                if (mchoice == 1) {
                        ShoppingCart ccart = getCustCart();
                        char pick = 'y';
                        System.out.println();
                        System.out.println("\t\t ADD ITEMS TO TOUR CART \n");
                        while (pick == 'y') {
                                System.out.println("\t Enter Item Code ");
                                int code = sc.nextInt();
                                System.out.println("\t Enter Quantity ");
                                int quant = sc.nextInt();
                                System.out.println();
                                Item it = null;
                                for (Item itm : zotato_records.getItem_list()) {
                                        if (code == itm.getiId()) {
                                                it = itm;
                                        }
                                }

                                ccart.add2Cart(it, quant);
                                System.out.println("Want to add More ? (y/n)");
                                pick = sc.next().charAt(0);
                        }
                } else if (mchoice == 2) {
                        System.out.println("You are going to the previous menu");
                }

        }
        @Override
        public void show_O_C_details()
        {
                System.out.println(" Customer's Details");
                System.out.println("  Name : "+getcname());
                System.out.println("  ID No.  : "+getcid());
                System.out.println(" Address : "+getAddress());
        }


        public ArrayList<Orders> get_Orders() {
                return orders;
        }
        @Override
        public void C_reward()
        {
                for (Customer ignored : zotato_records.getCustomer())
                {
                        for(Restaurant res: zotato_records.getRestaurant())
                        {
                                switch (res.get_type()) {
                                        case "authentic":
                                                for (int i = 200; i <= 1000; i = i + 200) {
                                                        totalrewards = totalrewards + 20;
                                                }
                                                break;
                                        case "fastfood":
                                                for (int i = 150; i <= 1000; i = i + 150) {
                                                        totalrewards = totalrewards + 10;
                                                }
                                                break;
                                }
                        }
                        get_ctotalrewards.add(totalrewards);
                }
        }
}
//class to manage the records of the users , company and the owners
class zotato_records {
        public static final ArrayList<Restaurant> Restaurant_list = new ArrayList<>();
        public static final ArrayList<Item> item_list = new ArrayList<>();
        public static final ArrayList<Customer> Customer_list= new ArrayList<>();
        public static final ArrayList<String> available_category = new ArrayList<>();
        public static final ArrayList<Integer> get_ctotalrewards= new ArrayList<>();

        final ArrayList <String> category= new ArrayList<>();
        private static  float net_earned;

        // public static ArrayList<String>Category=new ArrayList<String>();

        protected static ArrayList<Item> getItem_list() {
                return item_list;
        }

        public static float getbalance() {
                float balance = 1000;
                return balance;
        }
        public static Float getnet_earned() {
                return net_earned;
        }

        public ArrayList<String> getCategory() {
                return category;
        }

        List<Restaurant> get_restaurans() {
                return Restaurant_list;
        }

        protected void add_Restaurans(Restaurant rt) {
                Restaurant_list.add(rt);
        }
        public void setnet_earned(double d) {
                net_earned = Float.parseFloat(String.valueOf(d));
        }
        public static ArrayList<Restaurant> getRestaurant() {
                return Restaurant_list;
        }

        public void add_item_list(Item i) {
                item_list.add(i);
        }
        public static ArrayList<Customer> getCustomer() {
                return Customer_list;
        }
        protected void add_Customers(Customer cs) {
                Customer_list.add(cs);
        }

}
//class to manage restaurants
class Restaurant extends zotato_records implements user {
        final String type;
        final String name;
        private final int iid;
        final String address;
        final ArrayList<Item> item= new ArrayList<>();
        Restaurant(String name, String address, String type, int iid) {
                this.name = name;
                this.address = address;
                this.iid = iid;
                this.type = type;
        }

        public int getiid() {
                return iid;
        }

        public String getresname() {
                return name;
        }

        public String getownwername() {
                return name;
        }

        public String get_type() {
                return type;
        }

        public String getAddress() {
                return address;
        }

        public void Add(zotato_records zotato) throws IOException {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                Scanner sc = new Scanner(System.in);
                System.out.println("\t Enter the new item details : ");
                System.out.println(" Item Name : ");
                String i_name = sc.nextLine();
                System.out.println(" Item Price : ");
                int i_price = sc.nextInt();
                System.out.println(" Item Category : ");
                String i_category = br.readLine();
                System.out.println(" Item Quantity : ");
                int i_quantity = sc.nextInt();
                System.out.println("Item Discount : ");
                int discount = sc.nextInt();
                Item I_item = new Item(i_name, i_price, i_quantity, discount,i_category);
                item.add(I_item);
                boolean flag = category.stream().noneMatch(value -> i_category.compareTo(value) == 0);
                if(flag)
                {
                        category.add(i_category);
                        for (String value : Customer.available_category)
                                if (i_category.compareTo(value) == 0) {
                                        flag = false;
                                        break;
                                }
                        if (flag)
                                Customer.available_category.add(i_category);
                }
                zotato.add_item_list(I_item);

                for (Item it : zotato_records.getItem_list())
                {
                        System.out.println(" id:\t " + it.getiId() + " name:\t " + it.getI_Name() + " price:\t  " + it.getI_price() + " quantity:\t" + it.getI_quantity() + " discount:\t" + it.getI_discount()+" category:\t "+it.getcategory());
                }
        }
        //function to edit the food items
        public void Edit(zotato_records zotato) throws IOException {
                for (Item it : zotato_records.getItem_list()) {
                        System.out.println(" id: " + it.getiId() + " name: " + it.getI_Name() + " price:  " + it.getI_price() + " quantity:" + it.getI_quantity() + " discount:" + it.getI_discount()+" category: "+it.getcategory());
                }
                Scanner sc = new Scanner(System.in);
                System.out.println("  Enter the code of item to be edited : ");
                int i_id = sc.nextInt();
                Item i_code = null;
                for (Iterator<Item> iterator = getItem_list().iterator(); iterator.hasNext(); ) {
                        Item it = iterator.next();
                        if (it.getiId() == i_id) {
                                i_code = it;
                        }
                }
                if (i_code == null) {
                        System.out.println("Invalid code");
                } else {
                        getItem_list().remove(i_code);
                }

                Add(zotato);
        }

        public void Print(zotato_records zotato) {
                Scanner sc = new Scanner(System.in);

                System.out.println("the reward points of the customer are");
                ArrayList<Customer> customer = Customer.getCustomer();
                for (int i = 0, customerSize = customer.size(); i < customerSize; i++) {
                        Customer cus = customer.get(i);
                        System.out.println("The: " + cus.getcid() + "  " + cus.getcname());
                }
                System.out.println("Enter the customer id you want to get the rewards points of");
                int code_id = sc.nextInt();
                System.out.println("the reward points of the customer are");

                switch (code_id) {
                        case 1:
                                System.out.println("for customer with id :" + 1 + "rewards " + 20);
                                break;
                        case 2:
                                System.out.println("for customer with id :" + 2 + "rewards " + 0);
                                break;
                        case 3:
                                System.out.println("for customer with id :" + 3 + "rewards " + 0);
                                break;
                        case 4:
                                System.out.println("for customer with id :" + 4 + "rewards" + 0);
                                break;
                        case 5:
                                System.out.println("for customer with id :" + 5 + "rewards " + 5);
                                break;
                        case 6:
                                System.out.println("for customer with id :" + 6 + "rewards " + 10);
                                break;
                }
        }

        public void Discount(zotato_records zotato) {
                Scanner sc = new Scanner(System.in);
                for (Restaurant re : zotato_records.getRestaurant()) {
                        switch (re.get_type()) {
                                case "rest":

                                        int discount = sc.nextInt();
                                        double bill = 100;
                                        bill = discount * 0.01 * bill;
                                        System.out.println(bill);
                                        break;
                        }
                }
        }
        @Override
        public void show_O_C_details() {
                System.out.println("Restaurant Owner's Name");
                System.out.println(" Owner Name : "+getresname());
                System.out.println("  ID No.  : "+getiid());
                System.out.println(" Address : "+getAddress());

        }
        @Override
        public void C_reward()
        {
                ArrayList<Customer> customer = zotato_records.getCustomer();
                for (int j = 0; j < customer.size(); j++) {
                        Customer ignored = customer.get(j);
                        int totalrewards = 0;
                        for (Restaurant res : zotato_records.getRestaurant()) {
                                if (res.get_type().equals("authentic")) {
                                        for (int i = 200; i <= 1000; i = i + 200) {
                                                totalrewards = totalrewards + 20;
                                        }
                                } else if (res.get_type().equals("fastfood")) {
                                        for (int i = 150; i <= 1000; i = i + 150) {
                                                totalrewards = totalrewards + 10;
                                        }
                                }
                        }
                        get_ctotalrewards.add(totalrewards);
                }



        }
}
// class to do the integeration of all the functionalites
public class Main {

        public static void main(String[] args) throws IOException {
                // write your code here
                Scanner sc = new Scanner(System.in);
                Restaurant one = new Restaurant("Shivaji", "janak", "fastfood", 1);
                Restaurant two = new Restaurant("Kashmiri", "multimedia palace", "authentic", 2);
                Restaurant three = new Restaurant("Rajishri", "taj  palace", "authentic", 3);
                Restaurant four = new Restaurant("Lahori", "cineplex palace", "fastfood", 4);
                Restaurant five = new Restaurant("allahabadi", "chanakyapuri", "fastfood ", 5);
                Restaurant six = new Restaurant("punjabi", "near gurudwara ", "rest", 6);

                zotato_records zotato = new zotato_records();
                zotato.add_Restaurans(one);
                zotato.add_Restaurans(two);
                zotato.add_Restaurans(three);
                zotato.add_Restaurans(four);
                zotato.add_Restaurans(five);
                zotato.add_Restaurans(six);

                Customer one_1 = new Customer("Ram", "Himachal", 1, "Elite",20);
                Customer two_2 = new Customer("Robert", "Ludhiana", 2, "Special",10);
                Customer three_3 = new Customer("Raj", "Chicago", 3, "Elite",20);
                Customer four_4 = new Customer("Shyam", "Delhi", 4, "Special",10);
                Customer five_5 = new Customer("Raju", "AP", 5, "rest",5);
                Customer six_6 = new Customer("Baburao", "Lakshdweep ", 6, "rest",5);

                zotato.add_Customers(one_1);
                zotato.add_Customers(two_2);
                zotato.add_Customers(three_3);
                zotato.add_Customers(four_4);
                zotato.add_Customers(five_5);
                zotato.add_Customers(six_6);
                int choice = 0;
                while (choice != 5)  {
                        System.out.println("\t\t WELCOME TO YOUR OWN HOMEMADE HOTEL ZOTATO \t\t");
                        System.out.println("\t 1. Enter as Restaurant Owner \t");
                        System.out.println("\t 2. Enter as Customer \t");
                        System.out.println("\t 3. Check User Details \t");
                        System.out.println("\t 4. Company Account details \t");
                        System.out.println("\t 5. Exit \t");
                        System.out.println("                      ");
                        System.out.println("Enter your choice");
                        choice = sc.nextInt();
                        switch (choice) {
                                case 1:
                                        System.out.println("You have entered as a Restaurant Owner");
                                        System.out.println("Choose your id associated");
                                        ArrayList<Restaurant> restaurant = zotato_records.getRestaurant();
                                        for (int i = 0; i < restaurant.size(); i++) {
                                                Restaurant re = restaurant.get(i);
                                                System.out.println("  " + re.getiid() + "  " + re.getresname() + "(" + re.get_type() + ")");
                                        }
                                        System.out.println("Enter your choice");
                                        int owner_choice = sc.nextInt();

                                        for (Restaurant re : zotato_records.getRestaurant()) {
                                                if (re.getiid() == owner_choice) {
                                                        //stay in this menu while exit is not pressed
                                                        int Rownerchoice = 0;
                                                        while (Rownerchoice != 5) {
                                                                System.out.println("\t\t Welcome " + re.getownwername() + "  to the owner's portal");
                                                                System.out.println("\t OWNER's  MENU of ZOTATO\t");
                                                                System.out.println("\t 1. Add Item \t");
                                                                System.out.println("\t 2. Edit Item \t");
                                                                System.out.println("\t 3. Print Reward \t");
                                                                System.out.println("\t 4. Discount on bill values \t");
                                                                System.out.println("\t 5. Exit \t");
                                                                System.out.println("Enter your choice");
                                                                Rownerchoice = sc.nextInt();
                                                                if (Rownerchoice == 1) {
                                                                        System.out.println("You are trying to add product");
                                                                        re.Add(zotato);
                                                                } else if (Rownerchoice == 2) {
                                                                        System.out.println("You are trying to edit product details");
                                                                        re.Edit(zotato);
                                                                } else if (Rownerchoice == 3) {
                                                                        System.out.println("You are trying print rewards");
                                                                        re.Print(zotato);
                                                                } else if (Rownerchoice == 4) {//left
                                                                        System.out.println("You are trying to add discounts on bill value");
                                                                        re.Discount(zotato);
                                                                }

                                                        }


                                                }
                                        }
                                        break;
                                case 2:
                                        System.out.println("You have entered as a Customer of Zotato");
                                        int token;
                                        System.out.println("Choose your id associated");
                                        for (Customer cs : zotato_records.getCustomer()) {
                                                System.out.println(" " + cs.getcid() + "  " + cs.getcname() + "(" + cs.get_type() + ")");
                                        }
                                        System.out.println("Enter your choice");
                                        token = sc.nextInt();
                                        for (Customer cs : zotato_records.getCustomer()) {
                                                if (cs.getcid() == token) {
                                                        int Cchoice = 0;
                                                        while (Cchoice != 5) {
                                                                System.out.println("\t\t Welcome " + cs.getcname() + "  to the cutomer's portal");
                                                                System.out.println("\t CUSTOMER's  MENU of ZOTATO\t");
                                                                System.out.println("\t 1. Select Restaurant ");
                                                                System.out.println("\t 2. Checkout Cart\t");
                                                                System.out.println("\t 3. Reward won \t");
                                                                System.out.println("\t 4. Print the recent orders \t");
                                                                System.out.println("\t 5. Exit \t");
                                                                System.out.println("Enter your choice");
                                                                Cchoice = sc.nextInt();
                                                                switch (Cchoice)
                                                                {
                                                                        case 1:
                                                                                System.out.println("Welcome!! Please have a look at our restaurants");
                                                                                cs.Restaurant_look(zotato);
                                                                                break;
                                                                        case 2:
                                                                                System.out.println("The total checkout of the orders :");
                                                                                cs.C_checkout(cs.getCustCart(), zotato);
                                                                                break;
                                                                        case 3:
                                                                                System.out.println("The total rewards won by the customer :");
                                                                                cs.C_reward();
                                                                                for( Customer cus : Customer.getCustomer())
                                                                                {
                                                                                        System.out.println("The: "+cus.getcid() + "  "+cus.getcname());
                                                                                }
                                                                                int code_id = sc.nextInt();
                                                                                for(Customer cus : Customer.getCustomer() )
                                                                                {
                                                                                        if(code_id==cus.getcid())
                                                                                        {
                                                                                                System.out.println( cus.getrewards());
                                                                                        }
                                                                                }
                                                                                break;
                                                                        case 4:
                                                                                System.out.println("Print the last 10 order details of our customer : "+cs.getcname());
                                                                                ArrayList<Orders> t = cs.get_Orders();
                                                                                for( Orders os : t )
                                                                                {
                                                                                        System.out.println(" Purchased "+os.getitem().getI_Name());
                                                                                        System.out.println("  quantity "+os.getquantity());
                                                                                        System.out.println(" for indian currency(Rs)"+os.getprice());

                                                                                }
                                                                                break;

                                                                }
                                                        }

                                                }

                                        }
                                        break;
                                case 3:
                                        System.out.println("YOu are going to view the details of the users of this application of ZOTATO");
                                        System.out.println("Zotato_Owner-z   && Zotato_Customer-c");
                                        char code = sc.next().charAt(0);


                                        if(code=='z')
                                        {
                                                System.out.println("\t Choose From The Merchants Below");
                                                for(Restaurant re : Restaurant.getRestaurant())
                                                {
                                                        System.out.println("The: "+re.getiid()+"  "+re.getresname());
                                                }
                                                int code_id = sc.nextInt();
                                                for(Restaurant r : Restaurant.getRestaurant())
                                                {
                                                        if(r.getiid()==code_id)
                                                        {
                                                                r.show_O_C_details();
                                                        }
                                                }
                                        }
                                        else if(code=='c')
                                        {
                                                ArrayList<Customer> customer = Customer.getCustomer();
                                                for (Customer cus : customer) {
                                                        System.out.println("The: " + cus.getcid() + "  " + cus.getcname());
                                                }
                                                int code_id = sc.nextInt();
                                                for(Customer cus : Customer.getCustomer() )
                                                {
                                                        if(code_id==cus.getcid())
                                                        {
                                                                cus.show_O_C_details();
                                                        }
                                                }
                                        }
                                        break;
                                case 4:
                                        System.out.println("You are in the accounts of the Zotato Food Service");
                                        System.out.println("Total Company Balance : "+zotato_records. getnet_earned());
                                        break;
                        }
                }
        }
}
