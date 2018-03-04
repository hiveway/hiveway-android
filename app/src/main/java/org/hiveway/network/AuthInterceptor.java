package org.hiveway.network;

import android.support.annotation.NonNull;

import org.hiveway.HivewayApplication;
import org.hiveway.db.AccountEntity;

import org.hiveway.HivewayApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by charlag on 31/10/17.
 */

public final class AuthInterceptor implements Interceptor {

    public AuthInterceptor() { }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        AccountEntity currentAccount = HivewayApplication.getAccountManager().getActiveAccount();

        Request originalRequest = chain.request();

        Request.Builder builder = originalRequest.newBuilder();
        if (currentAccount != null) {
            builder.header("Authorization", String.format("Bearer %s", currentAccount.getAccessToken()));
        }
        Request newRequest = builder.build();

        return chain.proceed(newRequest);
    }

}
