package com.java.asm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;

@RestController
public class TestController {

    @GetMapping("/test/old")
    public void old() {
        TestOriginalClass.testMethodOld();
    }

    @PostConstruct
    public void init() throws Exception {
        TestOriginalClass.testASM();
    }

    @GetMapping("/test/new")
    public void newMethod() throws IOException {
        new TestOriginalClass().testMethod();
    }
}
