package org.elastos.hive.vendors.ipfs;

import org.elastos.hive.HiveError;
import org.elastos.hive.HiveException;
import org.elastos.hive.HiveFile;
import org.elastos.hive.result.CID;
import org.elastos.hive.result.Data;
import org.elastos.hive.utils.ResponseHelper;
import org.elastos.hive.vendors.connection.ConnectionManager;
import org.elastos.hive.vendors.ipfs.network.model.AddFileResponse;
import org.elastos.hive.vendors.ipfs.network.model.ListFileResponse;
import org.elastos.hive.result.Length;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;


public class IPFSFile extends HiveFile implements IHiveIPFS {
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

    private CompletableFuture createNotImpException(String msg){
        CompletableFuture completableFuture = new CompletableFuture();
        HiveException exception = new HiveException(msg);
        completableFuture.completeExceptionally(exception);
        return completableFuture;
    }

    private CompletableFuture<CID> doPutFile(String sorceFilename , HiveIPFSPutDataCallback hiveIPFSPutDataCallback){
        CompletableFuture future = new CompletableFuture() ;
        if (!checkConnection(future)) return future;
        try {
            MultipartBody.Part requestBody = createFileRequestBody(sorceFilename);
            Response response = ConnectionManager.getIPFSApi().addFile(requestBody).execute();
            if (response == null){
                future.completeExceptionally(new HiveException(HiveError.PUT_FILE_ERROR));
                return future;
            }
            if (response.code() == 200){
                AddFileResponse addFileResponse = (AddFileResponse) response.body();
                CID cid = new CID(addFileResponse.getHash());
                if (hiveIPFSPutDataCallback != null){
                    hiveIPFSPutDataCallback.callback(cid);
                }
                future.complete(cid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            future.completeExceptionally(new HiveException(HiveError.PUT_FILE_ERROR));
        }
        return future;
    }

    private CompletableFuture<CID> doPutBuffer(byte[] data , HiveIPFSPutDataCallback hiveIPFSPutDataCallback){
        CompletableFuture future = new CompletableFuture();
        if (!checkConnection(future)) return future;
        try {
            MultipartBody.Part requestBody = createBufferRequestBody(data);
            Response response = ConnectionManager.getIPFSApi().addFile(requestBody).execute();
            if (response == null){
                future.completeExceptionally(new HiveException(HiveError.PUT_BUFFER_ERROR));
                return future;
            }
            if (response.code() == 200){
                AddFileResponse addFileResponse = (AddFileResponse) response.body();
                CID cid = new CID(addFileResponse.getHash());
                if (hiveIPFSPutDataCallback!=null){
                    hiveIPFSPutDataCallback.callback(cid);
                }
                future.complete(cid);
            }
        } catch (Exception e) {
            future.completeExceptionally(new HiveException(HiveError.PUT_BUFFER_ERROR));
        }

        return future;
    }

    private MultipartBody.Part createFileRequestBody(String sorceFilename){
        if (sorceFilename == null || sorceFilename.equals("")){
            return null ;
        }
        File file = new File(sorceFilename);
        RequestBody requestFile = RequestBody.create(null, file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return body ;
    }

    private MultipartBody.Part createBufferRequestBody(byte[] data){
        if (data == null || data.length == 0){
            return null ;
        }
        RequestBody requestFile = RequestBody.create(null, data);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "data", requestFile);
        return body ;
    }

    private CompletableFuture<Length> doGetFileLength(CID cid ,HiveIPFSGetLengthCallback hiveGetLengthCallback){
        CompletableFuture<Length> future = new CompletableFuture();
        if (!checkConnection(future)) return future;
        String hash ;
        int size = 0;
        Response response = null;
        try {
            response = ConnectionManager.getIPFSApi().listFile(cid.getCid()).execute();
        } catch (Exception e) {
            future.completeExceptionally(new HiveException(HiveError.GET_FILE_LENGTH_ERROR));
        }
        if (response == null){
            future.completeExceptionally(new HiveException(HiveError.GET_FILE_LENGTH_ERROR));
            return future;
        }
        ListFileResponse listFileResponse = (ListFileResponse) response.body();

        HashMap<String, ListFileResponse.ObjectsBean.Bean> map = listFileResponse.getObjects();

        if (null!=map && map.size()>0){
            for (String key:map.keySet()){
                hash = map.get(key).getHash();
                size = map.get(key).getSize();
                break;//if result only one
            }
            Length length = new Length(size);
            if (hiveGetLengthCallback!=null){
                hiveGetLengthCallback.callback(length);
            }
            future.complete(length);
        }else{
            future.completeExceptionally(new HiveException(HiveError.GET_FILE_LENGTH_ERROR));
        }
        return future;
    }

    private CompletableFuture<Length> doCatFile(CID cid,String storeFilepath ,HiveIPFSStoreFileCallback hiveIPFSStoreFileCallback){
        CompletableFuture<Length> future = new CompletableFuture() ;
        if (!checkConnection(future)) return future;
        try {
            Response response = getFileOrBuffer(cid.getCid());
            if (response == null){
                future.completeExceptionally(new HiveException(HiveError.GET_FILE_ERROR));
                return future ;
            }
            long length = ResponseHelper.saveFileFromResponse(storeFilepath,response);
            Length lengthObj = new Length(length);
            if (hiveIPFSStoreFileCallback!=null){
                hiveIPFSStoreFileCallback.callback(lengthObj);
            }
            future.complete(lengthObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return future;
    }

    private CompletableFuture<Data> doGetBuffer(CID cid , HiveIPFSGetDataCallback hiveIPFSGetDataCallback){
        CompletableFuture<Data> future = new CompletableFuture();
        if (!checkConnection(future)) return future;
        try {
            Response response = getFileOrBuffer(cid.getCid());
            if (response!=null){
                byte[] buffer = ResponseHelper.getBuffer(response);
                Data data = new Data(buffer);
                if (hiveIPFSGetDataCallback != null){
                    hiveIPFSGetDataCallback.callback(data);
                }
                future.complete(data);
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

    private boolean checkConnection(CompletableFuture future){
        if (!ipfsRpc.isAvailable()){
            future.completeExceptionally(new HiveException(HiveError.NO_RPC_NODE_AVAILABLE));
            return false;
        }
        return true ;
    }

    @Override
    public CompletableFuture<CID> putFile(String absPath, boolean encrypt) {
        return doPutFile(absPath , null);
    }

    @Override
    public CompletableFuture<CID> putFile(String absPath, boolean encrypt, HiveIPFSPutDataCallback hiveIPFSPutDataCallback) {
        return doPutFile(absPath , hiveIPFSPutDataCallback);
    }

    @Override
    public CompletableFuture<CID> putFileFromBuffer(byte[] data, boolean encrypt) {
        return doPutBuffer(data , null);
    }

    @Override
    public CompletableFuture<CID> putFileFromBuffer(byte[] data, boolean encrypt, HiveIPFSPutDataCallback hiveIPFSPutDataCallback) {
        return doPutBuffer(data , hiveIPFSPutDataCallback);
    }

    @Override
    public CompletableFuture<Length> getFileLength(CID cid) {
        return doGetFileLength(cid , null);
    }

    @Override
    public CompletableFuture<Length> getFileLength(CID cid, HiveIPFSGetLengthCallback hiveGetLengthCallback) {
        return doGetFileLength(cid , hiveGetLengthCallback);
    }

    @Override
    public CompletableFuture<Data> getFileToBuffer(CID cid, boolean decrypt) {
        return doGetBuffer(cid , null);
    }

    @Override
    public CompletableFuture<Data> getFileToBuffer(CID cid, boolean decrypt, HiveIPFSGetDataCallback hiveIPFSGetDataCallback) {
        return doGetBuffer(cid , hiveIPFSGetDataCallback);
    }

    @Override
    public CompletableFuture<Length> getFile(CID cid, boolean decrypt, String storeAbsPath) {
        return doCatFile(cid,storeAbsPath , null);
    }

    @Override
    public CompletableFuture<Length> getFile(CID cid, boolean decrypt, String storeAbsPath, HiveIPFSStoreFileCallback hiveIPFSStoreFileCallback) {
        return doCatFile(cid,storeAbsPath , hiveIPFSStoreFileCallback);
    }


}
