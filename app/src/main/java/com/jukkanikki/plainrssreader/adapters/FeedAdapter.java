package com.jukkanikki.plainrssreader.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jukkanikki.plainrssreader.R;
import com.jukkanikki.plainrssreader.model.FeedItem;
import com.jukkanikki.plainrssreader.model.FeedWrapper;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    private FeedWrapper feed;
    private Context context;
    private LayoutInflater inflater;

    public FeedAdapter(FeedWrapper feed, Context context) {
        this.feed = feed;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.feed_row,parent,false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        FeedItem item = feed.getItems().get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return feed.items.size();
    }
}

class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private FeedItem item;

    private TextView txtTitle;
    private TextView txtPubDate;
    private TextView txtContent;

    public FeedViewHolder(View itemView) {
        super(itemView);

        txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
        txtPubDate = (TextView)itemView.findViewById(R.id.txtPubDate);
        txtContent = (TextView)itemView.findViewById(R.id.txtContent);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
        Context context = view.getContext();
        context.startActivity(browserIntent);
    }


    public void bind(FeedItem item) {
        this.item = item;

        txtTitle.setText(item.getTitle());
        txtPubDate.setText(item.getPubDate());
        txtContent.setText(item.getContent());
    }

}