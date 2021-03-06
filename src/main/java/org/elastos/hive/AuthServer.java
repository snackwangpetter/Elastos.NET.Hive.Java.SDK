/*
 * Copyright (c) 2019 Elastos Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.elastos.hive;

import java.util.concurrent.Semaphore;

import fi.iki.elonen.NanoHTTPD;

public final class AuthServer extends NanoHTTPD {
    private Semaphore authLock;
    private String authCode = "";

    public AuthServer(Semaphore semaphore, String host, int port) {
        this(host, port);
        authLock = semaphore;
        try {
            authLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private AuthServer(String host, int port) {
        super(host, port);
    }

    @Override
    public Response serve(IHTTPSession session) {

        String[] query = session.getQueryParameterString().split("=");

        switch (query[0]) {
            case "code":
                authCode = query[1];
                break;
            case "error":
            default:
                break;
        }

        if (authLock != null) {
            authLock.release();
        }
        String builder = "<!DOCTYPE html><html><body></body></html>\n";
        return newFixedLengthResponse(builder);
    }

    public String getAuthCode() {
        return authCode;
    }
}
