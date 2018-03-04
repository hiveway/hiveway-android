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

package org.hiveway.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hiveway.R;
import org.hiveway.entity.Account;
import org.hiveway.interfaces.AccountActionListener;
import org.hiveway.interfaces.LinkListener;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.hiveway.entity.Account;
import org.hiveway.interfaces.AccountActionListener;
import org.hiveway.interfaces.LinkListener;

class AccountViewHolder extends RecyclerView.ViewHolder {
    private View container;
    private TextView username;
    private TextView displayName;
    private CircularImageView avatar;
    private String accountId;

    AccountViewHolder(View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.account_container);
        username = itemView.findViewById(R.id.account_username);
        displayName = itemView.findViewById(R.id.account_display_name);
        avatar = itemView.findViewById(R.id.account_avatar);
    }

    void setupWithAccount(Account account) {
        accountId = account.getId();
        String format = username.getContext().getString(R.string.status_username_format);
        String formattedUsername = String.format(format, account.getUsername());
        username.setText(formattedUsername);
        displayName.setText(account.getName());
        Context context = avatar.getContext();
        Picasso.with(context)
                .load(account.getAvatar())
                .placeholder(R.drawable.avatar_default)
                .into(avatar);
    }

    void setupActionListener(final AccountActionListener listener) {
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewAccount(accountId);
            }
        });
    }

    void setupLinkListener(final LinkListener listener) {
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewAccount(accountId);
            }
        });
    }
}