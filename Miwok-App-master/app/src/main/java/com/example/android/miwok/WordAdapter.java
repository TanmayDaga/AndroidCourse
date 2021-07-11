package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter<W> extends ArrayAdapter<Word> {

    private int colorResourceid;
    public WordAdapter(@NonNull Context context,  @NonNull List<Word> objects,int colorResourceid) {
        super(context,0, objects);
        this.colorResourceid = colorResourceid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }


            Word currentWord = getItem(position);
//        Setting miwoktext
            ((TextView) listItemView.findViewById(R.id.miwok_text)).setText(currentWord.getMiwokTranslation());
            ((TextView) listItemView.findViewById(R.id.english_text)).setText(currentWord.getDefaultTranslation());

            int color  = ContextCompat.getColor(getContext(),colorResourceid);
            ((LinearLayout) listItemView.findViewById(R.id.secondLinearLayout)).setBackgroundColor(color);

            // Checking if imageResource exists- As image doesn't exist for phrases
            ImageView imageView  = (ImageView) listItemView.findViewById(R.id.imageView);
            if (currentWord.hasImage()) {
                imageView.setImageResource(currentWord.getImageResourseId());
                imageView.setVisibility(View.VISIBLE);
            }
            else {
                imageView.setVisibility(View.GONE);
            }

        return listItemView;
    }
}
