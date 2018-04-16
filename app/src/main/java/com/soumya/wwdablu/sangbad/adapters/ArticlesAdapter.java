package com.soumya.wwdablu.sangbad.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soumya.wwdablu.sangbad.R;
import com.soumya.wwdablu.sangbad.databinding.ItemArticleInfoCardBinding;
import com.soumya.wwdablu.sangbad.databinding.ItemArticleInfoCompactBinding;
import com.soumya.wwdablu.sangbad.network.model.Articles;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            LAYOUT_COMPACT,
            LAYOUT_CARD
    })
    @interface ArticleViewType {}
    public static final int LAYOUT_COMPACT = 0;
    public static final int LAYOUT_CARD = 1;

    private LinkedList<Articles> articlesList;
    private @ArticleViewType int layoutViewType;

    public ArticlesAdapter() {
        this(LAYOUT_CARD);
    }

    public ArticlesAdapter(@ArticleViewType int viewType) {
        this.layoutViewType = viewType;
    }

    @Override
    public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch(viewType) {

            case LAYOUT_COMPACT:
                ItemArticleInfoCompactBinding compactBinder = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_article_info_compact, parent, false);
                return new ArticlesViewHolder(compactBinder, compactBinder.getRoot());

            case LAYOUT_CARD:
            default:
                ItemArticleInfoCardBinding cardBinder = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_article_info_card, parent, false);
                return new ArticlesViewHolder(cardBinder, cardBinder.getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {

        return layoutViewType;
    }

    @Override
    public void onBindViewHolder(ArticlesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    public void setArticlesList(LinkedList<Articles> articlesList) {
        this.articlesList = articlesList;
        notifyDataSetChanged();
    }

    public void setLayoutViewType(@ArticleViewType int viewType) {
        this.layoutViewType = viewType;
        notifyDataSetChanged();
    }

    @BindingAdapter("articleImage")
    public static void articleImage(ImageView imageView, String urlToImage) {

        //Guard check
        if(TextUtils.isEmpty(urlToImage)) {
            return;
        }

        Picasso.with(imageView.getContext())
            .load(urlToImage)
            .placeholder(R.drawable.newspaper)
            .error(R.drawable.newspaper)
            .into(imageView);
    }

    @BindingAdapter("authorName")
    public static void authorName(TextView textView, String author) {

        if(TextUtils.isEmpty(author)) {
            textView.setVisibility(View.GONE);
            return;
        }

        textView.setText(author);
    }

    class ArticlesViewHolder extends RecyclerView.ViewHolder {

        private Object binder;

        ArticlesViewHolder(Object binder, View itemView) {
            super(itemView);
            this.binder = binder;

            itemView.setOnClickListener(clickListener);
        }

        void bind(int position) {

            switch (layoutViewType) {

                case LAYOUT_COMPACT:
                    ((ItemArticleInfoCompactBinding) binder).setArticleInfo(articlesList.get(position));
                    break;

                case LAYOUT_CARD:
                default:
                    ((ItemArticleInfoCardBinding) binder).setArticleInfo(articlesList.get(position));
                    break;
            }
        }

        private View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Articles article = articlesList.get(getAdapterPosition());
                Context context = view.getContext();

                if(TextUtils.isEmpty(article.getUrl())) {
                    Toast.makeText(context, context.getString(R.string.article_details_not_found),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));

                if(null != browser.resolveActivity(context.getPackageManager())) {
                    context.startActivity(browser);
                }
            }
        };
    }
}
