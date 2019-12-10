package org.elastos.hive.ipfs;

import org.elastos.hive.Authenticator;
import org.elastos.hive.HiveClient;
import org.elastos.hive.HiveClientOptions;
import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.IHiveConnect;
import org.elastos.hive.vendors.HiveRpcNode;
import org.elastos.hive.vendors.ipfs.IPFSConnectOptions;
import org.elastos.hive.vendors.onedrive.OneDriveConnectOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class IPFSConnectTest {

    private static IHiveConnect hiveConnect ;
    private static HiveClient hiveClient ;
    private static HiveRpcNode[] hiveRpcNodes = new HiveRpcNode[1];

    @Test
    public void testGetInstance() {
        assertNotNull(hiveConnect);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        HiveClientOptions hiveOptions = new HiveClientOptions();
        hiveClient = HiveClient.createInstance(hiveOptions);

//        hiveRpcNodes[0] = new HiveRpcNode("3.133.166.156",5001);
//        hiveRpcNodes[1] = new HiveRpcNode("13.59.79.222",5001);
//        hiveRpcNodes[2] = new HiveRpcNode("3.133.71.168",5001);
        hiveRpcNodes[0] = new HiveRpcNode("127.0.0.1",5001);

        HiveConnectOptions hiveConnectOptions = new IPFSConnectOptions(hiveRpcNodes);
        hiveConnect = hiveClient.connect(hiveConnectOptions);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        hiveClient.disConnect(hiveConnect);
        hiveClient.close();
    }
}
