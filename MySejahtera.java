//package com.company;	//can add it in for IntelliJ 

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class IncomingPassenger {       //Parent class
    Scanner in = new Scanner(System.in);    //to get user input
    boolean input = false, choice = false;
    String fname, lname, country, iso, strDate, dateQuarantine, zone;
    Date date;
    String[] redZone = {"USA","IND","BRA","RUS","GBR"}; //countries that fall in RED Zone using ISO codes.
    String[] yellowZone = {"FRA","ESP","ITA","TUR","DEU"};  //countries that fall in YELLOW Zone using ISO codes.
    SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");   //formatting date to be represent in a certain format.
    Calendar c = Calendar.getInstance();

    public void details(){  //method to get user's first-name and last-name
        do {
            input = false;
            System.out.println("Please Enter your First Name: ");
            fname = in.nextLine();
            if (!fname.matches("^[a-zA-Z0-9_]*$") || fname.matches(".*\\d+.*")) {
                //using regex check for symbols and numbers for first name input. ONLY allows characters
                System.err.println("\tERROR! Please Enter Characters Only!");   //error message when user inputs wrong character
            } else
                input = true;

        } while (!input);

        do {
            input = false;
            System.out.println("Please Enter your Last Name: ");
            lname = in.nextLine();
            if (!lname.matches("^[a-zA-Z0-9_]*$") || lname.matches(".*\\d+.*")) {
                //using regex check for symbols and numbers for last name input. ONLY allows characters
                System.err.println("\tERROR! Please Enter Characters Only!");   //error message when user inputs incorrect value
            } else
                input = true;

        } while (!input);

        System.out.println("Hi," + fname + " " + lname + "!"); //printing out user's name
    }//end of details method

    public void departureCountry(){     //method to get the country user is departing from
        do {
            input=false;
            System.out.println("Which Country are you flying from?");
            country = in.nextLine();
            if(!country.matches("^[a-zA-Z0-9 ]*$") || country.matches(".*\\d+.*"))
            {	//using regex check for symbols and numbers for country input. ONLY allows characters and space character
                System.err.println("\tERROR! Please Enter Characters Only!");
            }
            else if(country.equalsIgnoreCase("Malaysia"))   //error message for when user enters malaysia
                System.err.println("Please state the country you are DEPARTING FROM to arrive at Malaysia!");
            else {
                // Locale.getISOCountries() will return a list of all 2-letter country codes defined in ISO 3166.
                String[] locales = Locale.getISOCountries();

                for (String countryCode : locales) {

                    Locale objCountry = new Locale("", countryCode);

                    //allows the country name, 2 letter country code or country ISO code (3-letters) to be entered.
                    if((country.equalsIgnoreCase(objCountry.getDisplayCountry())) || (country.equalsIgnoreCase(objCountry.getCountry()))
                            || (country.equalsIgnoreCase(objCountry.getISO3Country())))
                    {
                        System.out.println("Country Found!");
                        country = objCountry.getDisplayCountry();   //stores country name properly in country variable
                        iso = objCountry.getISO3Country();      //stores country's iso code
                        choice=true;
                        input=true;
                        break;
                    }

                }//end of for loop
                if(!choice)     //error message for when user inputs an Invalid country name.
                    System.err.println("\tSorry, No such Country Exists! Please try again");

            }//end of else

        }while(!input);   //while loop terminates when input is not equal to false, meaning data has been entered correctly.
        System.out.println("\nGreat "+fname+", you will be flying from "+country+" to Malaysia!");
    }//end of country of departure method

    public void arrivalDate(){  //method to get arrival date of user
        do{
            input=false;
            System.out.println("Enter date of arrival at Malaysia from "+country+" (dd/MM/yyyy): ");
            strDate = in.nextLine();
            if (!strDate.trim().equals("")){
                //if not empty string entered by user, run code below
                sdfrmt.setLenient(false);   //used to specify if date format should be lenient or not.
                //set to false. Meaning strict parsing, inputs MUST match the object format.
                try
                {
                    date = sdfrmt.parse(strDate);   //parsing from String to Date datatype
                    input=true;
                }
                /* Date format is invalid */
                catch (ParseException e)    //exception handling
                {
                    System.err.println(strDate+" is Invalid Date format! Please enter in the format dd/MM/yyyy");
                }
            }
        }while(!input); //end of while loop. terminates when input is true.

    }//end of arrivalDate method

    public void displayDetails(){   //method to display all the details and calculate the quarantine period
        input=false;
        for(int i=0;i<5;i++){
            if(iso.equalsIgnoreCase(redZone[i])){ //if user's country's iso code matches to any of the iso codes in redZone array
                zone = "RED ZONE";
                input=true;
                break;
            }
            else if(iso.equalsIgnoreCase(yellowZone[i])){   //else if it matches to any of the iso codes in yellowZone array
                zone = "YELLOW ZONE";
                input=true;
                break;
            }
        }//end of for loop
        if(!input){         //if neither in redZone nor yellowZone array, means part of "Green Zone"
            zone="GREEN ZONE";
        }

        //printing out customer details
        System.out.println("\n----------CUSTOMER DETAILS----------");
        System.out.println("Name: "+fname+" "+lname);
        System.out.println("Country Arriving From: "+country+" ("+iso+")");
        System.out.println("Country Falls in: "+zone);
        System.out.println("Arrival Date at Malaysia: "+sdfrmt.format(date));

        //setting date to the given date entered by user. The arrival date
        c.setTime(date);

        if(zone.equals("RED ZONE")) {   //if Red Zone, 14 days of quarantine
            System.out.println("Number of Days to Quarantine: 14 days");
            c.add(Calendar.DAY_OF_MONTH, 14);   //Date after adding 14 days to arrival date
        }
        else if(zone.equals("YELLOW ZONE")){    //else if Yellow Zone, 10 days of quarantine
            System.out.println("Number of Days to Quarantine: 10 days");
            c.add(Calendar.DAY_OF_MONTH, 10);   ////Date after adding 10 days to arrival date
        }
        else if(zone.equals("GREEN ZONE")){     //else if Green Zone, 7 days of quarantine
            System.out.println("Number of Days to Quarantine: 7 days");
            c.add(Calendar.DAY_OF_MONTH, 7);    ////Date after adding 7 days to arrival date
        }
        dateQuarantine = sdfrmt.format(c.getTime());    //The New Date after addition of Days
        System.out.println("Quarantine Start Date "+sdfrmt.format(date));   //quarantine begin when user lands in Malaysia
        System.out.println("Quarantine End Date: "+(dateQuarantine)); //Quarantine end date developed depending on number of quarantine days
        System.out.println("-------------------------------------");
    }//end of displaying customer method

}//end of class

public class MySejahtera extends IncomingPassenger{
    public static void main(String[] args){
        boolean loop=false;
        String option,place=null;
        int num=0, rating=0;
        Random random = new Random();

        System.out.println("*************** Welcome to MySejahtera App ***************");

        MySejahtera obj = new MySejahtera();  //creating object to access methods and variables in the parent class. Inheritance

        obj.details();  //to get user details first
        obj.departureCountry(); //to get country user is flying from
        obj.arrivalDate();  //to get arrival date at malaysia
        obj.displayDetails();   //to display customer details and show quarantine period

        do{
            loop=false;
            System.out.println("\nWould you like to travel domestically within Malaysia? Enter y/n: ");
            option=obj.in.nextLine();
            if (!option.matches("^[a-zA-Z0-9_]*$") || option.matches(".*\\d+.*"))
            {
                //using regex check for symbols and numbers for first name input. ONLY allows characters
                System.err.println("\tERROR! Please Enter characters ONLY!");
            }
            if(option.equalsIgnoreCase("y") || option.equalsIgnoreCase("n"))
                loop = true;
            else
                System.err.println("\tERROR! Please Enter 'y' or 'n' ONLY!");
        }while(!loop);

        if(option.equalsIgnoreCase("y")){   //user wants to travel domestically

            System.out.println("\n\t\t\t Welcome "+ obj.fname+" "+obj.lname+" to Malaysia!");
            System.out.println("\n\t\t\t Places to Visit in Malaysia ");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("ID | Place \t\t\t\t | Covid Spread Rating");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("1. | Lakegarden Park\t\t\t | 1");
            System.out.println("2. | Kajang Hospital \t\t\t | 2");
            System.out.println("3. | Secret Recipe \t\t\t | 3");
            System.out.println("4. | KLCC \t\t\t\t | 4");
            System.out.println("5. | Tesco Semenyih  \t\t\t | 4");
            System.out.println("6. | GSC Cinemas \t\t\t | 5");
            System.out.println("7. | Others \t\t\t\t | 2");
            System.out.println("------------------------------------------------------------------------------");

            do {
                loop=false;
                try
                {
                    System.out.println("Please Select the Place you wish to visit by selecting any number from 1-7: ");
                    option = obj.in.nextLine();
                    num=Integer.parseInt(option);
                    if(num>=1 && num<=7)
                    {
                        loop=true;
                    }
                    else
                    {
                        System.err.println("Enter a Number within 1-7 ONLY!");
                    }
                }
                catch(Exception e)  //exception handling when does not enter an integer.
                {
                    System.err.println("Please Enter Numbers ONLY!");
                }
            }while(!loop);

            switch(num){
                case 1:
                    rating=1;
                    place="Lakegarden Park";
                    break;
                case 2:
                    rating=2;
                    place="Kajang Hospital";
                    break;
                case 3:
                    rating=3;
                    place="Secret Recipe";
                    break;
                case 4:
                    rating=4;
                    place="KLCC";
                    break;
                case 5:
                    rating=4;
                    place="Tesco Semenyih";
                    break;
                case 6:
                    rating=5;
                    place="GSC Cinemas";
                    break;
                case 7:
                    do{
                        loop=false;
                        System.out.println("Where would you like to visit?");
                        place = obj.in.nextLine();
                        if (!place.matches("^[a-zA-Z0-9 ]*$") || place.matches(".*\\d+.*")) {
                            //using regex check for symbols and numbers for first name input. ONLY allows characters and space
                            System.err.println("\tERROR! Please Enter Characters Only!");
                        }
                        else if(place.equalsIgnoreCase("Lakegarden Park") || place.equalsIgnoreCase("Kajang Hospital")
                                || place.equalsIgnoreCase("Secret Recipe") || place.equalsIgnoreCase("KLCC")
                                || place.equalsIgnoreCase("Tesco Semenyih") ||place.equalsIgnoreCase("GSC Cinemas")
                                || place.equalsIgnoreCase("others"))
                        {
                            //error condition for when user inputs same place as the ones mentioned in the menu.
                            //user must input another different place.
                            System.err.println("\tERROR! This place is NOT accepted. Please choose another place.");
                        }
                        else
                            loop = true;
                    }while(!loop);
                    rating=2;
                    break;
            }//end of switch statement

            System.out.println("---------------------------------------------------------------------------------------------");

            int people;
            if(obj.zone.equals("RED ZONE") && rating>2){
                //if user from Red Zone country and place of visit has a rating>2, NOT ALLOWED
                people = random.nextInt(20)+300;    //creates random number from 300 to 320
                System.out.println("\t\t\t\t Risk: HIGH");
                System.out.println("\n\t\tSorry,you are NOT ALLOWED to visit "+place);
            }
            else if(obj.zone.equals("YELLOW ZONE") && rating>3){
                //if user from Yellow Zone country and place of visit has a rating>3, NOT ALLOWED
                people = random.nextInt(15)+200;    //creates random number from 200 to 215
                System.out.println("\t\t\t\t Risk: HIGH");
                System.out.println("\n\t\tSorry,you are NOT ALLOWED to visit "+place);
            }
            else if(obj.zone.equals("GREEN ZONE") && rating>4){
                //if user from Green Zone country and place of visit has a rating>4, NOT ALLOWED
                people = random.nextInt(10)+100;    //creates random number from 100 to 110
                System.out.println("\t\t\t\t Risk: HIGH");
                System.out.println("\n\t\tSorry, you are NOT ALLOWED to visit "+place);
            }
            else{
                //default case, ALLOWED
                people = random.nextInt(20)+50;  //creates random number from 50 to 70
                System.out.println("\t\t\t\t Risk: LOW");
                System.out.println("\n\t\tGreat! You are ALLOWED to visit "+place);
            }
            System.out.println("\nAs you are from '"+obj.zone+"' and the Probability of Infection Spread Rating is = "+rating);
            System.out.println("As well as, there are currently "+people+" people present at "+place+".");

        }//end of if statement
        System.out.println("\n\n************* Thank You for using MySejahtera App :) *************");

    }//end of main method
}//end of child class MySejahtera
