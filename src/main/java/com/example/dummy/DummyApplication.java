package com.example.dummy;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.io.IOException;
import java.net.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.time.LocalTime;
import java.util.Objects;

@SpringBootApplication
@RestController
public class DummyApplication {

    public DummyApplication() throws MalformedURLException {
    }

    public static void main(String[] args) {

        SpringApplication.run(DummyApplication.class, args);

    }
    private static final String TEMPLATE = "Hello, %s!";
    private static final LocalTime localtime = LocalTime.now(ZoneId.of("GMT+05:30"));
    ObjectMapper mapper = new ObjectMapper();

    @RequestMapping("/greeting")
    public HttpEntity<Greeting> greeting(
            @RequestParam(value = "name", defaultValue = "World") String name) {

        Greeting greeting = new Greeting(String.format(TEMPLATE, name));
        greeting.add(linkTo(methodOn(DummyApplication.class).greeting(name)).withSelfRel());

        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }
    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
    @GetMapping("/timestamp")
    public String timestamp(){
        return String.format("timestamp: %s", String.valueOf(localtime));
    }
    public float exchnagerate = 0.0F;
    URL url=new URL("http://www.floatrates.com/daily/EUR.json");
    @GetMapping("/reconversion")
    public String rateconvert(@RequestParam(value = "euro",defaultValue = "5.2") float euro) throws IOException {
        String[] activeProfiles = environment.getActiveProfiles();      // it will return String Array of all active profile.
        String a1 =  "";
        for(String profile:activeProfiles) {
            a1=profile;
            //System.out.print(profile);
        }
        float usd;

        if (Objects.equals(a1, "prod"))
        {
            JsonNode jsonNode = mapper.readTree(url);
            exchnagerate= Float.parseFloat(jsonNode.get("usd").get("rate").asText());
        }
        else
        {
            exchnagerate = (float) 1.06;
        }
        usd = (float) (euro*exchnagerate);

        return String.format("The price in USD %s",String.valueOf(usd));


     }
    @Autowired
    private org.springframework.core.env.Environment environment;
    @GetMapping("/checkProfile")
    public String[] checkProfile() {
        String[] activeProfiles = environment.getActiveProfiles();      // it will return String Array of all active profile.
        String a =  "";
        for(String profile:activeProfiles) {
            a=profile;
            //System.out.print(profile);
        }
        System.out.println(a);
        return activeProfiles;
    }




}
