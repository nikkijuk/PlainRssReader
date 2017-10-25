package com.jukkanikki.plainrssreader.adapters;

/**
 * Created by jnikki on 10/25/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jukkanikki.plainrssreader.R;
import com.jukkanikki.plainrssreader.db.Article;
import com.jukkanikki.plainrssreader.model.FeedItem;
import com.jukkanikki.plainrssreader.model.FeedWrapper;

import java.util.List;

/**
 * Article adapter creates article view holders and bind holders to content in specified location of article list
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    private List<Article> articles;
    private Context context;
    private LayoutInflater inflater;

    public ArticleAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
        this.inflater = LayoutInflater.from(context); // inflate layout only once
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // view holders are created separately from binding to support reuse of holder objects
        View itemView = inflater.inflate(R.layout.feed_row,parent,false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = articles.get(position); // find article to bind
        holder.bind(article); // bind article to holder
    }

    @Override
    public int getItemCount() {
        return articles.size(); // recyler view need to know amount of items
    }
}

class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Article article; // reference to article, contains all fields

    private TextView txtTitle;
    private TextView txtPubDate;
    private TextView txtContent;

    public ArticleViewHolder(View articleView) {
        super(articleView);

        // create text fields of view during creation of holder
        txtTitle = (TextView) articleView.findViewById(R.id.txtTitle);
        txtPubDate = (TextView) articleView.findViewById(R.id.txtPubDate);
        txtContent = (TextView) articleView.findViewById(R.id.txtContent);

        // click listener set once at holder level
        articleView.setOnClickListener(this);
    }

    /**
     * Called when user clicks view
     */
    @Override
    public void onClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getLink())); // intent to show item using url
        Context context = view.getContext();
        context.startActivity(browserIntent);
    }


    /**
     * Binding is in holder so that adapter doesn't need to know how item view is filled
     */
    public void bind(Article article) {
        this.article = article; // save full item

        // bind fields
        txtTitle.setText(article.getTitle());
        txtPubDate.setText(article.getPubDate());
        txtContent.setText(article.getContent());
    }
}