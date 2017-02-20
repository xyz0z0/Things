package xyz.xyz0z0.things.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

import xyz.xyz0z0.things.util.Utils;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class Task {
    @NonNull
    private String mId;

    @Nullable
    private String mTitle;

    @Nullable
    private String mDescription;

    private boolean mCompleted;

    public Task(@Nullable String title, @Nullable String description) {
        this(title, description, UUID.randomUUID().toString(), false);
    }

    public Task(@Nullable String title, @Nullable String description, @Nullable String id) {
        this(title, description, id, false);
    }

    public Task(@Nullable String title, @Nullable String description, boolean completed) {
        this(title, description, UUID.randomUUID().toString(), completed);
    }

    public Task(@Nullable String title, @Nullable String description, @Nullable String id, boolean complted) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = complted;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getTitleForList() {
        if (!Utils.isNullOrEmpty(mTitle)) {
            return mTitle;
        } else {
            return mDescription;
        }
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public boolean isEmpty() {
        return Utils.isNullOrEmpty(mTitle) && Utils.isNullOrEmpty(mDescription);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || getClass() != obj.getClass()) {
            return false;
        }
        Task task = (Task) obj;
        return Utils.equal(mId, task.mId) &&
                Utils.equal(mTitle, task.mTitle) &&
                Utils.equal(mDescription, task.mDescription);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(mId, mTitle, mDescription);
    }

    @Override
    public String toString() {
        return "Task with title " + mTitle;
    }
}
