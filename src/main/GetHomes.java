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
               Boolean hasParsedPlayerName = false;
               Boolean hasHome = false;

               //Scrape Homes and Player's name
               while (stdin.hasNextLine()) {
                  String data = stdin.nextLine();

                  if(data.equals("    homes: {")){ //Checks if Scanner has reached the homes yet, if there are homes.
                     hasHome = true;
                     //add first home
                     data = stdin.nextLine().trim().replace("{", ""); //clean first home name
                     scrappedData.add("\t" + data);

                     while (stdin.hasNextLine() && !(data.equals("    loc: {"))) {
                        //adds rest of home names
                        if(data.equals("        },")){
                           data = stdin.nextLine().trim().replace("{", ""); //clean home name
                           scrappedData.add("\t" + data);
                        }
                        else{
                           data = stdin.nextLine();
                        }

                        //adds dimension
                        if(data.contains("dim: \"")){
                           data = data.trim().replace(",", ""); //clean dimension name
                           scrappedData.add("\t\t" + data);
                        }

                        //adds position
                        else if(data.equals("                pos: {")){
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
                           scrappedData.add("\t\t\t" + pos);
                        }
                     }
                  }
                  else if(data.contains("name:") && hasHome = true && hasParsedPlayerName == false){ //records player's name
                     data = data.trim().replace("name: \"", "").replace("\",", ""); //clean player name
                     scrappedData.add(0, data);
                     hasParsedPlayerName = true;
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