package org.elastos.hive.vendors.ipfs;

import org.elastos.hive.IHiveFile;
import org.elastos.hive.Length;
import org.elastos.hive.Void;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


public class IPFSFiles implements IHiveFile {
    String fileName ;
    String cid ;

    IPFSFiles(String fileName , String cid){
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
        return null;
    }

    @Override
    public CompletableFuture<Void> putFileFromBuffer(String filename, byte[] data, boolean encrypt) {
        return null;
    }

    @Override
    public CompletableFuture<Length> getFileLength(String filename) {
        return null;
    }

    @Override
    public CompletableFuture<byte[]> getFileToBuffer(String filename, boolean decrypt, int bufferLen) {
        return null;
    }

    @Override
    public CompletableFuture<Length> getFile(String filename, boolean decrypt, String storePath) {
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
    public CompletableFuture<String[]> listFile(HiveFileIteraterCallback hiveFileIteraterCallback) {
        return null;
    }

    @Override
    public CompletableFuture<Void> putValue(String key, byte[] value, boolean encrypt) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setValue(String key, byte[] value, boolean encrypt) {
        return null;
    }

    @Override
    public CompletableFuture<ArrayList<byte[]>> getValue(String key, boolean decrypt) {
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
