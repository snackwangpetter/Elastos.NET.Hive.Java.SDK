package org.elastos.hive.vendors.ipfs;

import org.elastos.hive.IHiveFile;
import org.elastos.hive.Length;
import org.elastos.hive.Void;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;


public class IPFSFiles implements IHiveFile {
    String fileName ;

    IPFSFiles(String fileName){
        this.fileName = fileName ;
    }


    @Override
    public CompletableFuture<Void> putFile(String destFilename, String pathname, boolean encrypt) {
        return null;
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
        return null;
    }

    @Override
    public CompletableFuture<Void> listFile(HiveFileIteraterCallback hiveFileIteraterCallback) {
        return null;
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
}
