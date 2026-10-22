package com.example.smartpantry.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.logic.RecipeMatcher;
import com.example.smartpantry.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/** Adapter used for BOTH the strict-suggestions list and the bonus "Almost There" list. */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public interface Listener {
        void onRecipeClick(Recipe recipe);
    }

    private final List<RecipeMatcher.MatchResult> results = new ArrayList<>();
    private final Listener listener;
    private final boolean showMissing;

    /** @param showMissing true for the "Almost There" list: shows which ingredient is missing */
    public RecipeAdapter(Listener listener, boolean showMissing) {
        this.listener = listener;
        this.showMissing = showMissing;
    }

    public void setData(List<RecipeMatcher.MatchResult> newResults) {
        results.clear();
        results.addAll(newResults);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RecipeMatcher.MatchResult result = results.get(position);
        final Recipe recipe = result.getRecipe();
        holder.tvName.setText(recipe.getName());

        if (showMissing) {
            holder.tvInfo.setText("Missing: " + result.getMissing().get(0));
        } else {
            holder.tvInfo.setText(recipe.getIngredients().size() + " ingredients - you have them all");
        }
        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName, tvInfo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvInfo = itemView.findViewById(R.id.tvRecipeInfo);
        }
    }
}
