package polishTweets;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String... args) throws TwitterException, IOException {

        Twitter twitter = getTwitter();

        getSth(twitter, "pl", "#ironia");
        getSth(twitter, "pl", "#sarkazm");
        getSth(twitter, "en", "#irony");
        getSth(twitter, "en", "#sarcasm");
    }

    public static void getSth(Twitter twitter, String lang, String query) throws TwitterException, IOException {
        Set<String> statusList = executeQuery(twitter, lang, query);
        whenWriteStringUsingBufferedWritter_thenCorrect(query, statusList);
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

    public static Set<String> executeQuery(Twitter twitter, String lang, String queryString) throws TwitterException {
        Query query = new Query(queryString);
        query.setLang(lang);
        query.setResultType(Query.RECENT);
        query.setCount(100);

        List<Status> collect = new LinkedList<>();
        while (query != null) {
            QueryResult result = twitter.search(query);
            collect.addAll(result.getTweets());
            query = result.nextQuery();

        }

        Set<String> uniqueCollect = new HashSet<>();

        for (Status status : collect) {
            String tmp = status.getText();
//            if(tmp.contains("\r")) {
//                System.out.println("bbbbbbbb");
//            }
//            if(tmp.contains("\n")) {
//                System.out.println("aaaaaaaaaa");
//            }
            tmp = tmp.replace("\n", " ");
            uniqueCollect.add(tmp);
        }

        for (String text : uniqueCollect) {
            text = text.trim();
            System.out.println(text);
        }
        System.out.println(uniqueCollect.size());

        return uniqueCollect;

    }

    public static void whenWriteStringUsingBufferedWritter_thenCorrect(String query, Set<String> collect) throws IOException {
        String fileName = query + java.time.LocalDateTime.now() + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (String status : collect) {
            writer.write(status + "\n");
        }
        writer.close();
        System.out.println(java.time.LocalDateTime.now());
    }


}
