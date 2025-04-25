package com.deloitte.elrr.test;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.elrr.ElrrExternalServicesApplication;

@SpringBootTest(classes = ElrrExternalServicesApplication.class)
@RunWith(SpringRunner.class)
class DemoApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true);
    }

}
