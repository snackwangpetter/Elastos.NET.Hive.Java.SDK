package org.elastos.hive.vendors.ipfs;


import org.elastos.hive.result.CID;
import org.elastos.hive.result.Data;
import org.elastos.hive.result.Length;

import java.util.concurrent.CompletableFuture;

public interface IHiveIPFS{
    CompletableFuture<CID> putFile(String absPath, boolean encrypt);
    CompletableFuture<CID> putFile(String absPath, boolean encrypt , HiveIPFSPutDataCallback hiveIPFSPutDataCallback);

    CompletableFuture<CID> putFileFromBuffer(byte[] data, boolean encrypt);
    CompletableFuture<CID> putFileFromBuffer(byte[] data, boolean encrypt , HiveIPFSPutDataCallback hiveIPFSPutDataCallback);

    CompletableFuture<Length> getFileLength(CID cid);
    CompletableFuture<Length> getFileLength(CID cid , HiveIPFSGetLengthCallback hiveGetLengthCallback);

    CompletableFuture<Data> getFileToBuffer(CID cid, boolean decrypt);
    CompletableFuture<Data> getFileToBuffer(CID cid, boolean decrypt , HiveIPFSGetDataCallback hiveIPFSGetDataCallback);

    CompletableFuture<Length> getFile(CID cid , boolean decrypt, String storeAbsPath);
    CompletableFuture<Length> getFile(CID cid , boolean decrypt, String storeAbsPath , HiveIPFSStoreFileCallback hiveIPFSStoreFileCallback);

    interface HiveIPFSPutDataCallback{
        void callback(CID cid);
    }

    interface HiveIPFSGetLengthCallback{
        void callback(Length length);
    }

    interface HiveIPFSGetDataCallback{
        void callback(Data data);
    }

    interface HiveIPFSStoreFileCallback{
        void callback(Length length);
    }
}


