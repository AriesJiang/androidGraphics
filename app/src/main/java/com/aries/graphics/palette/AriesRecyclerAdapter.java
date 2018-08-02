/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aries.graphics.palette;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aries.graphics.R;

public class AriesRecyclerAdapter extends RecyclerView.Adapter<AriesRecyclerAdapter.ItemViewHolder>{

    private int[] mColorsArray;
    private int[] mColorsPopulation;
    private int[] mTextColorsArray;

    public AriesRecyclerAdapter(Context context) {
    }

    public void reFreshColor(int[] colorsArray, int[] colorsPopulation, int[] textColorsArray) {
        if ((colorsArray != null) && (colorsArray.length > 0)) {
            mColorsArray = colorsArray;
            mColorsPopulation = colorsPopulation;
            mTextColorsArray = textColorsArray;
            notifyDataSetChanged();
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_real, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        int color = mColorsArray[position];
        holder.textViewPopulation.setText(Integer.toString(mColorsPopulation[position]));
        holder.textView.setText(Integer.toHexString(color));
        holder.textViewPopulation.setTextColor(mTextColorsArray[position]);
        holder.textView.setTextColor(mTextColorsArray[position]);
        holder.bgView.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return mColorsArray == null ? 0 : mColorsArray.length;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView textViewPopulation;
        public final TextView textView;
        public final View bgView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            textViewPopulation = itemView.findViewById(R.id.textPopulation);
            bgView = itemView.findViewById(R.id.bg);
        }
    }
}
