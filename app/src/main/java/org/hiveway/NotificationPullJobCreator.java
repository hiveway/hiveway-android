/*
 * Copyright 2018 Hiveway
 * Copyright 2017 Andrew Dawson
 *
 * This file is a part of Tusky and Hiveway.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tusky is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tusky; if not,
 * see <http://www.gnu.org/licenses>. */


package org.hiveway;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hiveway.db.AccountEntity;
import org.hiveway.entity.Notification;
import org.hiveway.json.SpannedTypeAdapter;
import org.hiveway.network.HivewayApi;
import org.hiveway.util.NotificationHelper;
import org.hiveway.util.OkHttpUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by charlag on 31/10/17.
 */

public final class NotificationPullJobCreator implements JobCreator {

    private static final String TAG = "NotificationPJC";

    static final String NOTIFICATIONS_JOB_TAG = "notifications_job_tag";

    private Context context;

    NotificationPullJobCreator(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        if (tag.equals(NOTIFICATIONS_JOB_TAG)) {
            return new NotificationPullJob(context);
        }
        return null;
    }

    private static HivewayApi createHivewayApi(String domain, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        OkHttpClient okHttpClient = OkHttpUtils.getCompatibleClientBuilder(preferences)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Spanned.class, new SpannedTypeAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + domain)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(HivewayApi.class);
    }

    private final static class NotificationPullJob extends Job {

        private Context context;

        NotificationPullJob(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        protected Result onRunJob(@NonNull Params params) {

            List<AccountEntity> accountList = new ArrayList<>(HivewayApplication.getAccountManager().getAllAccountsOrderedByActive());

            for(AccountEntity account: accountList) {

                if(account.getNotificationsEnabled()) {
                    HivewayApi api = createHivewayApi(account.getDomain(), context);
                    try {
                        Log.d(TAG, "getting Notifications for "+account.getFullName());
                        Response<List<Notification>> notifications =
                                api.notificationsWithAuth(String.format("Bearer %s", account.getAccessToken())).execute();
                        if (notifications.isSuccessful()) {
                            onNotificationsReceived(account, notifications.body());
                        } else {
                            Log.w(TAG, "error receiving notificationsEnabled");
                        }
                    } catch (IOException e) {
                        Log.w(TAG, "error receiving notificationsEnabled", e);
                    }
                }

            }

            return Result.SUCCESS;


        }

        private void onNotificationsReceived(AccountEntity account, List<Notification> notificationList) {

            Collections.reverse(notificationList);

            BigInteger newId = new BigInteger(account.getLastNotificationId());

            BigInteger newestId = BigInteger.ZERO;

            for(Notification notification: notificationList){

                BigInteger currentId = new BigInteger(notification.getId());

                if(isBiggerThan(currentId, newestId)) {
                    newestId = currentId;
                }

                if (isBiggerThan(currentId, newId)) {
                    NotificationHelper.make(context, notification, account);
                }
            }

            account.setLastNotificationId(newestId.toString());
            HivewayApplication.getAccountManager().saveAccount(account);

        }

        private boolean isBiggerThan(BigInteger newId, BigInteger lastShownNotificationId) {

            return lastShownNotificationId.compareTo(newId) == - 1;
        }
    }
}
