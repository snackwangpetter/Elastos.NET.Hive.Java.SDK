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

package org.elastos.hive.vendor.onedrive.network;

import org.elastos.hive.vendor.onedrive.network.model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthApi {
    @FormUrlEncoded
    @POST(ConnectConstance.TOKEN)
    Call<TokenResponse> getToken(@Field(ConnectConstance.CLIENT_ID) String clientId,
                                 @Field(ConnectConstance.CODE) String code,
                                 @Field(ConnectConstance.REDIRECT_URL) String redirectUrl,
                                 @Field(ConnectConstance.GRANT_TYPE) String grantType);

    @FormUrlEncoded
    @POST(ConnectConstance.TOKEN)
    Call<TokenResponse> refreshToken(@Field(ConnectConstance.CLIENT_ID) String clientId,
                                     @Field(ConnectConstance.REDIRECT_URL) String redirectUrl,
                                     @Field(ConnectConstance.REFRESH_TOKEN) String refreshToken,
                                     @Field(ConnectConstance.GRANT_TYPE) String grantType);
}
