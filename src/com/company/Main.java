package com.company;
import java.io.*;
import static com.company.consts.*;

public class Main {

    public static void main(String[] args) {
        try{
            Automat aut_A = new Automat(input_filename_A);
            aut_A.print_to_file(output_filename_A);

            Automat aut_B = new Automat(input_filename_B);
            aut_B.print_to_file(output_filename_B);

            boolean res = aut_A.is_equal(aut_B);
            System.out.println(res);

            /*
            aut_A.minimise();
            aut_A.print_to_file("out_debug.txt");
            aut_A.refactor();
            aut_A.print_to_file("out_debug_2.txt");
            */


        }
        catch (NumberFormatException ex){
            System.out.println("Incorrect data: something can`t be converted to int");
            System.out.println(ex.getMessage());
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }


    }
}
