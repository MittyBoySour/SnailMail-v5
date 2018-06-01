package com.mad.snailmail_v5.MailList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.snailmail_v5.R;

import java.util.ArrayList;

import com.mad.snailmail_v5.Model.Mail;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MailViewHolder> {

    public class MailViewHolder extends RecyclerView.ViewHolder {

        public TextView senderTV;
        public TextView titleTv;
        // make separate views for status

        public MailViewHolder(View view) {
            super(view);
            senderTV = (TextView) view.findViewById(R.id.mail_sender);
            titleTv = (TextView) view.findViewById(R.id.mail_title);
        }
    }

    private ArrayList<Mail> mMailList;

    public MailAdapter(ArrayList<Mail> mailList) {
        mMailList = mailList;
    }

    @NonNull
    @Override
    public MailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MailViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MailViewHolder holder, int position) {
        holder.senderTV.setText(mMailList.get(position).getSender());
        holder.titleTv.setText(mMailList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mMailList.size();
    }
}
