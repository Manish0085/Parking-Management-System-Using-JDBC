package org.example;

import java.util.Scanner;

public class Operations {

    static Scanner sc = new Scanner(System.in);
    static DBConnection connection = new DBConnection();
    public static void operation(){
        System.out.println("Press 1 to get vehicle registered for parking\nPress 2 to get vehicle Parked Out\nPress 3 for checking Parking Charge\nPress 4 to get the information of vehicle\nPress 5 to exit");
        int type = sc.nextInt();
        switch (type){
            case 1:
                System.out.println("*********************************************************");
                System.out.println();
                connection.parkVehicle();
                System.out.println();
                System.out.println("*********************************************************");
                System.out.println();
                System.out.print("Press 1 :: Main Page :: ");
                if (sc.nextInt() == 1)
                    operation();
                break;
            case 2:
                System.out.println("*********************************************************");
                System.out.println();
                connection.parkOutVehicle();
                System.out.println();
                System.out.println("*********************************************************");
                System.out.println();
                System.out.print("Press 1 :: Main Page :: ");
                if (sc.nextInt() == 1)

                    operation();
                break;

            case 3:
                System.out.println("**********************************************************");
                System.out.println();
                connection.checkParkingChargeForVehicle();
                System.out.println();
                System.out.println("**********************************************************");
                System.out.println();
                System.out.print("Press 1 :: Main Page :: ");
                if (sc.nextInt() == 1)

                    operation();

                break;
            case 4:
                System.out.println("*********************************************************");
                System.out.println();
                connection.getVehicleInfo();
                System.out.println();
                System.out.println("*********************************************************");
                System.out.println();
                System.out.print("Press 1 :: Main Page :: ");
                if (sc.nextInt() == 1)
                    operation();
                break;
            case 5:
                System.exit(0);
            default:
                System.out.println("               ///////////////////////////////////////////////////////");
                System.out.println();
                System.out.println("                               Unexpected input symbol");
                System.out.println();
                System.out.println("                //////////////////////////////////////////////////////");
                System.out.println();
                operation();
                break;
        }
    }
}
