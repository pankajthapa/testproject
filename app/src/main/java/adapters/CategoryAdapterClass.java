package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cynoteck.petofy.ParticularPetCategory;
import com.cynoteck.petofy.R;

import java.util.ArrayList;

import beans.CategoryItemBeanClass;
import utility.Common;

/**
 * Created by cynoteck on 1/13/2016.
 */
public class CategoryAdapterClass extends RecyclerView.Adapter<CategoryAdapterClass.ViewHolder> {

    private ArrayList<CategoryItemBeanClass> categoryItems;
    private Context context;

    public CategoryAdapterClass(Context context, ArrayList<CategoryItemBeanClass> categoryItems){
        this.context = context;
        this.categoryItems = categoryItems;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryTitle;
        public ImageView categoryImageIcon;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.categoryGridItem);
            categoryTitle =(TextView) itemView.findViewById(R.id.categoryName);
            categoryImageIcon = (ImageView) itemView.findViewById(R.id.categoryImageView);
        }
    }


    @Override
    public CategoryAdapterClass.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapterClass.ViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        CategoryItemBeanClass category_item = categoryItems.get(position);
        final int categoryId = category_item.getCategoryID();
        holder.categoryTitle.setText(category_item.getCategoryTitle());
        holder.categoryImageIcon.setImageBitmap(category_item.getCategoryImage());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(context, ParticularPetCategory.class);
                intent.putExtra(Common.KEY_CATEGORYID,categoryId);
                context.startActivity(intent);
//                Toast.makeText(holder.view.getContext(), "hello how r.. khana khake jana haan..", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

}
