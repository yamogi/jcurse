package org.bitbucket.keiki.jcurse.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class IntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(IntegrationTest.class);
    
    public static void main(String[] args) throws Exception {
        
//        for (int i=10;i>=0;i--) {
//            LOG.info(String.valueOf(i));
//            TimeUnit.SECONDS.sleep(1);
//        }
        long time = System.currentTimeMillis();
//        Console.main("add", "bagnon", "decursive", "auctioneer", "atlasloot-enhanced", "battlegroundtargets");
        Console.main("update", "all");
        LOG.info((System.currentTimeMillis() - time) / 1000 + " seconds");
        
    }

}
