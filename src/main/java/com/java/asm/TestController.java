package com.java.asm;

import com.java.asm.model.ClassNameChangeEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

@RestController
public class TestController {

    @GetMapping("/test/old")
    public void old() {
        TestOriginalClass.testMethodOld();
    }

    @PostConstruct
    public void init() throws Exception {
        ClassNameChangeEntity classNameChangeEntity = new ClassNameChangeEntity();

        classNameChangeEntity.setClazz(TestOriginalClass.class);
        classNameChangeEntity.setOldClassName("com/java/asm/v1/BASE64Encoder");
        classNameChangeEntity.setNewClassName("com/java/asm/v2/BASE64Encoder");
        classNameChangeEntity.setMethodNames(Arrays.asList("testMethod"));

        ASMUtils.asmClassPathUpdate(classNameChangeEntity);
    }

    @GetMapping("/test/new")
    public void newMethod() throws IOException {
        TestOriginalClass.testMethod();
    }
}
