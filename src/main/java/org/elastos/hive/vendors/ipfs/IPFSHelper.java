package org.elastos.hive.vendors.ipfs;

import java.util.concurrent.CompletableFuture;

import org.elastos.hive.Callback;
import org.elastos.hive.NullCallback;
import org.elastos.hive.Status;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

class IPFSHelper {
	private final IPFSEntry ipfsEntry;
	private boolean isValid = false;
	private String BASEURL  = null;
	private String validAddress;

	IPFSHelper(IPFSEntry ipfsEntry) {
		this.ipfsEntry = ipfsEntry;
	}

	IPFSEntry getIpfsEntry() {
		return ipfsEntry;
	}

	CompletableFuture<Status> loginAsync() {
		return loginAsync(new NullCallback<Status>());
	}

	CompletableFuture<Status> loginAsync(Callback<Status> callback) {
		return checkValid(callback);
	}

	void logout() {
		isValid = false;
	}

	String getBaseUrl() {
		return BASEURL;
	}

	void setStatus(boolean invalid) {
		isValid = invalid;
	}

	void setValidAddress(String validAddress) {
		this.validAddress = validAddress;
	}

	CompletableFuture<Status> checkValid() {
		return checkValid(new NullCallback<Status>());
	}

	CompletableFuture<Status> checkValid(Callback<Status> callback) {
		if (isValid) {
			CompletableFuture<Status> future = new CompletableFuture<Status>();
			Status status = new Status(1);
		    callback.onSuccess(status);
		    future.complete(status);
			return future;
		}

		//get home hash and login
		CompletableFuture<Status> future = CompletableFuture.supplyAsync(() -> {
			Status status = null;
			try {
				String homeHash = null;
				//Using the older validAddress try to get the home hash.
				if (validAddress != null && !validAddress.isEmpty()) {
					String url = String.format(IPFSURL.URLFORMAT, validAddress);
					homeHash = getHomeHash(url);
					BASEURL = url;
					if (homeHash == null) {
						validAddress = null;
					}
				}

				if (homeHash == null) {
					String[] addrs = ipfsEntry.getRpcIPAddrs();
					for (int i = 0; i < addrs.length; i++) {
						String url = String.format(IPFSURL.URLFORMAT, addrs[i]);
						homeHash = getHomeHash(url);
						if (homeHash != null && !homeHash.isEmpty()) {
							BASEURL = url;
							validAddress = addrs[i];
							break;
						}
					}
				}

				if (homeHash == null) {
					status = new Status(0);
				    callback.onSuccess(status);
					return status;
				}

				Unirest.get(BASEURL + IPFSMethod.LOGIN)
					.header(IPFSURL.ContentType, IPFSURL.Json)
					.queryString(IPFSURL.UID, ipfsEntry.getUid())
					.queryString(IPFSURL.HASH, homeHash)
					.asJson();

				isValid = true;
			} catch (Exception e) {
				e.printStackTrace();
			}

			status = new Status(0);
		    callback.onSuccess(status);
			return status;
		});

		return future;
	}

	private String getHomeHash(String baseUrl) {
		String url = baseUrl + IPFSMethod.NAMERESOLVE;
		try {
			HttpResponse<JsonNode> json = Unirest.get(url)
					.header(IPFSURL.ContentType, IPFSURL.Json)
					.asJson();
			return json.getBody().getObject().getString("Path");
		} catch (UnirestException e) {
			e.printStackTrace();
		}

		return null;
	}
}