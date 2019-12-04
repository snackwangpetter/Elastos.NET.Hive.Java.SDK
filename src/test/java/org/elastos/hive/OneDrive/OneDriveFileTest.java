package org.elastos.hive.OneDrive;

import org.elastos.hive.Authenticator;
import org.elastos.hive.HiveClient;
import org.elastos.hive.HiveClientOptions;
import org.elastos.hive.HiveConnectOptions;
import org.elastos.hive.IHiveConnect;
import org.elastos.hive.IHiveFile;
import org.elastos.hive.Length;
import org.elastos.hive.Void;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OneDriveFileTest {

    private static final String APPID = "afd3d647-a8b7-4723-bf9d-1b832f43b881";//f0f8fdc1-294e-4d5c-b3d8-774147075480
    private static final String SCOPE = "User.Read Files.ReadWrite.All offline_access";//offline_access Files.ReadWrite
    private static final String REDIRECTURL = "http://localhost:12345";//http://localhost:44316


    private static IHiveConnect hiveConnect ;
    private static HiveClient hiveClient ;
    private static IHiveFile hiveFile ;

    @Test
    public void testGetInstance() {
        assertNotNull(hiveFile);
    }

    @Test
    public void testPutFile() {
        String testFilepath = System.getProperty("user.dir")+"/src/resources/org/elastos/hive/test.txt";

//        String testFilepath = "/Users/wangran/tmp/tmp/test.txt";
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
        String testString = "this is test for buffer";
        byte[] data = testString.getBytes() ;

        CompletableFuture future = hiveFile.putFileFromBuffer("hi2.txt",data ,false);

        waitFinish(future);

    }

    @Test
    public void testFileLength(){
        try {
            Length length = hiveFile.getFileLength("hi.txt").get();
            LogUtil.d("length = "+length.getLength());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFile(){
        try {
            hiveFile.getFile("hi.txt",false,"/Users/wangran/tmp/hi.txt").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFileBuffer(){
        try {
            byte[] bytes = hiveFile.getFileToBuffer("hi.txt",false,-1).get();

            String str = new String(bytes);
            LogUtil.d("result = "+str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDeleteFile(){
        String deleteTestFile = "hi.txt";

//        CompletableFuture future = hiveFile.putFile(deleteTestFile,System.getProperty("user.dir")+"/src/resources/org/elastos/hive/test.txt",false)
////                .thenApply(result -> testEqual())
//                .thenApply(result -> hiveFile.deleteFile(deleteTestFile));

        try {
            hiveFile.deleteFile(deleteTestFile).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        waitFinish(future);
    }

    private CompletableFuture testEqual(String expected , String actual){
        CompletableFuture<Void> completableFuture = new CompletableFuture();
        assertEquals("","");
        completableFuture.complete(new Void());
        return completableFuture;
    }

    @Test
    public void testListFiles(){
        try {
            String[] files = hiveFile.listFile().get();
            for (int i = 0 ; i<files.length ; i++){
                LogUtil.d("file = "+files[i]);
                assertNotNull(files[i]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListFilesAsync(){

        CompletableFuture future = hiveFile.listFile(new IHiveFile.HiveFileIteraterCallback(){
            @Override
            public boolean callback(String filename) {
                LogUtil.d("file = "+filename);
                return false;
            }
        });

        waitFinish(future);
    }


    @BeforeClass
    public static void setUp() throws Exception {
        HiveClientOptions hiveOptions = new HiveClientOptions();
        hiveClient = HiveClient.createInstance(hiveOptions);

        HiveConnectOptions hiveConnectOptions = new OneDriveConnectOptions(new Authenticator() {
            @Override
            public void requestAuthentication(String requestUrl) {
                try {
                    Desktop.getDesktop().browse(new URI(requestUrl));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    fail("Authenticator failed");
                }
            }
        });
        ((OneDriveConnectOptions) hiveConnectOptions).setClientId(APPID);
        ((OneDriveConnectOptions) hiveConnectOptions).setScope(SCOPE);
        ((OneDriveConnectOptions) hiveConnectOptions).setRedirectUrl(REDIRECTURL);


        hiveConnect = hiveClient.connect(hiveConnectOptions);

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

    private void createTestRootPath(){
    }

    private void getContentFromFile(String filepath){
        File file = new File(filepath);
        try {
            FileInputStream out = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(out);
            int ch = 0;
            while ((ch = isr.read()) != -1) {
                System.out.print((char) ch);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
