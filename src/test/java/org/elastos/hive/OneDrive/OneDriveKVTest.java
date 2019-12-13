package org.elastos.hive.OneDrive;

import org.elastos.hive.Callback;
import org.elastos.hive.HiveClient;
import org.elastos.hive.HiveClientOptions;
import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.HiveException;
import org.elastos.hive.IHiveConnect;
import org.elastos.hive.result.Data;
import org.elastos.hive.result.ValueList;
import org.elastos.hive.vendors.onedrive.IHiveFile;
import org.elastos.hive.result.Void;
import org.elastos.hive.utils.LogUtil;
import org.elastos.hive.vendors.onedrive.OneDriveConnectOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNotNull;

public class OneDriveKVTest {

    private static final String APPID = "afd3d647-a8b7-4723-bf9d-1b832f43b881";//f0f8fdc1-294e-4d5c-b3d8-774147075480
    private static final String SCOPE = "User.Read Files.ReadWrite.All offline_access";//offline_access Files.ReadWrite
    private static final String REDIRECTURL = "http://localhost:12345";//http://localhost:44316


    private static IHiveConnect hiveConnect ;
    private static HiveClient hiveClient ;
    private static IHiveFile hiveFile ;

//    private byte[]
    private String value1 = "value1";
    private String value2 = "value2";
    private String value3 = "value3";


    @Test
    public void testGetInstance() {
        assertNotNull(hiveFile);
    }

    @Test
    public void testPutValue() {
        Void future = null;
        try {

//            byte[] data = "test".getBytes() ;
            future = hiveFile.putValue("KVT",value1.getBytes(),false).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetValue() {
        try {
            ValueList valueList = hiveFile.getValue("KVT",false).get();
            ArrayList<Data> arrayDatas = valueList.getList();
            for (int i = 0 ; i < arrayDatas.size() ; i++){
                byte[] data = arrayDatas.get(i).getData();
                LogUtil.d("result = "+new String(data));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetValueAsync() {
        CompletableFuture future = hiveFile.getValue("KVT", false, new Callback<ValueList>() {
            @Override
            public void onError(HiveException e) {

            }

            @Override
            public void onSuccess(ValueList body) {
                ArrayList list = body.getList();

                LogUtil.d("===============");
                LogUtil.d("key = "+list);
//                LogUtil.d("value = "+new String(value.getData()));
//                LogUtil.d("length = "+length);
                LogUtil.d("===============");
            }
        });
        waitFinish(future);
    }


    @Test
    public void testSetValue() {
        try {
            byte[] data = "setValue".getBytes() ;
            hiveFile.setValue("KVT",data,false).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteKey() {
        try {
            hiveFile.deleteValueFromKey("KVT").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void setUp(){
        HiveClientOptions hiveOptions = new HiveClientOptions();
        hiveClient = HiveClient.createInstance(hiveOptions);

        HiveConnectOptions hiveConnectOptions = new OneDriveConnectOptions(APPID,SCOPE,REDIRECTURL, requestUrl -> {
            try {
                Desktop.getDesktop().browse(new URI(requestUrl));
            }
            catch (Exception e) {
                e.printStackTrace();
                fail("Authenticator failed");
            }
        });

        try {
            hiveConnect = hiveClient.connect(hiveConnectOptions);
        } catch (HiveException e) {
            e.printStackTrace();
        }
        hiveFile = hiveConnect.createHiveFile("/bar","");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        hiveClient.disConnect(hiveConnect);
        hiveClient.close();
    }

    private void waitFinish(CompletableFuture future){
        while(!future.isDone()){
            try {
                LogUtil.d("1000");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
