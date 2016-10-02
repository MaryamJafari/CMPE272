package camel.rest;

import org.apache.camel.Exchange;

import java.io.*;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class TwitterService {
    public void getTweets(Exchange exchange) {
        String q = exchange.getIn().getHeader("q").toString();
        String urlString = "https://api.twitter.com/1.1/search/tweets.json?q=" + q +"&result_type=mixed";
        String bodyMessage = httpGetData(urlString);
        exchange.getIn().setBody(bodyMessage);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
    }

    public void getUserTimeline(Exchange exchange) {
        String screen_name = exchange.getIn().getHeader("screen_name").toString();
        String urlString = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=" + screen_name;
        String bodyMessage = httpGetData(urlString);
        exchange.getIn().setBody(bodyMessage);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
    }

    public void getFriends(Exchange exchange) {
        String screen_name = exchange.getIn().getHeader("screen_name").toString();
        String urlString = "https://api.twitter.com/1.1/friends/list.json?cursor=-1&screen_name=" + screen_name + "&skip_status=true&include_user_entities=false";
        String bodyMessage = httpGetData(urlString);
        exchange.getIn().setBody(bodyMessage);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
    }

    public void getUserSearch(Exchange exchange) {
        String q = exchange.getIn().getHeader("q").toString();
        String urlString = "https://api.twitter.com/1.1/users/search.json?q=" + q + "&page=1";
        String bodyMessage = httpGetData(urlString);
        exchange.getIn().setBody(bodyMessage);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
    }

    public String httpGetData(String urlString) {
        try {
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(TwitterCredentials.consumerKey, TwitterCredentials.consumerSecret);
            consumer.setTokenWithSecret(TwitterCredentials.accessToken, TwitterCredentials.accessTokenSecret);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(urlString);
            consumer.sign(request);

            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = rd.readLine())!= null) {
                sb.append(line);
            }
            rd.close();
            return sb.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}