package com.example.android.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StoryAdapter extends ArrayAdapter<Story> {

    public StoryAdapter(Context context, List<Story> stories) {
        super(context, 0, stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.story_list_item, parent, false);
        }
        Story currentStory = getItem(position);

        TextView sectionNameView = (TextView) listItemView.findViewById(R.id.section_name);
        String sectionName = currentStory.getSectionName();
        sectionNameView.setText(sectionName);

        TextView webPublicationDateView = (TextView) listItemView.findViewById(R.id.web_publication_date);
        String webPublicationDate = currentStory.getWebPublicationDate();
        if(webPublicationDate == "") {
            webPublicationDateView.setText(R.string.no_web_publication_date);
        } else {
            webPublicationDateView.setText(webPublicationDate);
        }
        webPublicationDateView.setText(webPublicationDate);

        TextView webTitleView = (TextView) listItemView.findViewById(R.id.web_title);
        String webTitle = currentStory.getWebTitle();
        webTitleView.setText(webTitle);

        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        String author = currentStory.getAuthor();
        if(author == "") {
            authorView.setText(R.string.no_author);
        } else {
            authorView.setText(author);
        }

        return listItemView;
    }
}
