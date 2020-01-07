package org.elastos.hive.ipfs;

import org.elastos.hive.HiveClient;
import org.elastos.hive.ClientOptions;
import org.elastos.hive.HiveConnect;
import org.elastos.hive.ConnectOptions;
import org.elastos.hive.vendors.ipfs.IPFSRpcNode;
import org.elastos.hive.vendors.ipfs.IPFSConnectOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class IPFSFileRPCUnusedTest {
    private static HiveConnect hiveConnect ;
    private static HiveClient hiveClient ;
    private static IPFSRpcNode[] hiveRpcNodes = new IPFSRpcNode[1];
    private static final String STORE_PATH = System.getProperty("user.dir");

    @BeforeClass
    public static void setUp() {
        ClientOptions hiveOptions = new ClientOptions.Builder().setStorePath(STORE_PATH).build();
        hiveClient = new HiveClient(hiveOptions);

        hiveRpcNodes[0] = new IPFSRpcNode("127.0.0.2",5001) ;
    }

    @AfterClass
    public static void tearDown(){
        hiveClient.disConnect(hiveConnect);
    }

    @Test
    public void testConnect() {
        ConnectOptions hiveConnectOptions = new IPFSConnectOptions.Builder().setRpcNodes(hiveRpcNodes).build();
        hiveConnect = hiveClient.connect(hiveConnectOptions);
    }
}
