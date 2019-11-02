package ironysarcasmGetDataset;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class AccesTwitter {

    public Twitter getTwitter() {
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
