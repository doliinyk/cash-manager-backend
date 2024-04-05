package com.cashmanagerbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.UseMainMethod;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(useMainMethod = UseMainMethod.ALWAYS)
class CashManagerBackendApplicationTest {
    @Test
    void contextLoads(ApplicationContext applicationContext) {
        assertNotNull(applicationContext);
    }
}
