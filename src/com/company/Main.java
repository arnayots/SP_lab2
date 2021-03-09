package com.company;
import java.io.*;
import static com.company.consts.*;

public class Main {

    public static void main(String[] args) {
        try{
            Automat aut_A = new Automat(input_filename_A);
            aut_A.print_to_file(output_filename_A);

            aut_A.graphviz("graphviz_A.txt");
            System.out.println("A: " + aut_A.graphlink());

            Automat aut_B = new Automat(input_filename_B);
            aut_B.print_to_file(output_filename_B);
            System.out.println("B: "+aut_B.graphlink());

            boolean res = aut_A.is_equal(aut_B);
            System.out.println(res);
            System.out.println("A: " + aut_A.graphlink());

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
