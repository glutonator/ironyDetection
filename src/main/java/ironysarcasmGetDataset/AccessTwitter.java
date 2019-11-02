package ironysarcasmGetDataset;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccessTwitter {

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


    public static Map<Integer, long[]> parseToBatches(List<String> twitterIdsList) {
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

    public static String preprocessTextMessage(String message) {
        message = message.replace("\n", " ");
        message = message.replace("\t", " ");
        message = message.replace("\r", " ");

        return message;
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
