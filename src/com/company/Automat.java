package com.company;
import java.io.FileReader;
import java.io.*;

import java.util.HashSet;


import static com.company.consts.delim;

public class Automat {
    public Automat(String input_fname, String stream_filename) throws IOException {
        File input_file = new File(input_fname);
        FileReader fr = new FileReader(input_file);
        BufferedReader reader = new BufferedReader(fr);
        String[] data;

        stream_fname = stream_filename;
        stream_reader = new FileReader(new File(stream_filename));

        //first line
        data = check_input_line(reader, 1, input_fname,1);
        a_size = Integer.parseInt(data[0]);
        if(a_size < 1)
            throw new IOException("Not enough value of A length (input alphabet).");

        for(int i = 0; i < s_size; i++)
            A.add(String.valueOf((char)('a' + i)));

        //second line
        data = check_input_line(reader, 1, input_fname, 2);
        s_size = Integer.parseInt(data[0]);
        if(s_size < 1)
            throw new IOException("Not enough value of B length (states).");



        //third line
        data = check_input_line(reader, 1, input_fname,3);
        c_start = Integer.parseInt(data[0]);
        if(!state_exists(c_start))
            throw new IOException("Incorrect data in line 3 (start state). Automat do not have state with this symbol.");

        //fourth line
        String line = reader.readLine();
        if(line == null)
            throw new IOException("New line in " + input_fname + " can`t be read.");
        data = line.split(delim);
        f_size = Integer.parseInt(data[0]);

        if(data.length < f_size + 1)
            throw new IOException("Not enough arguments in line 4 of " + input_fname);
        else if(data.length > f_size + 1)
            throw new IOException("Too much arguments in line 4 of " + input_fname);

        for(int i = 1; i < f_size + 1; i++){
            int tmp = Integer.parseInt(data[i]);
            if(!state_exists(tmp))
                throw new IOException("Argument \"" + data[i] + "\" in line 4 of " + input_fname + " is incorrect (the state does not exists).");
            Final.add(tmp);
        }

        //line 5 and other

        func = new int[a_size][s_size];
        for(int i = 0; i < a_size; i++){
            for(int j = 0; j < s_size; j++)
                func[i][j] = -1;
        }

        int line_num = 5;
        line = reader.readLine();
        while(line != null){
            data = line.split(delim);
            if(data.length < 3)
                throw new IOException("Not enough arguments in line " + line_num + " of " + input_fname);
            else if(data.length > 3)
                throw new IOException("Too much arguments in line " + line_num + " of " + input_fname);

            int x = Integer.parseInt(data[0]);
            int y = data[1].charAt(0) - 'a';
            int z = Integer.parseInt(data[2]);

            if(x < 0 || x > s_size)
                throw new IOException("Argument \"" + x + "\" in line " + line_num + " of " + input_fname + " is incorrect (out of range).");
            if(y < 0 || y > a_size)
                throw new IOException("Argument \"" + y + "\" in line " + line_num + " of " + input_fname + " is incorrect (out of range).");
            if(z < 0 || z > s_size)
                throw new IOException("Argument \"" + z + "\" in line " + line_num + " of " + input_fname + " is incorrect (out of range).");

            line = reader.readLine();
            line_num++;
        }
        fr.close();
    }

    private static String[] check_input_line(BufferedReader reader, int len, String fname, int line_num) throws IOException {
        String line = reader.readLine();
        if(line == null)
            throw new IOException("New line in " + fname + " can`t be read. File do not contains enough data.");
        String[] data = line.split(delim);
        if(data.length < len)
            throw new IOException("Not enough arguments in line " + line_num + " of " + fname);
        else if(data.length > len)
            throw new IOException("Too much arguments in line " + line_num + " of " + fname);
        return data;
    }

    private boolean state_exists(int st){
        return c_start >= 0 && c_start < s_size;
    }

    private void print_to_file(String fname) throws IOException{
        //FireWriter myWriter
    }



    private String stream_fname;
    private int a_size = 0;
    private int s_size = 0;
    private HashSet<String> A = new HashSet<>();
    private int c_start = -1;
    int f_size = 0;
    HashSet<Integer> Final = new HashSet<>();
    int[][] func;

    FileReader stream_reader;

}

