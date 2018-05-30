package com.mad.snailmail_v5.MailList;

class MailListPresenter implements MailListContract.Presenter {

    private final MailListContract.View mMailListView;

    MailListPresenter(MailListContract.View mailListView) {
        // check for null
        this.mMailListView = mailListView;

        mMailListView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
