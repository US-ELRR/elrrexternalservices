package com.deloitte.elrr;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ElrrExternalServicesApplication.class)
@RunWith(SpringRunner.class)
class DemoApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true);
    }

}
