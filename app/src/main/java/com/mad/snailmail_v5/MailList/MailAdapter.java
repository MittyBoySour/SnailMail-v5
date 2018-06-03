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

    private static MailListPresenter mMailListPresenter;

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

    public MailAdapter(ArrayList<Mail> mailList, MailListPresenter mailListPresenter) {
        mMailList = mailList;
        mMailListPresenter = mailListPresenter;
    }

    @NonNull
    @Override
    public MailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MailViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MailViewHolder holder, int position) {
        Mail mail = mMailList.get(position);

        if (!mail.isDelivered()) {
            holder.senderTV.setText(R.string.mystery_sender);
            holder.titleTv.setText(R.string.mail_not_delivered);
        } else if (mail.isDelivered() && !mail.isCollected()) {
            holder.senderTV.setText(R.string.mystery_sender);
            holder.titleTv.setText(R.string.mail_delivered);
        } else {
            holder.senderTV.setText(mail.getSender());
            holder.titleTv.setText(mail.getTitle());
        }

        holder.titleTv.setOnClickListener(getTitleClickListener(position));

    }

    @Override
    public int getItemCount() {
        return mMailList.size();
    }

    private View.OnClickListener getTitleClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMailListPresenter.mailTitleClicked(mMailList.get(position));
            }
        };
    }

}
