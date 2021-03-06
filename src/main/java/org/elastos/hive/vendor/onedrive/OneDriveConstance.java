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

package org.elastos.hive.vendor.onedrive;

class OneDriveConstance {
    static final String APP_SCOPE = "Files.ReadWrite.AppFolder offline_access";

    static final String ONE_DRIVE_AUTH_URL = "https://login.microsoftonline.com/common/oauth2/v2.0" ;
    static final String ONE_DRIVE_AUTH_BASE_URL = ONE_DRIVE_AUTH_URL+"/";
    static final String ONE_DRIVE_API_BASE_URL  = "https://graph.microsoft.com/v1.0/me/drive/";

    static final String AUTHORIZE = "authorize";


    static final String DEFAULT_REDIRECT_URL = "localhost";
    static final int    DEFAULT_REDIRECT_PORT = 12345;

    static final String GRANT_TYPE_GET_TOKEN = "authorization_code";
    static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    static final String CONFIG = "onedrive.json";

    static final String FILES_ROOT_PATH  = "/Files";
    static final String KEYVALUES_ROOT_PATH = "/KeyValues";
}
