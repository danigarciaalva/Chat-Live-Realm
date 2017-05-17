package io.dflabs.chatonline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import io.dflabs.chatonline.model.pojos.Message;
import io.dflabs.chatonline.view.adapters.ChatAdapter;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import rx.Observable;

public class MainActivity extends AppCompatActivity {

    EditText mEditText;
    RecyclerView mChatRecyclerView;
    ChatAdapter mChatAdapter;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.act_main_text);
        mChatRecyclerView = (RecyclerView) findViewById(R.id.act_main_chat);
        mChatRecyclerView.setAdapter(mChatAdapter = new ChatAdapter(this));
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setHasFixedSize(true);

        Realm.init(this);

        findViewById(R.id.act_main_send).setOnClickListener(mOnSendClickListener);

        SyncCredentials myCredentials = SyncCredentials.usernamePassword("danigarciaalva@hotmail.com", "Passw0rd0", false);
        String authURL = "http://192.168.100.7:9080/auth";
        SyncUser user = SyncUser.currentUser();
        if (user == null) {
            SyncUser.loginAsync(myCredentials, authURL, new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) {
                    loadRealm(user);
                }

                @Override
                public void onError(ObjectServerError error) {
                    error.printStackTrace();
                }
            });
        } else {
            loadRealm(user);
        }
    }

    private void loadRealm(SyncUser user) {
        String serverURL = "realm://192.168.100.7:9080/~/default2";
        SyncConfiguration configuration = new SyncConfiguration.Builder(user, serverURL).build();
        mRealm = Realm.getInstance(configuration);

        Observable<RealmResults<Message>> messages = mRealm.where(Message.class)
                .findAllAsync()
                .asObservable();

        messages
                .map(results -> {
                    RealmList<Message> list = new RealmList<>();
                    list.addAll(results.subList(0, results.size()));
                    return list;
                })
                .subscribe(results -> {
                    mChatAdapter.update(results);
                    mChatRecyclerView.scrollToPosition(mChatAdapter.getItemCount());
                });
    }

    private View.OnClickListener mOnSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!TextUtils.isEmpty(mEditText.getText())) {
                mRealm.executeTransaction(realm -> {
                    Message message = new Message(mEditText.getText().toString().trim());
                    realm.copyToRealmOrUpdate(message);
                    mEditText.setText("");
                });
            }
        }
    };
}
