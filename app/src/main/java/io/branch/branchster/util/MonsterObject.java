package io.branch.branchster.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marshall on 3/9/17.
 */

public class MonsterObject implements Parcelable {
    private String monsterName;
    private String monsterDescription;

    private int colorIndex;
    private int bodyIndex;
    private int faceIndex;

    public MonsterObject() {
        colorIndex = 0;
        bodyIndex = 0;
        faceIndex = 0;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public String getMonsterDescription() {
        return monsterDescription;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public int getBodyIndex() {
        return bodyIndex;
    }

    public int getFaceIndex() {
        return faceIndex;
    }

    public String getMonsterImage() {
        return "https://s3-us-west-1.amazonaws.com/branchmonsterfactory/" + (short) getColorIndex() + (short) getBodyIndex() + (short) getFaceIndex() + ".png";
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public void setMonsterDescription(String monsterDescription) {
        this.monsterDescription = monsterDescription;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public void setBodyIndex(int bodyIndex) {
        this.bodyIndex = bodyIndex;
    }

    public void setFaceIndex(int faceIndex) {
        this.faceIndex = faceIndex;
    }

    //////////////////////////////////////////////
    // Parcelable Implementation
    //////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(monsterName);
        dest.writeString(monsterDescription);

        dest.writeInt(colorIndex);
        dest.writeInt(bodyIndex);
        dest.writeInt(faceIndex);
    }

    protected MonsterObject(Parcel in) {
        monsterName = in.readString();
        monsterDescription = in.readString();
        colorIndex = in.readInt();
        bodyIndex = in.readInt();
        faceIndex = in.readInt();
    }

    public static final Creator<MonsterObject> CREATOR = new Creator<MonsterObject>() {
        @Override
        public MonsterObject createFromParcel(Parcel in) {
            return new MonsterObject(in);
        }

        @Override
        public MonsterObject[] newArray(int size) {
            return new MonsterObject[size];
        }
    };
}
