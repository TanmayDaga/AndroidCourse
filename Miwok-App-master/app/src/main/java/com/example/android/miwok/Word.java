package com.example.android.miwok;

public class Word {
    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private int imageResourceId = NO_IMAGE_PROVIDED;
    private int audioResourceID;
    private static final int NO_IMAGE_PROVIDED = -1;

    public Word(String defaultTranslation, String miwokTranslation,int audioResourceID){
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        this.audioResourceID = audioResourceID;
    }
    public Word(String mDefaultTranslation,String mMiwokTranslation,int imageResourceId,int audioResourceID){
        this.mDefaultTranslation = mDefaultTranslation;
        this.mMiwokTranslation = mMiwokTranslation;
        this.imageResourceId = imageResourceId;
        this.audioResourceID = audioResourceID;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getImageResourseId() { return imageResourceId; }

    public int getAudioResourceID() { return audioResourceID;}



    public boolean hasImage(){
        return imageResourceId!=NO_IMAGE_PROVIDED;
    }
}
