package org.elastos.hive.vendor.onedrive;

import org.elastos.hive.Authenticator;
import org.elastos.hive.Callback;
import org.elastos.hive.Client;
import org.elastos.hive.NullCallback;
import org.elastos.hive.exception.HiveException;
import org.elastos.hive.interfaces.Files;
import org.elastos.hive.interfaces.IPFS;
import org.elastos.hive.interfaces.KeyValues;
import org.elastos.hive.utils.DigitalUtil;
import org.elastos.hive.utils.ResponseHelper;
import org.elastos.hive.vendor.connection.ConnectionManager;
import org.elastos.hive.vendor.onedrive.network.OneDriveApi;
import org.elastos.hive.vendor.onedrive.network.model.DirChildrenResponse;
import org.elastos.hive.vendor.onedrive.network.model.FileOrDirPropResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Response;

final class OneDriveClient extends Client implements Files, KeyValues {
    private Authenticator authenticator;
    private OneDriveAuthHelper authHelper;
    private String rootPath = "/Files";

    OneDriveClient(Client.Options options) {
        OneDriveOptions oneDriveOptions = (OneDriveOptions) options;
        authHelper = new OneDriveAuthHelper(oneDriveOptions.clientId(),
                OneDriveConstance.appScope, oneDriveOptions.redirectUrl(), oneDriveOptions.storePath());
        authenticator = oneDriveOptions.authenticator();
    }

    @Override
    public void connect() throws HiveException {
        try {
            authHelper.loginAsync(authenticator).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new HiveException(e.getLocalizedMessage());
        }
    }

    @Override
    public void disconnect() {
        authHelper.dissConnect();
    }

    @Override
    public boolean isConnected() {
        return authHelper.getConnectState();
    }

    @Override
    public Files getFiles() {
        return this;
    }

    @Override
    public IPFS getIPFS() {
        return null;
    }

    @Override
    public KeyValues getKeyValues() {
        return this;
    }

    @Override
    public CompletableFuture<Void> put(byte[] from, String remoteFile) {
        return put(from, remoteFile, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Void> put(byte[] data, String remoteFile, Callback<Void> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doPutBuffer(creatDestFilePath(remoteFile), data, callback));
    }

    @Override
    public CompletableFuture<Void> put(String data, String remoteFile) {
        return put(data, remoteFile, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Void> put(String data, String remoteFile, Callback<Void> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doPutBuffer(creatDestFilePath(remoteFile), data.getBytes(), callback));
    }

    @Override
    public CompletableFuture<Void> put(InputStream input, String remoteFile) {
        return put(input, remoteFile, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Void> put(InputStream input, String remoteFile, Callback<Void> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doPutInputStream(creatDestFilePath(remoteFile), input, callback));
    }

    @Override
    public CompletableFuture<Void> put(Reader reader, String remoteFile) {
        return put(reader, remoteFile, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Void> put(Reader reader, String remoteFile, Callback<Void> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doPutReader(creatDestFilePath(remoteFile), reader, callback));
    }

    @Override
    public CompletableFuture<Long> size(String remoteFile) {
        return size(remoteFile, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Long> size(String remoteFile, Callback<Long> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doGetFileLength(creatDestFilePath(remoteFile), callback));
    }

    @Override
    public CompletableFuture<String> getAsString(String remoteFile) {
        return getAsString(remoteFile, new NullCallback<>());
    }

    @Override
    public CompletableFuture<String> getAsString(String remoteFile, Callback<String> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doGetString(creatDestFilePath(remoteFile), callback));
    }

    @Override
    public CompletableFuture<byte[]> getAsBuffer(String remoteFile) {
        return getAsBuffer(remoteFile, new NullCallback<>());
    }

    @Override
    public CompletableFuture<byte[]> getAsBuffer(String remoteFile, Callback<byte[]> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doGetBuffer(creatDestFilePath(remoteFile), callback));
    }


    @Override
    public CompletableFuture<Void> delete(String remoteFile) {
        return delete(remoteFile, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Void> delete(String remoteFile, Callback<Void> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doDeleteFile(creatDestFilePath(remoteFile), callback));
    }

    @Override
    public CompletableFuture<ArrayList<String>> list() {
        return list(new NullCallback<>());
    }

    @Override
    public CompletableFuture<ArrayList<String>> list(Callback<ArrayList<String>> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doListFile(callback));
    }

    @Override
    public CompletableFuture<Long> get(String remoteFile, OutputStream output) {
        return get(remoteFile, output, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Long> get(String remoteFile, OutputStream output, Callback<Long> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doWriteToOutput(creatDestFilePath(remoteFile), output, callback));
    }

    @Override
    public CompletableFuture<Long> get(String remoteFile, Writer writer) {
        return get(remoteFile, writer, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Long> get(String remoteFile, Writer writer, Callback<Long> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doWriteToWriter(creatDestFilePath(remoteFile), writer, callback));
    }

    @Override
    public CompletableFuture<Void> putValue(String key, String value) {
        return putValue(key, value, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Void> putValue(String key, String value, Callback<Void> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doPutValue(creatDestKeyPath(key), value.getBytes(), callback));
    }

    @Override
    public CompletableFuture<Void> putValue(String key, byte[] value) {
        return putValue(key, value, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Void> putValue(String key, byte[] value, Callback<Void> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doPutValue(creatDestKeyPath(key), value, callback));
    }

    @Override
    public CompletableFuture<Void> setValue(String key, String value) {
        return setValue(key, value, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Void> setValue(String key, String value, Callback<Void> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doSetValue(creatDestKeyPath(key), value.getBytes(), callback));
    }

    @Override
    public CompletableFuture<Void> setValue(String key, byte[] value) {
        return setValue(key, value, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Void> setValue(String key, byte[] value, Callback<Void> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doSetValue(creatDestKeyPath(key), value, callback));

    }

    @Override
    public CompletableFuture<ArrayList<byte[]>> getValues(String key) {
        return getValues(key, new NullCallback<>());
    }

    @Override
    public CompletableFuture<ArrayList<byte[]>> getValues(String key, Callback<ArrayList<byte[]>> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doGetValue(creatDestKeyPath(key), callback));
    }

    @Override
    public CompletableFuture<Void> deleteKey(String key) {
        return deleteKey(key, new NullCallback<>());
    }

    @Override
    public CompletableFuture<Void> deleteKey(String key, Callback<Void> callback) {
        return authHelper.checkExpired()
                .thenCompose(result -> doDeleteFile(creatDestKeyPath(key), callback));
    }

////

    private CompletableFuture<ArrayList<byte[]>> doGetValue(String key, Callback<ArrayList<byte[]>> callback) {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<byte[]> list = null;
            try {
                list = getValueImpl(key);
                callback.onSuccess(list);
            } catch (HiveException e) {
                e.printStackTrace();
                callback.onError(new HiveException(e.getLocalizedMessage()));
            }
            return list;
        });
    }

    private ArrayList<byte[]> getValueImpl(String key) throws HiveException {
        if (key == null || key.equals(""))
            throw new HiveException("Key is null");

        ArrayList<byte[]> arrayList = new ArrayList<>();
        byte[] data = getBufferImpl(key);
        createValueResult(arrayList, data);
        return arrayList;
    }

    private String creatDestFilePath(String destFileName) {
        return rootPath + "/" + destFileName;
    }

    private String creatDestKeyPath(String key) {
        return "/KeyValues/" + key;
    }

    private CompletableFuture<Void> doPutReader(String remoteFile, Reader reader, Callback<Void> callback) {
        return CompletableFuture.runAsync(() -> {
            try {
                writeReader(remoteFile, reader);
                callback.onSuccess(null);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(new HiveException(e.getLocalizedMessage()));
            }
        });
    }

    private void writeReader(String remoteFile, Reader reader) throws Exception {
        if (reader == null) {
            throw new HiveException("Reader is null");
        }
        RequestBody requestBody = createWriteRequestBody(reader);
        Response response = ConnectionManager.getOnedriveApi()
                .write(remoteFile, requestBody)
                .execute();
        int checkResponseCode = checkResponseCode(response);
        if (checkResponseCode == 404) {
            throw new HiveException(HiveException.ITEM_NOT_FOUND);
        } else if (checkResponseCode != 0) {
            throw new HiveException(HiveException.ERROR);
        }
    }


    private CompletableFuture<Void> doPutInputStream(String destFilePath, InputStream inputStream, Callback<Void> callback) {
        return CompletableFuture.runAsync(() -> {
            try {
                writeInputStream(destFilePath, inputStream);
                callback.onSuccess(null);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(new HiveException(e.getLocalizedMessage()));
            }
        });
    }

    private void writeInputStream(String destFilePath, InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new HiveException("InputStream is null");
        }
        RequestBody requestBody = createWriteRequestBody(inputStream);
        Response response = ConnectionManager.getOnedriveApi()
                .write(destFilePath, requestBody)
                .execute();
        int checkResponseCode = checkResponseCode(response);
        if (checkResponseCode == 404) {
            throw new HiveException(HiveException.ITEM_NOT_FOUND);
        } else if (checkResponseCode != 0) {
            throw new HiveException(HiveException.ERROR);
        }
    }


    private CompletableFuture<Void> doPutBuffer(String destFilePath, byte[] data, org.elastos.hive.Callback<Void> callback) {
        return CompletableFuture.runAsync(() -> {
            try {
                writeBuffer(destFilePath, data);
                callback.onSuccess(null);
            } catch (Exception e) {
                e.printStackTrace();
                HiveException exception = new HiveException(e.getLocalizedMessage());
                callback.onError(exception);
            }
        });
    }

    private void writeBuffer(String remoteFile, byte[] data) throws Exception {
        if (remoteFile == null || remoteFile.equals(""))
            throw new HiveException("RemoteFile is null");

        if (data == null)
            throw new HiveException("Data is null");

        RequestBody requestBody = createWriteRequestBody(data);
        Response response = ConnectionManager.getOnedriveApi()
                .write(remoteFile, requestBody)
                .execute();
        if (response == null)
            throw new HiveException(HiveException.ERROR);

        int checkResponseCode = checkResponseCode(response);
        if (checkResponseCode == 404) {
            throw new HiveException(HiveException.ITEM_NOT_FOUND);
        } else if (checkResponseCode != 0) {
            throw new HiveException(HiveException.ERROR);
        }
    }

    private RequestBody createWriteRequestBody(byte[] data) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), data);
    }

    private RequestBody createWriteRequestBody(InputStream inputStream) throws IOException {
        Buffer buffer = new Buffer();
        byte[] cache = new byte[1024];
        int len;
        while ((len = inputStream.read(cache)) != -1) {
            buffer.write(cache, 0, len);
        }
        return createWriteRequestBody(buffer.readByteArray());
    }

    private RequestBody createWriteRequestBody(Reader reader) throws IOException {
        return createWriteRequestBody(transReader(reader).toString().getBytes());
    }

    private StringBuffer transReader(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        boolean flag = false;
        while ((line = bufferedReader.readLine()) != null) {
            if (!flag) {
                flag = true;
                stringBuffer.append(line);
            } else {
                stringBuffer.append("\n");
                stringBuffer.append(line);
            }

        }
        reader.close();
        return stringBuffer;
    }

    private CompletableFuture<Long> doGetFileLength(String destFilePath, Callback<Long> callback) {
        return CompletableFuture.supplyAsync(() -> {
            long length = 0;
            try {
                length = getLength(destFilePath);
                callback.onSuccess(length);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(new HiveException(e.getLocalizedMessage()));
            }
            return length;
        });
    }

    private Long getLength(String remoteFile) throws Exception {
        if (remoteFile == null || remoteFile.equals(""))
            throw new HiveException("RemoteFile is null");

        Response<FileOrDirPropResponse> response = requestFileInfo(remoteFile);
        int checkResponseCode = checkResponseCode(response);
        if (checkResponseCode == 0) {
            return decodeFileLength(response);
        } else if (checkResponseCode == 404) {
            throw new HiveException(HiveException.ITEM_NOT_FOUND);
        } else {
            throw new HiveException(HiveException.GET_FILE_LENGTH_ERROR);
        }
    }

    private CompletableFuture<Void> doDeleteFile(String destFilePath, Callback<Void> callback) {
        return CompletableFuture.runAsync(() -> {
            try {
                deleteFileImpl(destFilePath);
                callback.onSuccess(null);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(new HiveException(e.getLocalizedMessage()));
            }
        });
    }

    private void deleteFileImpl(String remoteFile) throws Exception {
        if (remoteFile == null || remoteFile.equals(""))
            throw new HiveException("RemoteFile is null");

        Response<org.elastos.hive.vendor.connection.model.NoBodyEntity> response = ConnectionManager.getOnedriveApi()
                .deleteItem(remoteFile)
                .execute();
        int checkResponseCode = checkResponseCode(response);
        if (checkResponseCode == 404) {
            throw new HiveException(HiveException.ITEM_NOT_FOUND);
        } else if (checkResponseCode != 0) {
            throw new HiveException(HiveException.ERROR);
        }
    }

    private CompletableFuture<byte[]> doGetBuffer(String remoteFile, Callback<byte[]> callback) {
        return CompletableFuture.supplyAsync(() -> {
            byte[] bytes = new byte[0];
            try {
                bytes = getBufferImpl(remoteFile);
                callback.onSuccess(bytes);
            } catch (HiveException e) {
                e.printStackTrace();
                callback.onError(e);
            }
            return bytes;
        });
    }

    private byte[] getBufferImpl(String remoteFile) throws HiveException {
        if (remoteFile == null || remoteFile.equals(""))
            throw new HiveException("RemoteFile is null");

        Response response = getFileOrBuffer(remoteFile);

        int checkResponseCode = checkResponseCode(response);
        if (checkResponseCode == 404) {
            throw new HiveException(HiveException.ITEM_NOT_FOUND);
        } else if (checkResponseCode != 0) {
            throw new HiveException(HiveException.ERROR);
        }

        byte[] bytes = ResponseHelper.getBuffer(response);
        if (bytes == null)
            return new byte[0];

        return bytes;
    }

    private CompletableFuture<String> doGetString(String remoteFile, Callback<String> callback) {
        return CompletableFuture.supplyAsync(() -> {
            String result = "";
            try {
                result = getString(remoteFile);
                callback.onSuccess(result);
            } catch (HiveException e) {
                e.printStackTrace();
                callback.onError(e);
            }
            return result;
        });
    }

    private String getString(String remoteFile) throws HiveException {
        byte[] bytes = getBufferImpl(remoteFile);
        if (bytes == null || bytes.length == 0)
            return "";

        return new String(bytes);
    }

    private CompletableFuture<Long> doWriteToOutput(String remoteFile, OutputStream outputStream, Callback<Long> callback) {
        return CompletableFuture.supplyAsync(() -> {
            long length = 0;
            try {
                length = writeToOutput(remoteFile, outputStream);
                callback.onSuccess(length);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(new HiveException(e.getLocalizedMessage()));
            }
            return length;
        });
    }

    private long writeToOutput(String remoteFile, OutputStream outputStream) throws Exception {
        if (remoteFile == null || remoteFile.equals(""))
            throw new HiveException("RemoteFile is null");

        if (outputStream == null)
            throw new HiveException("OutputStream is null");

        Response response = getFileOrBuffer(remoteFile);
        if (response == null)
            throw new HiveException(HiveException.ERROR);

        int checkResponseCode = checkResponseCode(response);
        if (checkResponseCode == 404) {
            throw new HiveException(HiveException.ITEM_NOT_FOUND);
        } else if (checkResponseCode != 0) {
            throw new HiveException(HiveException.ERROR);
        }

        return ResponseHelper.writeOutput(response, outputStream);
    }

    private CompletableFuture<Long> doWriteToWriter(String remoteFile, Writer writer, Callback<Long> callback) {
        return CompletableFuture.supplyAsync(() -> {
            long length = 0;
            try {
                length = writeToWriter(remoteFile, writer);
                callback.onSuccess(length);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(new HiveException(e.getLocalizedMessage()));
            }
            return length;
        });
    }

    private long writeToWriter(String remoteFile, Writer writer) throws Exception {
        if (remoteFile == null || remoteFile.equals(""))
            throw new HiveException("RemoteFile is null");

        if (writer == null)
            throw new HiveException("Writer is null");

        Response response = getFileOrBuffer(remoteFile);

        if (response == null)
            throw new HiveException(HiveException.ERROR);

        int checkResponseCode = checkResponseCode(response);
        if (checkResponseCode == 404) {
            throw new HiveException(HiveException.ITEM_NOT_FOUND);
        } else if (checkResponseCode != 0) {
            throw new HiveException(HiveException.ERROR);
        }

        return ResponseHelper.writeDataToWriter(response, writer);
    }

    private Response getFileOrBuffer(String destFilePath) throws HiveException {
        Response<okhttp3.ResponseBody> response;
        try {
            response = ConnectionManager.getOnedriveApi()
                    .read("identity", destFilePath)
                    .execute();

        } catch (Exception ex) {
            throw new HiveException(ex.getMessage());
        }
        return response;
    }

    private CompletableFuture<ArrayList<String>> doListFile(Callback<ArrayList<String>> callback) {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<String> list = null;
            try {
                list = listFile();
                callback.onSuccess(list);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(new HiveException(e.getLocalizedMessage()));
            }
            return list;
        });
    }

    private ArrayList<String> listFile() throws Exception {
        OneDriveApi api = ConnectionManager.getOnedriveApi();
        Response<DirChildrenResponse> response = api.getChildren(rootPath).execute();

        int checkResponseCode = checkResponseCode(response);
        if (checkResponseCode == 404) {
            throw new HiveException(HiveException.ITEM_NOT_FOUND);
        } else if (checkResponseCode != 0) {
            throw new HiveException(HiveException.ERROR);
        }

        return decodeListFile(response);
    }

    private CompletableFuture<Void> doPutValue(String key, byte[] value, Callback<Void> callback) {
        return CompletableFuture.runAsync(() -> {
            try {
                putValueImpl(key, value);
                callback.onSuccess(null);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(new HiveException(e.getLocalizedMessage()));
            }
        });
    }

    private void putValueImpl(String key, byte[] value) throws Exception {
        byte[] finalData = mergeData(key, value);
        writeBuffer(key, finalData);
    }

    private byte[] mergeData(String key, byte[] value) {
        byte[] originData = new byte[0];
        try {
            originData = getBufferImpl(key);
        } catch (HiveException e) {
            e.printStackTrace();
        }
        byte[] data = mergeLengthAndData(value);
        return mergeData(originData, data);
    }


    private ArrayList<String> decodeListFile(Response<DirChildrenResponse> response) {
        ArrayList<String> files = new ArrayList<>();
        DirChildrenResponse dirChildrenResponse = response.body();
        List<DirChildrenResponse.ValueBean> list = dirChildrenResponse != null ? dirChildrenResponse.getValue() : null;
        if (list == null) {
            files.add("");
            return files;
        }
        for (DirChildrenResponse.ValueBean fileinfo : list) {
            files.add(fileinfo.getName());
        }
        return files;
    }

    private long decodeFileLength(Response<FileOrDirPropResponse> response) {
        FileOrDirPropResponse info = response.body();
        return info != null ? info.getSize() : 0;
    }

    private CompletableFuture<Void> doSetValue(String remoteFile, byte[] data, Callback<Void> callback) {
        return CompletableFuture.runAsync(() -> {
            try {
                deleteFileImpl(remoteFile);
                writeBuffer(remoteFile, mergeLengthAndData(data));
                callback.onSuccess(null);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(new HiveException(e.getLocalizedMessage()));
            }
        });
    }

    private byte[] mergeLengthAndData(byte[] data) {
        byte[] lengthByte = DigitalUtil.intToByteArray(data.length);
        return mergeData(lengthByte, data);
    }

    private byte[] mergeData(byte[] bytes1, byte[] bytes2) {
        byte[] tmp = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, tmp, 0, bytes1.length);
        System.arraycopy(bytes2, 0, tmp, bytes1.length, bytes2.length);
        return tmp;
    }

    private byte[] spliteBytes(byte[] data, int length) {
        byte[] tmp = new byte[length];
        System.arraycopy(data, 4, tmp, 0, length);

        return tmp;
    }

    private int calcuLength(byte[] data) {
        byte[] lengthByte = new byte[4];

        System.arraycopy(data, 0, lengthByte, 0, 4);

        return DigitalUtil.byteArrayToInt(lengthByte);
    }

    private void createValueResult(ArrayList<byte[]> arrayList, byte[] data) throws HiveException {
        if (data == null)
            throw new HiveException("Data is null");

        if (arrayList == null)
            throw new HiveException("List is null");

        int total = data.length;
        int dataLength = calcuLength(data);
        byte[] strbytes = spliteBytes(data, dataLength);
        arrayList.add(strbytes);
        int remainingDataLength = total - (dataLength + 4);
        if (remainingDataLength <= 0)
            return;

        byte[] remainingData = new byte[remainingDataLength];
        System.arraycopy(data, dataLength + 4, remainingData, 0, remainingDataLength);
        createValueResult(arrayList, remainingData);
    }

    private Response<FileOrDirPropResponse> requestFileInfo(String filename) throws Exception {
        return ConnectionManager.getOnedriveApi()
                .getDirAndFileInfo(filename)
                .execute();
    }

    private int checkResponseCode(Response response) {
        if (response == null)
            return -1;

        int code = response.code();

        if (code < 300 && code >= 200)
            return 0;

        return code;
    }
}