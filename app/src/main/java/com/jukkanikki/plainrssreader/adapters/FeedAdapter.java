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

/**
* Feed adapter creates feed view holders and bind holders to content in specified location of feeds list
*/
public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    private FeedWrapper feed;
    private Context context;
    private LayoutInflater inflater;

    public FeedAdapter(FeedWrapper feed, Context context) {
        this.feed = feed;
        this.context = context;
        this.inflater = LayoutInflater.from(context); // inflate layout only once
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         // view holders are created separately from binding to support reuse of holder objects
        View itemView = inflater.inflate(R.layout.feed_row,parent,false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        FeedItem item = feed.getItems().get(position); // find feed item to bind
        holder.bind(item); // bind item to holder
    }

    @Override
    public int getItemCount() {
        return feed.items.size(); // recyler view need to know amount of items 
    }
}

class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private FeedItem item; // reference to item, contains all fields

    private TextView txtTitle;
    private TextView txtPubDate;
    private TextView txtContent;

    public FeedViewHolder(View itemView) {
        super(itemView);

        // create text fields of view during creation of holder
        txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
        txtPubDate = (TextView)itemView.findViewById(R.id.txtPubDate);
        txtContent = (TextView)itemView.findViewById(R.id.txtContent);

        // click listener set once at holder level
        itemView.setOnClickListener(this);
    }

    /**
    * Called when user clicks view
    */
    @Override
    public void onClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink())); // intent to show item using url
        Context context = view.getContext();
        context.startActivity(browserIntent);
    }


    /**
    * Binding is in holder so that adapter doesn't need to know how item view is filled
    */
    public void bind(FeedItem item) {
        this.item = item; // save full item

        // bind fields
        txtTitle.setText(item.getTitle());
        txtPubDate.setText(item.getPubDate());
        txtContent.setText(item.getContent());
    }

}
