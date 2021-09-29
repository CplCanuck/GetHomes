import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class GetHomes { 
  public static void main(String[] args) {
      try{
         FileWriter myWriteFile = new FileWriter("homes.txt");
         List<String> myFileNames = new ArrayList<String>();

         //get all files from "generated" folder
         File[] myFiles = new File("generated").listFiles();

         //clean files for .snbt
         for(File file : myFiles){
            if(file.isFile() && file.getName().contains(".snbt")){ //filter out everything that isn't a file and isn't a .snbt file
               //add file
               myFileNames.add(file.getName());
            }
         }

         //loop through each .snbt and scrap data
         for(int i = 0; i < myFileNames.size(); i++){
            //print current file name
            System.out.println(myFileNames.get(i));
            try {
               File myReadFile = new File(("generated/" + myFileNames.get(i)));
               Scanner stdin = new Scanner(myReadFile);
               List<String> scrappedData = new ArrayList<String>();

               //Scrape Homes and Player's name
               while (stdin.hasNextLine()) {
                  String data = stdin.nextLine();

                  if(data.equals("    homes: {")){ //Checks if Scanner has reached the homes yet, if there are homes.
                     while (stdin.hasNextLine() && !(data.equals("    loc: {"))) {
                        data = stdin.nextLine();

                        //adds position
                        if(data.equals("                pos: {")){
                           String pos = new String();

                           while(!data.contains("}") && stdin.hasNextLine()){
                              data = stdin.nextLine();
                              //record x
                              if(data.contains("x: ")){
                                 data = data.trim().replace("x: ", "");
                                 int iend = data.indexOf(".");
                                 if(iend != -1){
                                    data = data.substring(0, iend); //cut off anything past ,
                                 }
                                 pos += (data + " ");
                              }
                              //record y
                              else if(data.contains("y: ")){
                                 data = data.trim().replace("y: ", "");
                                 int iend = data.indexOf(".");
                                 if(iend != -1){
                                    data = data.substring(0, iend); //cut off anything past ,
                                 }
                                 pos += (data + " ");
                              }
                              //record z
                              else if(data.contains("z: ")){
                                 data = data.trim().replace("z: ", "");
                                 int iend = data.indexOf(".");
                                 if(iend != -1){
                                    data = data.substring(0, iend); //cut off anything past ,
                                 }
                                 pos += data;
                              }
                           }

                           scrappedData.add(pos);
                        }
                     }
                  }
               }

               //write scraped data to write file
               for(int j = 0; j < scrappedData.size(); j++){
                  myWriteFile.write(scrappedData.get(j) + "\n");
               }
               stdin.close();
            } catch (FileNotFoundException e) {
               System.out.println("An error occurred.");
               e.printStackTrace();
            }
         }
         myWriteFile.close();
      } catch(IOException e){
         e.printStackTrace();
      }
  }
}