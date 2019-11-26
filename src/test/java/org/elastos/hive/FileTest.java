package org.elastos.hive;

import org.elastos.hive.utils.LogUtil;
import org.elastos.hive.vendors.onedrive.OneDriveConnectOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNotNull;

public class FileTest {

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

        CompletableFuture future = hiveFile.putFile("hi.txt",testFilepath,false);

        waitFinish(future);

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
}
