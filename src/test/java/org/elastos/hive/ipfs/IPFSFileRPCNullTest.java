package org.elastos.hive.ipfs;

import org.elastos.hive.Callback;
import org.elastos.hive.HiveClient;
import org.elastos.hive.HiveClientOptions;
import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.HiveException;
import org.elastos.hive.HiveConnect;
import org.elastos.hive.result.Void;
import org.elastos.hive.vendors.ipfs.IPFSRpcNode;
import org.elastos.hive.vendors.ipfs.IPFSConnectOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class IPFSFileRPCNullTest {
    private static HiveConnect hiveConnect ;
    private static HiveClient hiveClient ;
    private static IPFSRpcNode[] hiveRpcNodes = new IPFSRpcNode[1];

    @BeforeClass
    public static void setUp() {
        HiveClientOptions hiveOptions = new HiveClientOptions();
        hiveClient = new HiveClient(hiveOptions);

        hiveRpcNodes[0] = null ;

    }

    @AfterClass
    public static void tearDown(){
        hiveClient.disConnect(hiveConnect);
        hiveClient.close();
    }

    @Test
    public void testConnect() {
        HiveConnectOptions hiveConnectOptions = new IPFSConnectOptions(hiveRpcNodes);
        hiveConnect = hiveClient.connect(hiveConnectOptions, new Callback<Void>() {
            @Override
            public void onError(HiveException e) {
                assertNotNull(e);
            }

            @Override
            public void onSuccess(Void body) {
            }
        });
    }
}
