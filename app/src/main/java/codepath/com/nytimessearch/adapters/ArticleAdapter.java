package codepath.com.nytimessearch.adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.nytimessearch.R;
import codepath.com.nytimessearch.models.Article;
import codepath.com.nytimessearch.utils.CategoryColorChooser;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;
    private Context context;
    private SimpleDateFormat nytFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
            String category = article.getNewsDesk();
            viewHolder.tvCategory.setText(category);
            viewHolder.tvCategory.setBackgroundColor(Color.parseColor(CategoryColorChooser.getColor(category)));


            try {
                String reformattedStr = sdf.format(nytFormat.parse(article.getPubDate()));
                viewHolder.tvDate.setText(reformattedStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            String thumbnail = article.getPhoto();
            if (!TextUtils.isEmpty(thumbnail)) {
                Glide.with(context).load(thumbnail).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter().into(viewHolder.ivImage);
            }

        } else {
            ArticleViewNoImageHolder viewHolder = (ArticleViewNoImageHolder) holder;

            viewHolder.tvTitle.setText(article.getHeadline());
            viewHolder.tvSnippet.setText(article.getSnippet());
            String category = article.getNewsDesk();
            viewHolder.tvCategory.setText(category);
            viewHolder.tvCategory.setBackgroundColor(Color.parseColor(CategoryColorChooser.getColor(category)));
            try {
                String reformattedStr = sdf.format(nytFormat.parse(article.getPubDate()));
                viewHolder.tvDate.setText(reformattedStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

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
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvSnippet)
        TextView tvSnippet;
        @BindView(R.id.tvCategory)
        TextView tvCategory;
        @BindView(R.id.tvDate)
        TextView tvDate;

        public ArticleViewWithImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ArticleViewNoImageHolder extends ArticleViewHolder {
        @BindView(R.id.tvSnippet)
        TextView tvSnippet;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvCategory)
        TextView tvCategory;
        @BindView(R.id.tvDate)
        TextView tvDate;

        public ArticleViewNoImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
