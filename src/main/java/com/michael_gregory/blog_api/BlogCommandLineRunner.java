package com.michael_gregory.blog_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BlogCommandLineRunner implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {

        System.out.println("Command line runner working...");
    }
}
