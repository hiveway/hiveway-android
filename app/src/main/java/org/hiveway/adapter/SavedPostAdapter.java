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
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.hiveway.R;
import org.hiveway.db.PostEntity;

import java.util.ArrayList;
import java.util.List;

public class SavedPostAdapter extends RecyclerView.Adapter {
    private List<PostEntity> list;
    private PostAction handler;

    public SavedPostAdapter(Context context) {
        super();
        list = new ArrayList<>();
        handler = (PostAction) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        PostViewHolder holder = (PostViewHolder) viewHolder;
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(List<PostEntity> newPost) {
        list = new ArrayList<>();
        list.addAll(newPost);
    }

    public void addItems(List<PostEntity> newPost) {
        int end = list.size();
        list.addAll(newPost);
        notifyItemRangeInserted(end, newPost.size());
    }

    @Nullable
    public PostEntity removeItem(int position) {
        if (position < 0 || position >= list.size()) {
            return null;
        }
        PostEntity post = list.remove(position);
        notifyItemRemoved(position);
        return post;
    }

    private PostEntity getItem(int position) {
        if (position >= 0 && position < list.size()) {
            return list.get(position);
        }
        return null;
    }

    // handler saved post
    public interface PostAction {
        void delete(int position, PostEntity item);

        void click(int position, PostEntity item);
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView content;
        ImageButton suppr;

        PostViewHolder(View view) {
            super(view);
            this.view = view;
            this.content = view.findViewById(R.id.content);
            this.suppr = view.findViewById(R.id.suppr);
        }

        void bind(final PostEntity item) {
            suppr.setEnabled(true);

            if (item != null) {
                content.setText(item.getText());

                suppr.setOnClickListener(v -> {
                    v.setEnabled(false);
                    handler.delete(getAdapterPosition(), item);
                });
                view.setOnClickListener(v -> handler.click(getAdapterPosition(), item));
            }
        }
    }
}
