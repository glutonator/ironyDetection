package ironysarcasmGetDataset;


import javafx.util.Pair;
import twitter4j.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class MainGetTwitterCorpus {

    final static String PATH = "/home/filip/Documents/repo/ironyDetection/src/main/resources/";
    final static String SUB_DIR = "irony-sarcasm-ling2016/";
    final static String FILE_NAME = "regular";
    final static String EXTENSION_OF_FILE = ".csv";

    final static String PATH_TO_FILE = PATH + SUB_DIR + FILE_NAME + EXTENSION_OF_FILE;


    public static void main(String... args) throws IOException {

//        manFunc();
//
//        debug();

        manFuncInBatches();


    }

    public static void manFuncInBatches() throws IOException {

        Twitter twitter = AccessTwitter.getTwitter();
        List<String> twitterIdsList = FileOperations.getFile(PATH_TO_FILE);

        List<Pair<Long, String>> twitterMessageById = getAllPostsInFile(twitter, twitterIdsList);

        FileOperations.saveToFileLong(twitterMessageById, FILE_NAME + EXTENSION_OF_FILE);
    }


    public static void debug() {
//        String tmp = "627220119006130176";
        String tmp = "627237147968471040";
//        String tmp = "627303212215889920";

        Twitter twitter = AccessTwitter.getTwitter();
        AccessTwitter.getTwitterMessageById(twitter, tmp, true);
    }


    public static List<Pair<Long, String>> getAllPostsInFile(Twitter twitter, List<String> twitterIdsList) {
        Map<Integer, long[]> integerMap = AccessTwitter.parseToBatches(twitterIdsList);
        List<Pair<Long, String>> twitterMessageById = AccessTwitter.getTwitterMessageByMultipleIds(twitter, integerMap, false);
        return twitterMessageById;

    }


}
