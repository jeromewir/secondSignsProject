package com.wirsztj.jsonparser.Adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wirsztj.jsonparser.Model.Person;
import com.wirsztj.jsonparser.R;

import java.util.List;

/**
 * Created by wirszt_j on 22/10/14.
 */
public class GetInformationsAdapter extends BaseAdapter {
    private List<Person> persons;

    @Override
    public int getCount() {
        return persons != null ? persons.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return persons != null && persons.size() >= position ? null : persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.get_information_view, null);

            viewHolder = new ViewHolder();
            viewHolder.profilePicture = (ImageView)convertView.findViewById(R.id.profilePicture);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.birthDate = (TextView)convertView.findViewById(R.id.birthDate);
            viewHolder.year = (TextView)convertView.findViewById(R.id.year);
            viewHolder.pg = (ProgressBar)convertView.findViewById(R.id.progressBar);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Person person = persons.get(position);

        viewHolder.name.setText(person.getName());
        viewHolder.birthDate.setText(person.getBirthDate());
        viewHolder.year.setText(person.getYear());
        ImageLoader.getInstance().loadImage(person.getProfilePictureUrl(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                viewHolder.pg.setVisibility(View.GONE);
                viewHolder.profilePicture.setVisibility(View.VISIBLE);
                viewHolder.profilePicture.setImageBitmap(loadedImage);
            }
        });
        /* J'avoue qu'ici, j'ai triché par facilité en utilisant une lib */

        return convertView;
    }

    public void setList(List<Person> persons) {
        this.persons = persons;

        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView profilePicture;
        TextView name;
        TextView birthDate;
        TextView year;
        ProgressBar pg;
    }
}
