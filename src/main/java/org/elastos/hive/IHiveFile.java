package org.elastos.hive;


import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

public interface IHiveFile {
    CompletableFuture<Void> putFile(String destFilename , String sorceFilename , boolean encrypt);
    CompletableFuture<Void> putFileFromBuffer(String filename , ByteBuffer byteBuffer, int length , boolean encrypt);
    CompletableFuture<Length> getFileLength(String filename);
    CompletableFuture<Length> getFileToBuffer(String filename , boolean decrypt , ByteBuffer buffer , int bufferLen);
    CompletableFuture<Void> getFile(String filename , boolean decrypt , String storePath);
    CompletableFuture<String> getFileName();
    CompletableFuture<Void> deleteFile(String filename);
    CompletableFuture<String[]> listFile();
    CompletableFuture<String[]> listFile(HiveFileIteraterCallback hiveFileIteraterCallback);

    CompletableFuture<Void> putValue(String key , ByteBuffer value , boolean encrypt);
    CompletableFuture<Void> setValue(String key , ByteBuffer value , boolean encrypt);
    CompletableFuture<ByteBuffer> getValue(String key , boolean decrypt);
    CompletableFuture<Void> getValue(String key , boolean decrypt , HiveKeyValuesIterateCallback hiveKeyValuesIterateCallback);
    CompletableFuture<Void> deleteValueFromKey(String key);

    interface HiveFileIteraterCallback{
        boolean callback(String filename);
    }

    interface HiveKeyValuesIterateCallback{
        boolean callback(String key , Object value , int length);
    }
}
