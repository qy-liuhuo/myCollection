import java.util.*;

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int t;
        for (int j = 0; j < Integer.MAX_VALUE; j++){
            for(int i = 0;i<Integer.MAX_VALUE;i++){
                t = i / 2;
            }
        }

        long mid = System.currentTimeMillis();
        for (int j = 0; j < Integer.MAX_VALUE; j++){
            for(int i = 0;i<Integer.MAX_VALUE;i++){
                t = i >> 1;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(mid-start);
        System.out.println(end-start);


    }
}