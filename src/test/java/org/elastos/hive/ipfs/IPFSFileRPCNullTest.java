package org.elastos.hive.ipfs;

import org.elastos.hive.HiveClient;
import org.elastos.hive.HiveClientOptions;
import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.HiveException;
import org.elastos.hive.IHiveConnect;
import org.elastos.hive.result.CID;
import org.elastos.hive.result.Data;
import org.elastos.hive.result.Length;
import org.elastos.hive.util.Md5CaculateUtil;
import org.elastos.hive.util.TestUtils;
import org.elastos.hive.utils.LogUtil;
import org.elastos.hive.vendors.HiveRpcNode;
import org.elastos.hive.vendors.ipfs.IPFSConnectOptions;
import org.elastos.hive.vendors.ipfs.IPFSFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IPFSFileRPCNullTest {
    private static IHiveConnect hiveConnect ;
    private static HiveClient hiveClient ;
    private static HiveRpcNode[] hiveRpcNodes = new HiveRpcNode[1];
    private static IPFSFile hiveFile ;

    private static final CID EXPECTED_CID = new CID("QmaY6wjwnybJgd5F4FD6pPL6h9vjXrGv2BJbxxUC1ojUbQ");
    private static final CID TEST_CID = new CID("QmaY6wjwnybJgd5F4FD6pPL6h9vjXrGv2BJbxxUC1ojUbQ");

    private static final String TEST_FILE_PATH = System.getProperty("user.dir")+"/src/resources/org/elastos/hive/test.txt";

    private static final Length EXPECTED_LENGTH = new Length(17);
    private static final String EXPECTED_FILE_MD5 = "973131af48aa1d25bf187dacaa5ca7c0";

    private static final String EXPECTED_STR = "this is test file" ;

    private static final String STORE_FILE_PATH = System.getProperty("user.dir")+"/src/resources/org/elastos/hive/storetest.txt";

    @BeforeClass
    public static void setUp() {
        HiveClientOptions hiveOptions = new HiveClientOptions();
        hiveClient = HiveClient.createInstance(hiveOptions);

        hiveRpcNodes[0] = null ;

        HiveConnectOptions hiveConnectOptions = new IPFSConnectOptions(hiveRpcNodes);
        try {
            hiveConnect = hiveClient.connect(hiveConnectOptions);
        } catch (HiveException e) {
            e.printStackTrace();
        }

        if (hiveConnect != null){
            hiveFile = hiveConnect.createHiveFile("","");
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        hiveClient.disConnect(hiveConnect);
        hiveClient.close();
    }

    @Test
    public void testPutFile() {
        if (hiveFile!=null){
            CompletableFuture future = hiveFile.putFile(TEST_FILE_PATH, false);
            TestUtils.waitFinish(future);
            assertTrue(future.isCompletedExceptionally());
        }
    }

    @Test
    public void testPutFileAsync() {
        if (hiveFile!=null) {
            CompletableFuture future = hiveFile.putFile(TEST_FILE_PATH, false, cid -> {
            });
            TestUtils.waitFinish(future);
            assertTrue(future.isCompletedExceptionally());
        }
    }

    @Test
    public void testPutBuffer() {
        if (hiveFile!=null) {
            CompletableFuture future = hiveFile.putFileFromBuffer(EXPECTED_STR.getBytes(), false);
            TestUtils.waitFinish(future);
            assertTrue(future.isCompletedExceptionally());
        }
    }

    @Test
    public void testPutBufferAsync() {
        if (hiveFile!=null) {
            CompletableFuture future = hiveFile.putFileFromBuffer(EXPECTED_STR.getBytes(), false, cid -> {
            });
            TestUtils.waitFinish(future);
            assertTrue(future.isCompletedExceptionally());
        }
    }

    @Test
    public void testGetFileLength() {
        if (hiveFile!=null) {
            CompletableFuture future = hiveFile.getFileLength(TEST_CID);
            TestUtils.waitFinish(future);
            assertTrue(future.isCompletedExceptionally());
        }
    }

    @Test
    public void testGetFileLengthAsync() {
        if (hiveFile!=null) {
            CompletableFuture future = hiveFile.getFileLength(TEST_CID, length -> {
            });
            TestUtils.waitFinish(future);
            assertTrue(future.isCompletedExceptionally());
        }
    }

    @Test
    public void testGetFile() {
        if (hiveFile!=null) {
            CompletableFuture future = hiveFile.getFile(TEST_CID, false, STORE_FILE_PATH);
            TestUtils.waitFinish(future);
            assertTrue(future.isCompletedExceptionally());
        }
    }

    @Test
    public void testGetFileAsync() {
        if (hiveFile!=null) {
            CompletableFuture future = hiveFile.getFile(TEST_CID, false, STORE_FILE_PATH, length -> {
            });
            TestUtils.waitFinish(future);
            assertTrue(future.isCompletedExceptionally());
        }
    }

    @Test
    public void testGetBuffer() {
        if (hiveFile!=null) {
            CompletableFuture future = hiveFile.getFileToBuffer(TEST_CID, false);
            TestUtils.waitFinish(future);
            assertTrue(future.isCompletedExceptionally());
        }
    }


    @Test
    public void testGetBufferAsync() {
        if (hiveFile!=null) {
            CompletableFuture future = hiveFile.getFileToBuffer(TEST_CID, false, data -> {
            });
            TestUtils.waitFinish(future);
            assertTrue(future.isCompletedExceptionally());
        }
    }
}
