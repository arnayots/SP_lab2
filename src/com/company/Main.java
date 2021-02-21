package com.company;
import java.io.*;
import java.util.HashSet;

public class Main {

    static String input_filename = "input.txt";
    static String output_filename = "output.txt";
    static String delim = "\\s";

    private static String[] check_input_line(BufferedReader reader, int len, int line_num) throws IOException {
        String line = reader.readLine();
        if(line == null)
            throw new IOException("New line in " + input_filename + " can`t be read.");
        String[] data = line.split(delim);
        if(data.length < len)
            throw new IOException("Not enough arguments in line " + line_num + " of " + input_filename);
        else if(data.length > len)
            throw new IOException("Too much arguments in line " + line_num + " of " + input_filename);
        return data;
    }

    public static void main(String[] args) {
        try{
            File input_file = new File(input_filename);
            FileReader fr = new FileReader(input_file);
            BufferedReader reader = new BufferedReader(fr);
            String[] data;

            //first line
            data = check_input_line(reader, 1, 1);
            int a_size = Integer.parseInt(data[0]);
            if(a_size < 1)
                throw new IOException("Not enough value of A length (input alphabet).");

            //second line
            data = check_input_line(reader, 1, 2);
            int s_size = Integer.parseInt(data[0]);
            if(s_size < 1)
                throw new IOException("Not enough value of B length (states).");

            HashSet<String> S = new HashSet<>();
            for(int i = 0; i < s_size; i++)
                S.add(String.valueOf('a' + i));

            //third line
            data = check_input_line(reader, 1, 3);
            String c_start = data[0];
            if(c_start.length() != 1 || !(S.contains(c_start)))
                throw new IOException("Incorrect data in line 3 (start state).");

            //fourth line
            String line = reader.readLine();
            if(line == null)
                throw new IOException("New line in " + input_filename + " can`t be read.");
            data = line.split(delim);
            int f_size = Integer.parseInt(data[0]);

            if(data.length < f_size + 1)
                throw new IOException("Not enough arguments in line 4 of " + input_filename);
            else if(data.length > f_size + 1)
                throw new IOException("Too much arguments in line 4 of " + input_filename);

            HashSet<String> Final = new HashSet<>();
            for(int i = 1; i < f_size + 1; i++){
                if(data[i].length() != 1)
                    throw new IOException("Argument \"" + data[i] + "\" in line 4 of " + input_filename + " is incorrect (invalid format of the state).");
                if(!S.contains(data[i]))
                    throw new IOException("Argument \"" + data[i] + "\" in line 4 of " + input_filename + " is incorrect (the state does not exists).");
                Final.add(data[i]);
            }

            //line 5 and other

            int[][] func = new int[a_size][s_size];
            for(int i = 0; i < a_size; i++){
                for(int j = 0; j < s_size; j++)
                    func[i][j] = -1;
            }

            int line_num = 5;
            line = reader.readLine();
            while(line != null){
                data = line.split(delim);
                if(data.length < 3)
                    throw new IOException("Not enough arguments in line " + line_num + " of " + input_filename);
                else if(data.length > 3)
                    throw new IOException("Too much arguments in line " + line_num + " of " + input_filename);

                int x = Integer.parseInt(data[0]);
                int y = data[1].charAt(0) - 'a';
                int z = Integer.parseInt(data[2]);

                if(x < 0 || x > a_size)
                    throw new IOException("Argument \"" + x + "\" in line " + line_num + " of " + input_filename + " is incorrect (out of range).");
                if(y < 0 || y > s_size)
                    throw new IOException("Argument \"" + y + "\" in line " + line_num + " of " + input_filename + " is incorrect (out of range).");
                if(z < 0 || z > a_size)
                    throw new IOException("Argument \"" + z + "\" in line " + line_num + " of " + input_filename + " is incorrect (out of range).");

                line = reader.readLine();
                line_num++;
            }

            


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
