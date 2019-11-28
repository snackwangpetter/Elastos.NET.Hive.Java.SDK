package org.elastos.hive;


import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public interface IHiveFile {
    CompletableFuture<Void> putFile(String destFilename , String sorceFilename , boolean encrypt);
    CompletableFuture<Void> putFileFromBuffer(String filename , byte[] data , boolean encrypt);
    CompletableFuture<Length> getFileLength(String filename);
    CompletableFuture<byte[]> getFileToBuffer(String filename , boolean decrypt , int bufferLen);
    CompletableFuture<Length> getFile(String filename , boolean decrypt , String storePath);
    CompletableFuture<String> getFileName();
    CompletableFuture<Void> deleteFile(String filename);
    CompletableFuture<String[]> listFile();
    CompletableFuture<String[]> listFile(HiveFileIteraterCallback hiveFileIteraterCallback);

    CompletableFuture<Void> putValue(String key , byte[] value , boolean encrypt);
    CompletableFuture<Void> setValue(String key , byte[] value , boolean encrypt);
    CompletableFuture<ArrayList<byte[]>> getValue(String key , boolean decrypt);
    CompletableFuture<Void> getValue(String key , boolean decrypt , HiveKeyValuesIterateCallback hiveKeyValuesIterateCallback);
    CompletableFuture<Void> deleteValueFromKey(String key);

    interface HiveFileIteraterCallback{
        boolean callback(String filename);
    }

    interface HiveKeyValuesIterateCallback{
        boolean callback(String key , byte[] value , int length);
    }
}
