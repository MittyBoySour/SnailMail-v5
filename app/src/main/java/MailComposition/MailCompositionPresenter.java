package MailComposition;

import Model.User;
import Utilities.FirebaseManager;

class MailCompositionPresenter implements MailCompositionContract.Presenter {

    private final MailCompositionContract.View mMailCompositionView;
    private FirebaseManager mFirebaseManager;
    private User mCurrentUser;

    MailCompositionPresenter(MailCompositionContract.View mailCompositionView) {
        // check for null
        this.mMailCompositionView = mailCompositionView;

        mMailCompositionView.setPresenter(this);
    }

    @Override
    public void setCurrentUser(User user) {
        mCurrentUser = user;
        mFirebaseManager = FirebaseManager.getInstance(mCurrentUser.getUsername(), this);
    }


    @Override
    public void start() {

    }

    @Override
    public void updateMailList() {

    }
}
