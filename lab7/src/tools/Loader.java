package tools;

import data.LabworksStorage;
import data.Coordinates;
import data.Difficulty;
import data.Discipline;
import data.LabWork;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Scanner;

public class Loader {

    private String name;
    private Coordinates coordinates; //Поле не может быть null
    private Float minimalPoint; //Поле не может быть null, Значение поля должно быть больше 0
    private long maximumPoint = 0; //Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле может быть null
    private Discipline discipline; //Поле может быть null

    public void checkMin(Scanner sc, long min){
        checkMax = true;
        checker = min;
        search(sc);
    }
    private long checker = 0;
    private boolean checkMax = false;

    public LabWork search(Scanner sc){
        LabWork lab = new LabWork();
        for (Field field: lab.getFields()){

            if (field.getType().toString().contains("Float")){
                System.out.println("\nPlease enter a float number as a labwork minimal point. Example: 2.95");
                System.out.println("Number must be in (" + -Float.MAX_VALUE + " : " + Float.MAX_VALUE + "], please use '.' as separator for float");
                int i = 0;
                while (i==0){
                    System.out.print(" >> ");
                    try{
                        String line = sc.nextLine();
                        minimalPoint = Float.parseFloat(line);
                        if (minimalPoint <= 0) throw new NumberFormatException();
                        i = 1;
                    }catch (NumberFormatException e){
                        System.out.println("Input not correct, number must be a float in (" + -Float.MAX_VALUE + " : " + Float.MAX_VALUE + "]\n");}
                }
            }
            if (field.getType().toString().contains("long")){
                System.out.println("\nPlease enter a long number as a labwork maximum point. Example: 29000000000");
                System.out.println("Number must be in (" + -Long.MAX_VALUE + " : " + Long.MAX_VALUE + "]");
                int i = 0;
                while (i==0){
                    System.out.print(" >> ");
                    try{
                        String line = sc.nextLine();
                        maximumPoint = Long.parseLong(line);
                        if (maximumPoint <= minimalPoint) throw new IOException();
                        i = 1;
                    }catch (NumberFormatException e){
                        System.out.println("Input not correct, number must be in (" + -Long.MAX_VALUE + " : " + Long.MAX_VALUE + "]\n");
                    }catch (IOException e){
                        System.out.println("Input not corect, maximum point must be greater than Minimum point, " + minimalPoint);
                    }
                }
            }

            if (field.getType().toString().contains("String")){
                System.out.println("\nPlease enter a String name. Example: Labwork_666_Sicko");

                int i = 0;
                while (i==0){
                    System.out.print(" >> ");
                    try{
                        String line = sc.nextLine();
                        name = line.trim();
                        if (name.equals("")){
                            throw new NumberFormatException();
                        }
                        i = 1;
                    }catch (NumberFormatException e){ System.out.println("Field not correct, name cannot be null, please enter data again."); }
                }
            }

            if (field.getType().toString().contains("Coordinates")){
                System.out.println("\nPlease enter Coordinates. Example: 333:88.0 \nFirst coordinate must be integer, in [" + (-Integer.MAX_VALUE - 1) + " : " + Integer.MAX_VALUE + "]\n" +
                        "second coordinate must be a float, (" + -Float.MAX_VALUE + " : " + Float.MAX_VALUE + "], please use '.' as separator for float");

                int i = 0;
                while (i==0){
                    System.out.print(" >> ");
                    try{
                        String line = sc.nextLine();
                        coordinates = new Coordinates(line.trim());
                        i = 1;
                    }catch (ArrayIndexOutOfBoundsException | NumberFormatException e){ System.out.println("Input not correct, please enter an integer and a float, separated by :"); }
                }
            }

            if (field.getType().toString().contains("Difficulty")){
                System.out.println("\nPlease enter Difficulty. Example: Choose between: " +
                        "\nVERY_EASY," +
                        "\nVERY_HARD," +
                        "\nIMPOSSIBLE");

                int i = 0;
                while (i==0){
                    System.out.print(" >> ");
                    String line = sc.nextLine();

                    Difficulty diff = null;
                    if (line.toUpperCase().contains("VERY_EASY")){ diff = Difficulty.VERY_EASY; }
                    if (line.toUpperCase().contains("VERY_HARD")){ diff = Difficulty.VERY_HARD; }
                    if (line.toUpperCase().contains("IMPOSSIBLE")){ diff = Difficulty.IMPOSSIBLE; }
                    if (line.trim().equals("") || diff != null){
                        difficulty = diff;
                        i = 1;
                    }else{ System.out.println("Field not correct, please choose between variants listed or enter null."); }
                }
            }
            if (field.getType().toString().contains("Discipline")){
                System.out.println("\nPlease enter Discipline: Discipline_name, lecture_hours, practice_hours, self_study_hours. Example: Programming, 100, 50, 20");
                int i = 0;
                while (i==0){
                    System.out.print(" >> ");
                    try{
                        String line = sc.nextLine();
                        if (line.trim().equals("")){
                            discipline = null;
                        }
                        String[] data = line.trim().split(",");
                        String name = data[0];
                        int practiceHours = Integer.parseInt(data[1].trim());
                        int lectureHours = Integer.parseInt(data[2].trim());
                        long selfStudyHours = Long.parseLong(data[3].trim());
                        discipline = new Discipline(name, practiceHours, lectureHours, selfStudyHours);
                        i = 1;

                    }catch (ArrayIndexOutOfBoundsException | NumberFormatException e){
                        System.out.println("Please provide all the elements correctly: String, integer, integer, integer");
                    }
                }
            }
        }
        return new LabWork(name, coordinates, minimalPoint, maximumPoint, difficulty,discipline);
    }
    public void load(LabWork lab){
        LabworksStorage.put(lab);
    }
}