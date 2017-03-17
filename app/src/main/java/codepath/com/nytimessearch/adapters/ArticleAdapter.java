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


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;
    private Context context;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public int getItemViewType(int position) {
        Article article = articles.get(position);
        return TextUtils.isEmpty(article.getThumbNail()) ? Article.Type.NO_IMAGE.value : Article.Type.WITH_IMAGE.value;
    }


    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Article.Type.WITH_IMAGE.value) {
            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_article_with_image, parent, false);
            return new ArticleViewWithImageHolder(view);
        } else {
            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_article_no_image, parent, false);
            return new ArticleViewNoImageHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = articles.get(position);

        if (holder.getItemViewType() == Article.Type.WITH_IMAGE.value) {
            ArticleViewWithImageHolder viewHolder = (ArticleViewWithImageHolder) holder;
            viewHolder.tvTitle.setText(article.getHeadline());
            viewHolder.tvSnippet.setText(article.getSnippet());

            String thumbnail = article.getThumbNail();
            if (!TextUtils.isEmpty(thumbnail)) {
                //Measure parent width
                int displayWidth = context.getResources().getDisplayMetrics().widthPixels;

                Picasso.with(context).load(thumbnail).resize(displayWidth / 2, 0).into(viewHolder.ivImage);
            }

        } else {
            ArticleViewNoImageHolder viewHolder = (ArticleViewNoImageHolder) holder;

            viewHolder.tvTitle.setText(article.getHeadline());
            viewHolder.tvSnippet.setText(article.getSnippet());

        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        public ArticleViewHolder(View itemView) {
            super(itemView);
        }

    }

    public class ArticleViewWithImageHolder extends ArticleViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvSnippet;

        public ArticleViewWithImageHolder(View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSnippet = (TextView) itemView.findViewById(R.id.tvSnippet);

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

    public class ArticleViewNoImageHolder extends ArticleViewHolder {
        TextView tvSnippet;
        TextView tvTitle;

        public ArticleViewNoImageHolder(View itemView) {
            super(itemView);

            tvSnippet = (TextView) itemView.findViewById(R.id.tvSnippet);
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
