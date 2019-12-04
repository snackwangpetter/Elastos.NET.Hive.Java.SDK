package org.elastos.hive.ipfs;

import org.elastos.hive.Authenticator;
import org.elastos.hive.HiveClient;
import org.elastos.hive.HiveClientOptions;
import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.IHiveConnect;
import org.elastos.hive.IHiveFile;
import org.elastos.hive.Length;
import org.elastos.hive.Void;
import org.elastos.hive.utils.LogUtil;
import org.elastos.hive.vendors.HiveRpcNode;
import org.elastos.hive.vendors.ipfs.IPFSConnectOptions;
import org.elastos.hive.vendors.onedrive.OneDriveConnectOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IPFSFileTest {
    private static IHiveConnect hiveConnect ;
    private static HiveClient hiveClient ;
    private static HiveRpcNode[] hiveRpcNodes = new HiveRpcNode[3];
    private static IHiveFile hiveFile ;


    @BeforeClass
    public static void setUp() throws Exception {
        HiveClientOptions hiveOptions = new HiveClientOptions();
        hiveClient = HiveClient.createInstance(hiveOptions);

//        hiveRpcNodes[0] = new HiveRpcNode("3.133.166.156",5001);
        hiveRpcNodes[0] = new HiveRpcNode("13.59.79.222",5001);
        hiveRpcNodes[1] = new HiveRpcNode("3.133.71.168",5001);

//        hiveRpcNodes[0] = new HiveRpcNode("127.0.0.1",5001);

        HiveConnectOptions hiveConnectOptions = new IPFSConnectOptions(hiveRpcNodes);
        hiveConnect = hiveClient.connect(hiveConnectOptions);

        hiveFile = hiveConnect.createHiveFile("","");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        hiveClient.disConnect(hiveConnect);
        hiveClient.close();
    }
    @Test
    public void testGetInstance() {
        assertNotNull(hiveFile);
    }

    @Test
    public void testPutFile() {
//        String testFilepath = System.getProperty("user.dir")+"/src/resources/org/elastos/hive/test.txt";
        String testFilepath = "/Users/wangran/tmp/tmp/test.txt";

        try {
            hiveFile.putFile("hi.txt",testFilepath,false).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPutBuffer() {
//        String testFilepath = System.getProperty("user.dir")+"/src/resources/org/elastos/hive/test.txt";
        String testString = "this is test buffer";

        try {
            hiveFile.putFileFromBuffer("hi.txt",testString.getBytes(),false).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFileLength() {
//QmQfkGhRywxAEtq7DfE37h9t5JLkA7jqQTR5j3sbqBvYDf
//QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn
        try {
            Length length = hiveFile.getFileLength("QmQfkGhRywxAEtq7DfE37h9t5JLkA7jqQTR5j3sbqBvYDf").get();
            LogUtil.d("length="+length.getLength());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFile() {
        String storeFilepath = System.getProperty("user.dir")+"/src/resources/org/elastos/hive/storetest.txt";
        try {
            Length length = hiveFile.getFile("QmQfkGhRywxAEtq7DfE37h9t5JLkA7jqQTR5j3sbqBvYDf",false,storeFilepath).get();
            LogUtil.d("length="+length.getLength());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBuffer() {
        try {
            byte[] data = hiveFile.getFileToBuffer("QmQfkGhRywxAEtq7DfE37h9t5JLkA7jqQTR5j3sbqBvYDf",false,100).get();
            LogUtil.d("data="+new String(data));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
