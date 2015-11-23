package com.example.vipul.splash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vipul.splash.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder>{
    private static final int HEADER_TYPE = 0;
    private static final int ROW_TYPE = 1;

    private List<String> rows;
    private int icons[]=new int[7];
    private Typeface font;

    public DrawerAdapter(List<String> rowss, int[] iconns,Typeface font) {
//        this.rows = rows;
//        this.icons=icons;
        icons[0]=R.string.friends;
        icons[1]=R.string.icon_heart;
        icons[2]=R.string.map;
        icons[3]=R.string.invi;
        icons[4]=R.string.faceb;
        icons[5]=R.string.friends;
        icons[6]=R.string.emer;
        rows = new ArrayList<>();
        rows.add("Find Friends");
        rows.add("Events");
        rows.add("Map Me");
        rows.add("Invites");
        rows.add("Facebook");
        rows.add("Group");
        rows.add("Emergency Contact");
        this.font = font;
    }
    public DrawerAdapter(List<String> rows) {
        this.rows = rows;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            return new ViewHolder(view, viewType);
        } else if (viewType == ROW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);
            return new ViewHolder(view, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == ROW_TYPE) {
            String rowText = rows.get(position - 1);
            holder.textView.setText(rowText);
            holder.imageView.setText(icons[position-1]);
            holder.imageView.setTypeface(font);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_TYPE;
        return ROW_TYPE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected int viewType;
        private Context context;

        @InjectView(R.id.drawer_row_icon) TextView imageView;
        @InjectView(R.id.drawer_row_text) TextView textView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            this.viewType = viewType;

            if (viewType == ROW_TYPE) {
                ButterKnife.inject(this, itemView);
            }
        }
    }
}
