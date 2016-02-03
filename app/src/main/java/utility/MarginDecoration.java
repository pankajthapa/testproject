package utility;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cynoteck.petofy.R;

/**
 * Created by cynoteck on 1/13/2016.
 */
public class MarginDecoration extends RecyclerView.ItemDecoration {

    private int vertical_margin;
    private int horizontal_margin;

    public MarginDecoration(Context context)
    {
        vertical_margin = context.getResources().getDimensionPixelSize(R.dimen.category_item_vertical_margin);
        horizontal_margin = context.getResources().getDimensionPixelSize(R.dimen.category_item_horizontal_margin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(horizontal_margin, vertical_margin, horizontal_margin, vertical_margin);
    }
}
