package com.soumya.wwdablu.sangbad.adapters;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.soumya.wwdablu.sangbad.R;
import com.soumya.wwdablu.sangbad.databinding.ItemSourceInfoCardBinding;
import com.soumya.wwdablu.sangbad.databinding.ItemSourceInfoCompactBinding;
import com.soumya.wwdablu.sangbad.network.model.Sources;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;

public class NewsSourcesAdapter extends RecyclerView.Adapter<NewsSourcesAdapter.SourceViewHolder>
            implements Filterable {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            LAYOUT_COMPACT,
            LAYOUT_CARD
    })
    @interface SourceViewType {}
    public static final int LAYOUT_COMPACT = 0;
    public static final int LAYOUT_CARD = 1;

    private LinkedList<Sources> sourcesList;
    private LinkedList<Sources> actualList;
    private ISourceAction sourceAction;
    private @SourceViewType int layoutViewType;

    public NewsSourcesAdapter(@NonNull ISourceAction sourceAction) {
        this(sourceAction, LAYOUT_CARD);
    }

    public NewsSourcesAdapter(@NonNull ISourceAction sourceAction, @SourceViewType int viewType) {
        sourcesList = new LinkedList<>();
        actualList = new LinkedList<>();
        this.sourceAction = sourceAction;
        this.layoutViewType = viewType;
    }

    @Override
    public SourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch(layoutViewType) {

            case LAYOUT_COMPACT:
                ItemSourceInfoCompactBinding compactBinder = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_source_info_compact, parent, false);
                return new SourceViewHolder(compactBinder, compactBinder.getRoot());

            case LAYOUT_CARD:
            default:
                ItemSourceInfoCardBinding cardBinder = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_source_info_card, parent, false);
                return new SourceViewHolder(cardBinder, cardBinder.getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {

        return layoutViewType;
    }

    @Override
    public void onBindViewHolder(SourceViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return sourcesList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults filterResults = new FilterResults();

                if(TextUtils.isEmpty(charSequence)) {
                    filterResults.values = actualList;
                    return filterResults;
                }

                sourcesList = new LinkedList<>();
                for(Sources sources : actualList) {

                    if(sources.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        sourcesList.add(sources);
                    }
                }

                filterResults.values = sourcesList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                sourcesList = (LinkedList<Sources>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setSourcesList(LinkedList<Sources> sourcesList) {
        this.sourcesList = sourcesList;
        this.actualList = sourcesList;
        notifyDataSetChanged();
    }

    public void setLayoutViewType(@SourceViewType int viewType) {
        this.layoutViewType = viewType;
        notifyDataSetChanged();
    }

    @BindingAdapter("sourceType")
    public static void sourceType(ImageView imageView, String category) {

        switch(category.toLowerCase()) {
            case "business":
                imageView.setImageResource(R.drawable.business);
                break;

            case "entertainment":
                imageView.setImageResource(R.drawable.entertainment);
                break;

            case "gaming":
                imageView.setImageResource(R.drawable.gaming);
                break;

            case "general":
                imageView.setImageResource(R.drawable.general);
                break;

            case "health":
                imageView.setImageResource(R.drawable.health);
                break;

            case "music":
                imageView.setImageResource(R.drawable.music);
                break;

            case "science-and-nature":
                imageView.setImageResource(R.drawable.science);
                break;

            case "sport":
                imageView.setImageResource(R.drawable.sport);
                break;

            case "technology":
                imageView.setImageResource(R.drawable.technology);
                break;

            default:
                imageView.setImageResource(R.drawable.general);
        }
    }

    class SourceViewHolder extends RecyclerView.ViewHolder {

        private Object binder;

        SourceViewHolder(Object binder, View itemView) {
            super(itemView);
            this.binder = binder;

            itemView.setOnClickListener(clickListener);
        }

        void bind(int position) {

            switch (layoutViewType) {

                case LAYOUT_COMPACT:
                    ((ItemSourceInfoCompactBinding) binder).setSourceInfo(sourcesList.get(position));
                    break;

                case LAYOUT_CARD:
                default:
                    ((ItemSourceInfoCardBinding) binder).setSourceInfo(sourcesList.get(position));
                    break;
            }
        }

        private View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(null != sourceAction) {
                    sourceAction.onSourceClick(sourcesList.get(getAdapterPosition()), getAdapterPosition());
                }
            }
        };
    }
}
