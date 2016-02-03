package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cynoteck.petofy.PetDetailsScreen;
import com.cynoteck.petofy.R;

import java.util.ArrayList;

import beans.ParticularPetBeanClass;

/**
 * Created by cynoteck on 1/27/2016.
 */
public class ParticularPetAdapterClass extends RecyclerView.Adapter<ParticularPetAdapterClass.ViewHolder> {


    private ArrayList<ParticularPetBeanClass> particularPet;
    private Context context;

    public ParticularPetAdapterClass(Context context, ArrayList<ParticularPetBeanClass> particularPet){
        this.context = context;
        this.particularPet = particularPet;
        Log.e("ParticularPet",""+particularPet);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView petBreed, petAge, petDescription, petPrice;
        public ImageView petImage;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.particularPetItem);
            petImage = (ImageView)itemView.findViewById(R.id.petImage);
            petBreed = (TextView)itemView.findViewById(R.id.petBreed);
            petAge = (TextView)itemView.findViewById(R.id.petAge);
            petDescription = (TextView)itemView.findViewById(R.id.petDescription);
            petPrice = (TextView)itemView.findViewById(R.id.petPrice);
        }
    }


    @Override
    public ParticularPetAdapterClass.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.particular_category_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ParticularPetBeanClass particularPet_items = particularPet.get(position);
        Log.e("ParticularPetItems",""+particularPet.get(position));

        holder.petBreed.setText(particularPet_items.getBreed());
        holder.petAge.setText(String.valueOf(particularPet_items.getYears())+" "+context.getString(R.string.pet_age_years_string)+" "+String.valueOf(particularPet_items.getMonths())+" "+context.getString(R.string.pet_age_months_string));
        holder.petDescription.setText(particularPet_items.getPet_Title());
        holder.petPrice.setText(context.getString(R.string.rupee)+" "+String.valueOf(particularPet_items.getPet_price()));
        holder.petImage.setImageBitmap(particularPet_items.getPet_profile_Image());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PetDetailsScreen.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return particularPet.size();
    }

}
