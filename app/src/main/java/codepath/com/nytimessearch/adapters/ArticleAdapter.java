package codepath.com.nytimessearch.adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
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
        return TextUtils.isEmpty(article.getPhoto()) ? Article.Type.NO_IMAGE.value : Article.Type.WITH_IMAGE.value;
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

            String thumbnail = article.getPhoto();
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

            itemView.setOnClickListener(v -> {

                int position = getAdapterPosition();
                Article article = articles.get(position);

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share_action);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, article.getHeadline());
                int requestCode = 100;

                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
                builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(article.getWebUrl()));

            });
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

        }
    }

    public class ArticleViewNoImageHolder extends ArticleViewHolder {
        TextView tvSnippet;
        TextView tvTitle;

        public ArticleViewNoImageHolder(View itemView) {
            super(itemView);

            tvSnippet = (TextView) itemView.findViewById(R.id.tvSnippet);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

        }
    }
}
