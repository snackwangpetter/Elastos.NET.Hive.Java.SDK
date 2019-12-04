package org.elastos.hive.vendors.ipfs;

import org.elastos.hive.HiveError;
import org.elastos.hive.HiveException;
import org.elastos.hive.IHiveFile;
import org.elastos.hive.Length;
import org.elastos.hive.Void;
import org.elastos.hive.utils.LogUtil;
import org.elastos.hive.utils.ResponseHelper;
import org.elastos.hive.vendors.connection.ConnectionManager;
import org.elastos.hive.vendors.ipfs.network.model.AddFileResponse;
import org.elastos.hive.vendors.ipfs.network.model.ListFileResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;


public class IPFSFile implements IHiveFile {
    String fileName ;
    String cid ;
    IPFSRpc ipfsRpc ;

    IPFSFile(String fileName , String cid , IPFSRpc ipfsRpc){
        this.fileName = fileName ;
        this.cid = cid ;
        this.ipfsRpc = ipfsRpc ;
    }

    IPFSFile(String fileName , String cid){
        this.fileName = fileName ;
        this.cid = cid ;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }


    @Override
    public CompletableFuture<Void> putFile(String destFilename, String sorceFilename, boolean encrypt) {
        CompletableFuture completableFuture = new CompletableFuture();
        return checkConnection(completableFuture).thenCompose(result -> doPutFile(completableFuture,sorceFilename));
    }

    @Override
    public CompletableFuture<Void> putFileFromBuffer(String filename, byte[] data, boolean encrypt) {
        CompletableFuture completableFuture = new CompletableFuture();
        return checkConnection(completableFuture).thenCompose(result -> doPutBuffer(completableFuture,data));
    }

    @Override
    public CompletableFuture<Length> getFileLength(String filename) {
        CompletableFuture completableFuture = new CompletableFuture();
        return checkConnection(completableFuture).thenCompose(result -> doGetFileLength(completableFuture,filename));
    }

    @Override
    public CompletableFuture<byte[]> getFileToBuffer(String filename, boolean decrypt, int bufferLen) {
        CompletableFuture completableFuture = new CompletableFuture();
        return checkConnection(completableFuture).thenCompose(result -> doGetBuffer(completableFuture,filename));
    }

    @Override
    public CompletableFuture<Length> getFile(String filename, boolean decrypt, String storePath) {
        CompletableFuture completableFuture = new CompletableFuture();
        return checkConnection(completableFuture).thenCompose(result -> doCatFile(completableFuture,filename,storePath));
    }

    @Override
    public CompletableFuture<String> getFileName() {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteFile(String filename) {

        return createNotImpException("Delete file function is not supported in IPFS backend");
    }

    @Override
    public CompletableFuture<String[]> listFile() {
        return null;
    }

    @Override
    public CompletableFuture<String[]> listFile(HiveFileIteraterCallback hiveFileIteraterCallback) {
        return null;
    }

    @Override
    public CompletableFuture<Void> putValue(String key, byte[] value, boolean encrypt) {
        return createNotImpException("Put value function is not supported in IPFS backend");
    }

    @Override
    public CompletableFuture<Void> setValue(String key, byte[] value, boolean encrypt) {
        return createNotImpException("Set value function is not supported in IPFS backend");
    }

    @Override
    public CompletableFuture<ArrayList<byte[]>> getValue(String key, boolean decrypt) {
        return createNotImpException("Get value function is not supported in IPFS backend");
    }

    @Override
    public CompletableFuture<Void> getValue(String key, boolean decrypt, HiveKeyValuesIterateCallback hiveKeyValuesIterateCallback) {
        return createNotImpException("Get value function is not supported in IPFS backend");
    }

    @Override
    public CompletableFuture<Void> deleteValueFromKey(String key) {
        return createNotImpException("Delete value function is not supported in IPFS backend");
    }

    private CompletableFuture createNotImpException(String msg){
        CompletableFuture completableFuture = new CompletableFuture();
        HiveException exception = new HiveException(msg);
        completableFuture.completeExceptionally(exception);
        return completableFuture;
    }

    private CompletableFuture<Void> doPutFile(CompletableFuture future , String sorceFilename){
        try {
            RequestBody requestBody = createFileRequestBody(sorceFilename);
            Response response = ConnectionManager.getIPFSApi().addFile(requestBody).execute();
            if (response.code() == 200){
                AddFileResponse addFileResponse = (AddFileResponse) response.body();
                LogUtil.d(addFileResponse.getHash());
                future.complete(new Void());
            }
        } catch (Exception e) {
            future.completeExceptionally(new HiveException(HiveError.PUT_FILE_ERROR));
        }
        return future;
    }

    private CompletableFuture<Void> doPutBuffer(CompletableFuture future , byte[] data){
        try {
            RequestBody requestBody = createBufferRequestBody(data);
            Response response = ConnectionManager.getIPFSApi().addFile(requestBody).execute();
            if (response.code() == 200){
                AddFileResponse addFileResponse = (AddFileResponse) response.body();
                LogUtil.d(addFileResponse.getHash());
                future.complete(new Void());
            }
        } catch (Exception e) {
            future.completeExceptionally(new HiveException(HiveError.PUT_Buffer_ERROR));
        }

        return future;
    }

    private RequestBody createFileRequestBody(String sorceFilename){
        if (sorceFilename == null || sorceFilename.equals("")){
            return null ;
        }

        File file = new File(sorceFilename);

        //RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), testbyte);
        //MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "testfile", fileBody)
                .build();

        return requestBody ;
    }

    private RequestBody createBufferRequestBody(byte[] data){
        if (data == null || data.length == 0){
            return null ;
        }

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), data);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "testfile", fileBody)
                .build();

        return requestBody ;
    }

    private CompletableFuture doGetFileLength(CompletableFuture future , String cid){
        String hash ;
        int size ;
        Response response = null;
        try {
            response = ConnectionManager.getIPFSApi().listFile(cid).execute();
        } catch (Exception e) {
            future.completeExceptionally(new HiveException(HiveError.GET_FILE_LENGTH_ERROR));
        }
        ListFileResponse listFileResponse = (ListFileResponse) response.body();

        HashMap<String, ListFileResponse.ObjectsBean.Bean> map = listFileResponse.getObjects();

        if (null!=map && map.size()>0){
            for (String key:map.keySet()){
                hash = map.get(key).getHash();
                size = map.get(key).getSize();
                future.complete(new Length(size));
                break;//if result only one
            }
        }else{
            future.completeExceptionally(new HiveException(HiveError.GET_FILE_LENGTH_ERROR));
        }
        return future;
    }

    private CompletableFuture doCatFile(CompletableFuture future , String cid,String storeFilepath){
        try {
            Response response = getFileOrBuffer(cid);
            if (response!=null){
                long length = ResponseHelper.saveFileFromResponse(storeFilepath,response);

                Length lengthObj = new Length(length);
                future.complete(lengthObj);
            }else{
                future.completeExceptionally(new HiveException(HiveError.GET_FILE_ERROR));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return future;
    }

    private CompletableFuture doGetBuffer(CompletableFuture future , String cid){
        try {
            Response response = getFileOrBuffer(cid);
            if (response!=null){
                byte[] buffer = ResponseHelper.getBuffer(response);
                future.complete(buffer);
            }else{
                future.completeExceptionally(new HiveException(HiveError.GET_FILE_ERROR));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return future;
    }

    private Response getFileOrBuffer(String cid) throws HiveException {
        Response response ;
        try {
            response = ConnectionManager.getIPFSApi()
                    .catFile(cid)
                    .execute();
        } catch (Exception ex) {
            throw new HiveException(ex.getMessage());
        }
        return response;
    }

    private CompletableFuture checkConnection(CompletableFuture future){
        if (!ipfsRpc.isAvailable()){
            future.completeExceptionally(new HiveException(HiveError.NO_RPC_NODE_AVAILABLE));
        }
        return future;
    }
}
