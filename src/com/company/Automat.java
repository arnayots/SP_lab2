package com.company;
import com.sun.source.tree.Tree;

import java.io.FileReader;
import java.io.*;

import java.util.*;


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
        s_start = Integer.parseInt(data[0]);
        if(!good_state(s_start))
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
            if(!good_state(tmp))
                throw new IOException("Argument \"" + data[i] + "\" in line 4 of " + input_fname + " is incorrect (the state does not exists).");
            Final.add(tmp);
        }

        //line 5 and other

        func = new int[s_size][a_size];
        for(int i = 0; i < s_size; i++){
            for(int j = 0; j < a_size; j++)
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

            if(!good_state(x))
                throw new IOException("Argument x \"" + x + "\" in line " + line_num + " of " + input_fname + " is incorrect (out of range).");
            if(!good_char(y))
                throw new IOException("Argument y \"" + y + "\" in line " + line_num + " of " + input_fname + " is incorrect (out of range).");
            if(!good_state(z))
                throw new IOException("Argument z \"" + z + "\" in line " + line_num + " of " + input_fname + " is incorrect (out of range).");

            if(func[x][y] == -1)
                func[x][y] = z;
            else
                throw new IOException("In line " + line_num + " of " + input_fname + " data is incorrect (double definition).");

            line = reader.readLine();
            line_num++;
        }
        fr.close();

        for(int i = 0; i < s_size; i++){
            if(!Final.contains(i)){
                int counter = 0;
                for(int j = 0; j < a_size; j++){
                    if(func[i][j] != -1)
                        counter++;
                }
                if(counter != a_size)
                    throw new IOException("Automat is not determinated. Can`t leave from state "+ i +" with anyone char. j=");
            }
        }
    }

    public void minimise() throws IOException {
        remove_unattainable_states();

        HashMap<Integer, Integer> st_class = new HashMap<>();
        int st_count = 2;
        for(int i = 0; i < s_size; i++)
            st_class.put(i, 0);
        for(int i : Final)
            st_class.replace(i, 1);
        if(Final.isEmpty())
            st_count = 1;

        for(int i = 0; i < s_size; i++)
            System.out.println(i + " " + st_class.get(i));


        System.out.println("---------");

        boolean do_next = true;
        HashMap<Integer, Integer> next = new HashMap<>(st_class);
        while(do_next){
            for(int i = 0; i < st_count; i++){
                TreeSet<Integer> st_in_cl = new TreeSet<>();
                for(int j = 0; j < s_size; j++){
                    if(st_class.get(j) == i)
                        st_in_cl.add(j);
                }
                st_count = check_and_update_next(st_in_cl, i, st_class, st_count, next);
                System.out.println("st_count = " + st_count);
            }

            if(st_class == next)
                do_next = false;
            st_class = next;
        }


        for(int i = 0; i < s_size; i++)
            System.out.println(i + " " + st_class.get(i));


        int[][] new_func = new int[st_count][a_size];
        for(int i = 0; i < st_count; i++){
            for(int j = 0; j < a_size; j++)
                new_func[i][j] = -1;
        }

        for(int i = 0; i < st_count; i++) {
            for (int j = 0; j < a_size; j++) {
                System.out.print(new_func[i][j] + " ");
            }
            System.out.println(" ");
        }

        HashSet<Integer> new_final = new HashSet<>();
        for(int i = 0; i < st_count; i++){
            TreeSet<Integer> st_in_cl = new TreeSet<>();
            for(int j = 0; j < s_size; j++){
                if(st_class.get(j) == i){
                    st_in_cl.add(j);
                    if(Final.contains(j))
                        new_final.add(i);
                }
            }
            for(int j = 0; j < st_count; j++){
                if(func[st_in_cl.first()][j] != -1)
                    new_func[i][j] = st_class.get(func[st_in_cl.first()][j]);
            }

        }

        s_start = st_class.get(s_start);
        Final = new_final;
        f_size = Final.size();
        s_size = st_count;
        func = new_func;


/*
        for(int i = 0; i < st_count; i++){
            HashSet<Integer> states = new HashSet<>();
            for(int j = 0; j < st_count; j++){
                if(st_class.get(i) == st_class.get(j))
                    states.add(j);
            }
            if(states.size() == 1){
                for(int j = 0; j < a_size; j++){
                    if(func[i][j] != -1)
                        new_func[i][j] = st_class.get(func[i][j]);
                }
            } else {
                HashSet<Integer> goes_to = new HashSet<>();
                for(int el : states){
                    if(!Final.contains(el)){
                        goes_to.add
                    }
                }
            }

        }

*/
    }

    private void remove_unattainable_states(){
        HashMap<Integer, Integer> states_cond = new HashMap<>();
        for(int i = 0; i < s_size; i++)
            states_cond.put(i, 0);
        states_cond.replace(s_start, 1);
        // 0 - non visited
        // 1 - visited
        visit_next_states(states_cond, s_start);

        int unvis_count = 0;
        HashSet<Integer> unvis_states = new HashSet<>();
        for(int i = 0; i < s_size; i++){
            if(states_cond.get(i) == 0){
                unvis_states.add(i);
                unvis_count++;
            }
        }

        TreeMap<Integer, Integer> new_num = new TreeMap<>();
        HashMap<Integer, Integer> old_num = new HashMap<>();
        int i_new = 0;
        for(int i_old = 0; i_old < s_size; i_old++){
            if(!unvis_states.contains(i_old)){
                new_num.put(i_new, i_old);
                old_num.put(i_old, i_new);
                i_new++;
            } else
                old_num.put(i_old, -1);
        }

        int[][] new_func = new int[s_size - unvis_count][a_size];
        for(int i = 0; i < s_size - unvis_count; i++){
            if(!unvis_states.contains(i)){
                for(int j = 0; j < a_size; j++){
                    if(func[i][j] != -1)
                        new_func[i][j] = old_num.get(func[i][j]);
                    else
                        new_func[i][j] = -1;
                }
                i_new++;
            }
        }

        func = new_func;
        s_size = s_size - unvis_count;
        Final.removeAll(unvis_states);
        f_size = Final.size();
    }

    private void visit_next_states(HashMap<Integer, Integer> states_cond, int st){
        for(int i = 0; i < a_size; i++){
            if(states_cond.get(i) == 0){
                states_cond.replace(i, 1);
                visit_next_states(states_cond, i);
            }
        }
    }

    private boolean is_zero_eq(int st1, int st2, HashMap<Integer, Integer> st_class){
        for(int i = 0; i < a_size; i++){
            if(st_class.get(func[st1][i]) != st_class.get(func[st2][i]))
                return false;
        }
        return true;
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

    private int check_and_update_next(TreeSet<Integer> st_in_cl, int cl_num, HashMap<Integer, Integer> st_class, int st_count, HashMap<Integer, Integer> next) throws IOException {
        if(st_in_cl.isEmpty())
            return st_count;
        if(st_in_cl.size() == 1){
            next.replace(st_in_cl.first(), cl_num);
        } else {
            int fir = st_in_cl.first();
            TreeSet<Integer> split = new TreeSet<>();
            for(int elem : st_in_cl){
                if(!is_zero_eq(fir, elem, st_class))
                    split.add(elem);
            }
            for(int elem : st_in_cl){
                if(!split.contains(elem))
                    next.replace(elem, cl_num);
            }
            st_count++;
            System.out.println("New st_count = " + st_count);
            st_count = check_and_update_next(split,st_count - 1, st_class, st_count, next);
        }
        return st_count;
    }

    private boolean good_state(int st){
        return st >= 0 && st < s_size;
    }

    private boolean good_char(int st){
        return st >= 0 && st <= a_size;
    }

    public void print_to_file(String fname) throws IOException{
        FileWriter out = new FileWriter(fname);
        out.write(a_size + "\n");
        out.write(s_size + "\n");
        out.write(s_start + "\n");
        out.write(f_size + " ");
        for(int current : Final)
            out.write(current + " ");
        out.write("\n");
        for(int i = 0; i < s_size; i++){
            for(int j = 0; j < a_size; j++){
                if(func[i][j] != -1)
                    out.write(String.valueOf(i) + " " + (char)(j + 'a') + " " + String.valueOf(func[i][j]) + "\n");
            }
        }
        out.close();
    }

    private String stream_fname;
    private int a_size = 0;
    private int s_size = 0;
    private HashSet<String> A = new HashSet<>();
    private int s_start = -1;
    int f_size = 0;
    HashSet<Integer> Final = new HashSet<>();
    int[][] func;

    FileReader stream_reader;

}

