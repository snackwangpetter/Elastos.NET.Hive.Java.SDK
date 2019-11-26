package org.elastos.hive;

import org.elastos.hive.vendors.onedrive.OneDriveConnectOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ConnectTest {

    private static IHiveConnect hiveConnect ;
    private static HiveClient hiveClient ;

    @Test
    public void testGetInstance() {
        assertNotNull(hiveConnect);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        HiveClientOptions hiveOptions = new HiveClientOptions();
        hiveClient = HiveClient.createInstance(hiveOptions);

        HiveConnectOptions hiveConnectOptions = new OneDriveConnectOptions(new Authenticator() {
            @Override
            public void requestAuthentication(String requestUrl) {

            }
        });
        hiveConnect = hiveClient.connect(hiveConnectOptions);


    }

    @AfterClass
    public static void tearDown() throws Exception {
        hiveClient.disConnect(hiveConnect);
        hiveClient.close();
    }
}
