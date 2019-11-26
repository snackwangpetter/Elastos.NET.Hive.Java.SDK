package org.elastos.hive.vendors.onedrive;

import org.elastos.hive.AuthHelper;
import org.elastos.hive.HiveException;
import org.elastos.hive.IHiveFile;
import org.elastos.hive.Length;
import org.elastos.hive.Void;
import org.elastos.hive.vendors.connection.ConnectionManager;
import org.elastos.hive.vendors.onedrive.network.OneDriveApi;
import org.elastos.hive.vendors.onedrive.network.model.DirChildrenResponse;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OneDriveFiles implements IHiveFile {
    private String filename ;
    private AuthHelper authHelper;
    OneDriveFiles (String filename , AuthHelper authHelper){
        this.filename = filename ;
        this.authHelper = authHelper ;
    }

    @Override
    public CompletableFuture<Void> putFile(String destFilename, String pathname, boolean encrypt) {
        return authHelper.checkExpired()
                .thenCompose(result ->createFileSync(destFilename))
                .thenCompose(result ->writeFileToBackend(destFilename, pathname));
    }

    @Override
    public CompletableFuture<Void> putFileFromBuffer(String filename, ByteBuffer byteBuffer, int length, boolean encrypt) {
        return null;
    }

    @Override
    public CompletableFuture<Length> getFileLength(String filename) {
        return null;
    }

    @Override
    public CompletableFuture<Length> getFileToBuffer(String filename, boolean decrypt, ByteBuffer buffer, int bufferLen) {
        return null;
    }

    @Override
    public CompletableFuture<Void> getFile(String filename, boolean decrypt, String storePath) {
        return null;
    }

    @Override
    public CompletableFuture<String> getFileName() {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteFile(String filename) {
        return null;
    }

    @Override
    public CompletableFuture<String[]> listFile() {
        return authHelper.checkExpired().thenCompose(result -> listFileSync());
    }

    @Override
    public CompletableFuture<String[]> listFile(HiveFileIteraterCallback hiveFileIteraterCallback) {
        return authHelper.checkExpired().thenCompose(result -> listFileAsync(hiveFileIteraterCallback));
    }

    @Override
    public CompletableFuture<Void> putValue(String key, ByteBuffer value, boolean encrypt) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setValue(String key, ByteBuffer value, boolean encrypt) {
        return null;
    }

    @Override
    public CompletableFuture<ByteBuffer> getValue(String key, boolean decrypt) {
        return null;
    }

    @Override
    public CompletableFuture<Void> getValue(String key, boolean decrypt, HiveKeyValuesIterateCallback hiveKeyValuesIterateCallback) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteValueFromKey(String key) {
        return null;
    }


    private String creatDestFilePath(String destFileName){
        return filename +"/"+ destFileName;
    }

    private CompletableFuture<Void> createFileSync(String destFilename){
        CompletableFuture future = new CompletableFuture() ;
        String destFilePath = creatDestFilePath(destFilename);
        try {
            Response response = ConnectionManager.getOnedriveApi()
                    .createFile(destFilePath)
                    .execute();
            if (response.code() == 200) future.complete(new Void());
            else future.completeExceptionally(new HiveException("create backend file error"));
        } catch (Exception e) {
            future.completeExceptionally(e);
            e.printStackTrace();
        }
        return future;
    }

    private CompletableFuture<Void> writeFileToBackend(String destFilename ,  String pathname){
        CompletableFuture<Void> future = new CompletableFuture<>();
        File file = new File(pathname);
        String destFilePath = creatDestFilePath(destFilename);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        try {
            Response response = ConnectionManager.getOnedriveApi()
                    .write(destFilePath, requestBody)
                    .execute();
            if (response.code() == 200) future.complete(new Void());
            else future.completeExceptionally(new HiveException("write file to backend error"));
        } catch (Exception e) {
            future.completeExceptionally(e);
            e.printStackTrace();
        }
        return future;
    }

    private CompletableFuture<String[]> listFileSync(){
        CompletableFuture future = new CompletableFuture();
        try {
            OneDriveApi api = ConnectionManager.getOnedriveApi();
            Call<DirChildrenResponse> call;
            if (this.filename.equals("/")) call = api.getRootChildren();
            else call = api.getChildren(this.filename);
            Response<DirChildrenResponse> response = call.execute();
            future.complete(decodeListFile(response));
        } catch (Exception ex) {
            HiveException e = new HiveException(ex.getMessage());
            future.completeExceptionally(e);
        }
        return future;
    }

    public CompletableFuture<String[]> listFileAsync(HiveFileIteraterCallback hiveFileIteraterCallback) {
        CompletableFuture future = new CompletableFuture();
        try {
            OneDriveApi api = ConnectionManager.getOnedriveApi();
            Call<DirChildrenResponse> call;
            if (this.filename.equals("/")) call = api.getRootChildren();
            else call = api.getChildren(this.filename);
            call.enqueue(new Callback<DirChildrenResponse>() {
                @Override
                public void onResponse(Call<DirChildrenResponse> call, Response<DirChildrenResponse> response) {
                    String[] results = decodeListFile(response);
                    for (String result : results){
                        hiveFileIteraterCallback.callback(result);
                    }
                    future.complete(results);
                }
                @Override
                public void onFailure(Call<DirChildrenResponse> call, Throwable t) {
                    future.completeExceptionally(t);
                }
            });
        } catch (Exception ex) {
            HiveException e = new HiveException(ex.getMessage());
            future.completeExceptionally(e);
        }
        return future;
    }

    private String[] decodeListFile(Response response){
        DirChildrenResponse dirChildrenResponse = (DirChildrenResponse) response.body();
        List<DirChildrenResponse.ValueBean> list = dirChildrenResponse.getValue();
        String[] beans = new String[list.size()];
        for (int i =0 ; i<beans.length ; i++){
            beans[i] = list.get(i).getName();
        }
        return beans;
    }
}
