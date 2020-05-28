package tools;

import data.LabworksStorage;
import data.LabWork;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class Java2DB {

    private File f = new File("data.xml");
    public void writeXML(){
        //FileWriter fw = null;
        try {



            //PreparedStatement statement = connection.prepareStatement("SELECT username");
            /*fw = new FileWriter(f);
            fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            fw.write("<body>\n");
            for (LabWork labWork: LabworksStorage.getData()){
                fw.write("\t<el>\n");

                fw.write("\t\t<name>" + labWork.getName() + "</name>\n");
                fw.write("\t\t<coordinates>" + labWork.getCoordinates().toString() + "</coordinates>\n");
                fw.write("\t\t<discipline>" + labWork.getDiscipline() + "</discipline>\n");
                fw.write("\t\t<difficulty>" + labWork.getDifficulty() + "</difficulty>\n");
                fw.write("\t\t<minimalPoint>" + labWork.getMinPoint() + "</minimalPoint>\n");
                fw.write("\t\t<maximumPoint>" + labWork.getMaxPoint() + "</maximumPoint>\n");

                fw.write("\t</el>\n");
            }
            fw.write("</body>\n");*/
        } catch (Exception e) {
            //
        }

        //finally {
        //    try {
        //        if (fw != null) {
        //            fw.flush();
        //            fw.close();
        //        }
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }
        //}
    }
}