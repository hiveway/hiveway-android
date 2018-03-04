/*
 *  Copyright 2018 Hiveway
 *  Copyright 2018 Conny Duck
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

package org.hiveway.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import org.hiveway.HivewayApplication
import org.hiveway.util.NotificationHelper

class NotificationClearBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val accountId = intent.getLongExtra(org.hiveway.util.NotificationHelper.ACCOUNT_ID, -1)

        val accountManager = org.hiveway.HivewayApplication.getAccountManager()
        val account = accountManager.getAccountById(accountId)
        if (account != null) {
            account.activeNotifications = "[]"
            accountManager.saveAccount(account)
        }
    }

}
