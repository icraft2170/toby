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



    public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            T res = initVal;
            String line = null;
            while ((line = br.readLine()) != null){
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
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
        LineCallback<Integer> sumCallback =
                ((line, value) -> {
                    return value + Integer.valueOf(line);
                });
        return lineReadTemplate(filepath,sumCallback,0);
    }

    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback<Integer> multiplyCallback =
                ((line, value) -> {
                    return value * Integer.valueOf(line);
                });
        return lineReadTemplate(filepath,multiplyCallback,1);
    }

    public String concatenate(String filepath) throws IOException {
        LineCallback<String> multiplyCallback =
                ((line, value) -> {
                    return value + line;
                });
        return lineReadTemplate(filepath,multiplyCallback,"");
    }
}
