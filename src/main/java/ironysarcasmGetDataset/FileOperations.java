package ironysarcasmGetDataset;

import javafx.util.Pair;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileOperations {

    public static List<String> getFile(String path) {
        BufferedReader reader;
        List<String> twitterIdsList = new LinkedList<>();
        try {
            reader = new BufferedReader(new FileReader(
                    path));
            String line = reader.readLine();
            int count = 0;
            while (line != null) {
                String[] slittedRow = line.split("\\t");
                String twitterId = slittedRow[3];
                twitterIdsList.add(twitterId);

                line = reader.readLine();
                count++;
            }
            System.out.println("XXXXXXXXXXXXXx");
            System.out.println("count:  " + count);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return twitterIdsList;
    }

    public static void saveToFile(List<Pair<String, String>> allPostsInFile, String filePrefix) throws IOException {
//        String fileName = filePrefix + "___" + java.time.LocalDateTime.now() + ".txt";
        String fileName = "preprocessed____" + filePrefix + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (Pair<String, String> pair : allPostsInFile) {
            writer.write(pair.getKey() + "\t" + pair.getValue() + "\n");
        }
        writer.close();
        System.out.println(java.time.LocalDateTime.now());
    }

    public static void saveToFileLong(List<Pair<Long, String>> allPostsInFile, String filePrefix) throws IOException {
//        String fileName = filePrefix + java.time.LocalDateTime.now() + ".txt";
        String fileName = "preprocessed____" + filePrefix + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (Pair<Long, String> pair : allPostsInFile) {
            writer.write(pair.getKey() + "\t" + pair.getValue() + "\n");
        }
        writer.close();
        System.out.println(java.time.LocalDateTime.now());
    }
}
