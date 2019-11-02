package ironysarcasmGetDataset;


import javafx.util.Pair;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class MainGetTwitterCorpus {

    final static String PATH = "/home/filip/Documents/repo/ironyDetection/src/main/resources/";
    final static String SUB_DIR = "irony-sarcasm-ling2016/";
    final static String FILE_NAME = "irony";
    final static String EXTENSION_OF_FILE = ".csv";

    final static String PATH_TO_FILE = PATH + SUB_DIR + FILE_NAME + EXTENSION_OF_FILE;


    public static void main(String... args) throws IOException {

//        manFunc();
//
//        debug();

        manFuncInBatches();


    }

    public static void manFuncInBatches() throws IOException {
        Map<Integer, long[]> integerMap = parseTOBatches(getFile(PATH_TO_FILE));

        Twitter twitter = getTwitter();

        List<Pair<Long, String>> twitterMessageById = getTwitterMessageByMultipleIds(twitter, integerMap, false);
        saveToFileLong(twitterMessageById, FILE_NAME + EXTENSION_OF_FILE);
    }


    public static void debug() throws IOException {
//        String tmp = "627220119006130176";
        String tmp = "627237147968471040";
//        String tmp = "627303212215889920";

        Twitter twitter = getTwitter();
        getTwitterMessageById(twitter, tmp, true);
    }


    public static void manFunc() throws IOException {
        Twitter twitter = getTwitter();
        List<String> twitterIdsList = getFile(PATH_TO_FILE);
        List<Pair<String, String>> allPostsInFile = getAllPostsInFile(twitter, twitterIdsList);

        saveToFile(allPostsInFile, FILE_NAME);
    }

    public static Map<Integer, long[]> parseTOBatches(List<String> twitterIdsList) {
        Map<Integer, long[]> map = new HashMap<>();
        List<Long> twitterIdsListLong = twitterIdsList.stream().map(Long::parseLong).collect(Collectors.toList());

        List<List<Long>> subSets = Lists.partition(twitterIdsListLong, 100);

        int batchKey = 0;
        for (List<Long> tmp : subSets) {
            long[] tmpPrimativeLong = tmp.stream().mapToLong(l -> l).toArray();
            map.put(batchKey, tmpPrimativeLong);
            batchKey++;
        }

        return map;

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
        String fileName = filePrefix + java.time.LocalDateTime.now() + ".txt";
//        String fileName = "preprocessed____" + filePrefix + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (Pair<Long, String> pair : allPostsInFile) {
            writer.write(pair.getKey() + "\t" + pair.getValue() + "\n");
        }
        writer.close();
        System.out.println(java.time.LocalDateTime.now());
    }

    public static List<Pair<String, String>> getAllPostsInFile(Twitter twitter, List<String> twitterIdsList) {
        List<Pair<String, String>> pairOfPostIdAndText = new LinkedList<>();
//        int i = 0;
        for (String postId : twitterIdsList) {
//            if (i > 50) break;
            String twitterMessageById = getTwitterMessageById(twitter, postId, false);
            pairOfPostIdAndText.add(new Pair<>(postId, twitterMessageById));
//            i++;
        }
        return pairOfPostIdAndText;
    }


    public static List<Pair<Long, String>> getTwitterMessageByMultipleIds(Twitter twitter, Map<Integer, long[]> twitterPostIds, Boolean debug) {
        List<Pair<Long, String>> out = new LinkedList<>();
        try {
//            int bbbbreak = 0;
            for (Map.Entry<Integer, long[]> entry : twitterPostIds.entrySet()) {
                List<Status> uuu = twitter.lookup(entry.getValue());
                List<Pair<Long, String>> sssss = uuu.stream()
                        .map(status -> new Pair<>(status.getId(), preprocessTextMessage(status.getText())))
                        .collect(Collectors.toList());
                out.addAll(sssss);
//                bbbbreak++;
//                if (bbbbreak > 2) break;
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return out;
    }

    public static String preprocessTextMessage(String message) {
        message = message.replace("\n", " ");
        message = message.replace("\t", " ");
        message = message.replace("\r", " ");

        return message;
    }


    public static String getTwitterMessageById(Twitter twitter, String twitterPostId, Boolean debug) {
        Long number = new Long(twitterPostId);
        String postText;
        try {
            Status status = twitter.showStatus(number);
            postText = status.getText();
            postText = preprocessTextMessage(postText);
            if (debug) {
                System.out.println(postText);
            }
        } catch (TwitterException e) {
            e.printStackTrace();
            postText = null;
        }

        return postText;
    }

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


//    public static Twitter getTwitter() {
//        ConfigurationBuilder cb = new ConfigurationBuilder();
//        cb.setDebugEnabled(true)
//                .setOAuthConsumerKey("8yGPXIcLrdf8PUGFGk5Dx6mPC")
//                .setOAuthConsumerSecret("Kq1jbgekbuhvoWueKw9v2SJedD1vyH624VfF2JmfvNDsWcbkPh")
//                .setOAuthAccessToken("1115941392386351104-sHUgkaLkrxyOmMMps8qB81Zj18ClHl")
//                .setOAuthAccessTokenSecret("v2GXmdQ8tuPoxJ1ZjN4YQBGyylzGIIALXfJhaaybSp8Er");
//        TwitterFactory tf = new TwitterFactory(cb.build());
//        Twitter twitter = tf.getInstance();
//        return twitter;
//    }


}
