package org.elastos.hive;

import org.elastos.hive.vendors.onedrive.OneDriveConnectOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import okio.Buffer;

import static org.junit.Assert.assertNotNull;

public class FileTest {

    private static IHiveConnect hiveConnect ;
    private static HiveClient hiveClient ;
    private static IHiveFile hiveFile ;

    @Test
    public void testGetInstance() {
        assertNotNull(hiveFile);
    }

    @Test
    public void testPutFile() {
        File file = new File();
        assertNotNull(hiveFile.putFile("","",false));
    }



    @BeforeClass
    public static void setUp() throws Exception {
        HiveClientOptions hiveOptions = new HiveClientOptions();
        hiveClient = HiveClient.createInstance(hiveOptions);

        HiveConnectOptions hiveConnectOptions = new OneDriveConnectOptions();
        hiveConnect = hiveClient.connect(hiveConnectOptions);

        hiveFile = hiveConnect.getHiveFile("","");

    }

    @AfterClass
    public static void tearDown() throws Exception {
        hiveClient.disConnect(hiveConnect);
        hiveClient.close();
    }
}
