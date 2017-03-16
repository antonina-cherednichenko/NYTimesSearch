package codepath.com.nytimessearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import codepath.com.nytimessearch.R;
import codepath.com.nytimessearch.activities.ArticleActivity;
import codepath.com.nytimessearch.models.Article;

/**
 * Created by tonya on 3/14/17.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;
    private Context context;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_article_result, parent, false);


        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = articles.get(position);

        holder.tvTitle.setText(article.getHeadline());

        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(context).load(thumbnail).into(holder.ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(context, ArticleActivity.class);
                    Article article = articles.get(position);
                    i.putExtra(ArticleActivity.ARTICLE_EXTRA, article);
                    context.startActivity(i);
                }
            });
        }
    }
}
