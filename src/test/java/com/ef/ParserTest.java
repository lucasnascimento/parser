package com.ef;


import com.ef.parser.BlockedRepository;
import com.ef.parser.LogRepository;
import com.ef.parser.LogService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * This test assumes a specific file on src/test/resources/data/access.log
 * Which is 20 lines file and verify database state after it's loaded.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ParserTest {

    @Autowired
    private LogService logService;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private BlockedRepository blockedRepository;

    @Test
    public void should_load_twenty_ips_test() {
        Assert.assertEquals(20, logRepository.count());
    }

    @Test
    public void should_block_two_ips_test() {
        Assert.assertEquals(2, blockedRepository.count());
    }

    @Test
    public void should_express_correct_reason_for_two_blocked_ips_test() {
        Assert.assertEquals(2,blockedRepository.count());
        blockedRepository.findAll().forEach(
                blocked -> {
                    if ("127.0.0.1".equals(blocked.getIp())) {
                        Assert.assertEquals(
                                "This ip exceed the hourly limit of 2 between 2017-01-01T13:00 and 2017-01-01T14:00. Total requests are 5 .", blocked.getReason());
                    }
                    if ("127.0.0.2".equals(blocked.getIp())) {
                        Assert.assertEquals(
                                "This ip exceed the hourly limit of 2 between 2017-01-01T13:00 and 2017-01-01T14:00. Total requests are 4 .", blocked.getReason());
                    }
                }
        );
    }


}
