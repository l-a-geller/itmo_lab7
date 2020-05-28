package tools;

import data.LabworksStorage;
import data.Coordinates;
import data.Difficulty;
import data.Discipline;
import data.LabWork;

import java.io.*;
import java.util.ArrayList;

public class XMLScanner {
    private ArrayList<String> tokens = new ArrayList<>();
    private ArrayList<String> fieldNames = new ArrayList<>();

    public void scan(File file) throws IOException {

        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        int i;
        boolean checker = false;

        ArrayList<Character> word = new ArrayList<>();
        ArrayList<Character> fieldName = new ArrayList<>();

        while((i = bis.read()) != -1) {
            char c = (char) i;
            StringBuilder sb = new StringBuilder();
            switch (c){
                case '>':
                    checker = true;
                    for (Character character: fieldName){sb.append(character);}
                    String nextToken = sb.toString().replaceAll(",", "").replaceAll("<", "");
                    if (nextToken.equals("/el")){
                        validate();
                        tokens.clear();
                    }
                    nextToken = nextToken.replaceAll("/", "");
                    if (!fieldNames.contains(nextToken)) fieldNames.add(sb.toString().replaceAll(",", "").replaceAll("<", "").replaceAll("/", ""));
                    fieldName.clear();
                    break;
                case '<':
                    checker = false;
                    for (Character character: word){sb.append(character);}
                    String next = sb.toString().replaceAll(",", "").replaceAll(">", "");
                    if(!next.trim().equals("")) tokens.add(next);
                    word.clear();
                    break;
            }
            if (checker){
                word.add(c);
            }else fieldName.add(c);
        }
    }

    private void validate(){
        LabWork lab = new LabWork();

        ArrayList<String> fields = lab.getFieldNames();
        int length = fields.size();

        for (String st: fields){
            if (fieldNames.contains(st)){
                length--;
            }
        }
        if (length == 0){
            loadData();
        }else{
            System.out.println("Not all fields necessary provided.");
        }
    }

    private void loadData(){
        Coordinates c = new Coordinates(tokens.get(1));
        Difficulty dif = Difficulty.valueOf(tokens.get(3));
        Discipline dis = new Discipline(tokens.get(2));
        LabWork lab = new LabWork(tokens.get(0), c, Float.parseFloat(tokens.get(4)), Long.parseLong(tokens.get(5)), dif, dis);
        LabworksStorage.put(lab);
    }
}
