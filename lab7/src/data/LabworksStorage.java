package data;

import data.LabWork;
import tools.DataBase.DataBaseConnector;

import java.io.Serializable;
import java.util.*;

public class LabworksStorage implements Serializable {
    static {
        date = new Date();
    }
    private static Date date;
    private static TreeSet<LabWork> labworks = new TreeSet<>();
    public static String put(LabWork l){
        boolean added = labworks.add(l);
        if (added){ return "Labwork " + l.getName() + " added. Id = " + l.getId(); }
        else { return "Lab already exists."; }
    }

    public static void setData(TreeSet<LabWork> labs) {
        //for (LabWork labWork: labs){
        //    System.out.println(labWork.getName() + "\t" +  put(labWork));
        //}
        labworks = labs;
        //System.out.println("Labs" + labs.size());
        //System.out.println(labworks.size());
        //labs.stream().forEach(w -> labworks.add(w));
    }

    public static TreeSet<LabWork> getSortedByCoordinates(){

        TreeSet<LabWork> res = new TreeSet<>();
        labworks.stream().sorted(new CoordinatesComparator<LabWork>()).forEach(res::add);
        return res;
    }

    public static String printInfo(){
        return "\nCollection type: "+labworks.getClass().getName() + "\nCreation date: " + date + "\nNumber of elements: " + labworks.size();
    }
    public static ArrayList<LabWork> getByMin(float min) {
        ArrayList<LabWork> lab = new ArrayList<>();

        labworks.stream().filter(w -> Float.parseFloat(w.getMinPoint()) == min).forEach(lab::add);
        return lab;
    }
    public static LabWork getMinByCoordinates(){
        return labworks.stream().min(new CoordinatesComparator<LabWork>()).get();
    }
    public static long getMinMaxPoint(){
        Long res = null;
        for (LabWork lab: labworks){
            if (res != null){
                if (lab.getMaximumPointer() < res){
                    res = lab.getMaximumPointer();
                }
            }else { res = lab.getMaximumPointer(); }
        }
        return res;
    }
    public static ArrayList<LabWork> getData(){
        ArrayList<LabWork> arr = new ArrayList<>();

        labworks.stream().forEach(w -> arr.add(w));
        return arr;
    }
    public static void clear(){
        labworks.clear();
    }

    public static String remove(LabWork lab, String login){
        String res = "";
        if (login.equals(lab.getUser())){
            labworks.remove(lab);
            DataBaseConnector.removeLab(lab, login);
            res = "Element with ID: " + lab.getId() + " removed.";
        }
        return res;
    }

    public static LabWork searchById(int id){
        LabWork laboratory =  null;
        Optional<LabWork> lab = labworks.stream().filter(l -> l.checkId(id)).findAny();//filter(lab -> lab.checkId(id)).forEach(lab -> laboratory = lab);\
        try{
            laboratory = lab.get();
        }catch (NoSuchElementException e){
            //
        }
        //System.out.println(laboratory);
        return laboratory;
    }
}