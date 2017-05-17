package io.dflabs.chatonline.model.pojos;

import java.util.UUID;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by dflabs on 5/11/17.
 * ChatOnline
 */

@RealmClass
public class Message implements RealmModel {
    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    public String message;
    public String username;

    public Message() {

    }

    public Message(String message) {
        this.message = message;
    }
}
