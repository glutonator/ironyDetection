package ironysarcasmGetDataset;


import javafx.util.Pair;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class MainGetTwitterCorpus {

    final static String PATH = "/home/filip/Documents/repo/ironyDetection/src/main/resources/";
    final static String SUB_DIR = "irony-sarcasm-ling2016/";
    final static String FILE_NAME = "irony.csv";
    final static String PATH_TO_FILE = PATH + SUB_DIR + FILE_NAME;


    public static void main(String... args) throws IOException {

//        manFunc();
//
//        debug();
        parseTOBatches(getFile(PATH_TO_FILE));
    }

    public static void debug() throws IOException {
        String tmp = "623083534907912192";

        Twitter twitter = getTwitter();
//        getTwitterMessageById(twitter, tmp, true);
        getTwitterMessageByMultipleIds(twitter, tmp, true);
    }


    public static void manFunc() throws IOException {
        Twitter twitter = getTwitter();
        List<String> twitterIdsList = getFile(PATH_TO_FILE);
        List<Pair<String, String>> allPostsInFile = getAllPostsInFile(twitter, twitterIdsList);

        saveToFile(allPostsInFile, FILE_NAME);
//        saveToFile(allPostsInFile, "testFile");
    }

    public static void parseTOBatches(List<String> twitterIdsList) {
        Map<Integer, long[]> map = new HashMap<>();
        List<Long> twitterIdsListLong = twitterIdsList.stream().map(Long::parseLong).collect(Collectors.toList());

        List<List<Long>> subSets = Lists.partition(twitterIdsListLong,100);

        int batchKey = 0;
        for(List<Long> tmp : subSets) {
            long[] tmpPrimativeLong = tmp.stream().mapToLong(l -> l).toArray();
            map.put(batchKey,tmpPrimativeLong);
            batchKey++;
        }

        System.out.println(map);

    }

    public static void saveToFile(List<Pair<String, String>> allPostsInFile, String filePrefix) throws IOException {
        String fileName = filePrefix + java.time.LocalDateTime.now() + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (Pair<String, String> pair : allPostsInFile) {
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

    public static String getTwitterMessageByMultipleIds(Twitter twitter, String twitterPostId, Boolean debug) {
        Long number = new Long(twitterPostId);
        String postText;
        try {
//            Status status = twitter.showStatus(number);
            long[] fff = {623054008991772672L, 623055404885372928L, 623056094445871104L};
            List<Status> uuu = twitter.lookup(fff);
            System.out.println(uuu);
//            postText = status.getText();
//            postText = postText.replace("\n", " ");
//            postText = postText.replace("\t", " ");
//            if (debug) {
//                System.out.println(postText);
//            }
        } catch (TwitterException e) {
            e.printStackTrace();
            postText = null;
        }

        return "ppp";
    }


    public static String getTwitterMessageById(Twitter twitter, String twitterPostId, Boolean debug) {
        Long number = new Long(twitterPostId);
        String postText;
        try {
            Status status = twitter.showStatus(number);
            postText = status.getText();
            postText = postText.replace("\n", " ");
            postText = postText.replace("\t", " ");
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
//                    "/home/filip/Documents/repo/ironyDetection/src/main/resources/irony.csv"));
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


    public static Twitter getTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("8yGPXIcLrdf8PUGFGk5Dx6mPC")
                .setOAuthConsumerSecret("Kq1jbgekbuhvoWueKw9v2SJedD1vyH624VfF2JmfvNDsWcbkPh")
                .setOAuthAccessToken("1115941392386351104-sHUgkaLkrxyOmMMps8qB81Zj18ClHl")
                .setOAuthAccessTokenSecret("v2GXmdQ8tuPoxJ1ZjN4YQBGyylzGIIALXfJhaaybSp8Er");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }


}
