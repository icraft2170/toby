package me.hyeonho.toby.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {


    public Integer fileReadTemplate(String filepath, BufferReaderCallback callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            int ret = callback.doSomethingWithReader(br);
            return ret;
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw e;
        }finally {
            if(br != null){
                try {
                    br.close();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                    throw e;
                }
            }
        }
    }




    public  Integer calcSum(String filepath) throws IOException{
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            Integer sum = 0;
            String line = null;
            while ((line = br.readLine()) != null){
                sum += Integer.valueOf(line);
            }
            return sum;
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw e;
        }finally {
            if(br != null){
                try {
                    br.close();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                    throw e;
                }
            }
        }
    }

}
