package camel.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class TwitterRouterBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration().component("restlet")
                .host("localhost")
                .port(8080)
                .bindingMode(RestBindingMode.auto);

        rest("/twitter")
                .get("/user_timeline/{screen_name}").produces("application/json").to("direct:user_timeline")
                .get("/friends/{screen_name}").produces("application/json").to("direct:friends")
                .get("/users/search/{q}").produces("application/json").to("direct:userSearch")
                .get("/tweets/{q}").produces("application/json").to("direct:tweets");

        from("direct:user_timeline").bean(new TwitterService(), "getUserTimeline");
        from("direct:friends").bean(new TwitterService(), "getFriends");
        from("direct:userSearch").bean(new TwitterService(), "getUserSearch");
        from("direct:tweets").bean(new TwitterService(), "getTweets");
    }
}
