package com.company;
import java.io.*;
import static com.company.consts.*;


public class Main {


    public static void main(String[] args) {
        try{
            Automat aut_A = new Automat(input_filename_A, input_filename_stream);
            aut_A.print_to_file(output_filename);
            aut_A.minimise();
            aut_A.print_to_file("out_debug.txt");



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
