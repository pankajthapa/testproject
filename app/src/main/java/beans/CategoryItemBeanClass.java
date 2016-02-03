package beans;

import android.graphics.Bitmap;

/**
 * Created by cynoteck on 1/14/2016.
 */
public class CategoryItemBeanClass {

    private int categoryID;
    private String categoryTitle;
    private Bitmap categoryImage;
    private String categorySubTitle;

    public CategoryItemBeanClass()
    {
    }

    public int getCategoryID()
    {
        return this.categoryID;
    }
    public void setCategoryID(int id)
    {
        categoryID = id;
    }

    public String getCategoryTitle()
    {
        return this.categoryTitle;
    }
    public void setCategoryTitle(String title)
    {
         categoryTitle = title;
    }

    public Bitmap getCategoryImage()
    {
         return this.categoryImage;
    }
    public void setCategoryImage(Bitmap image)
    {
        categoryImage = image;
    }

    public String getCategorySubTitle()
    {
        return this.categorySubTitle;
    }
    public void setCategorySubTitle(String subtitle)
    {
        categorySubTitle = subtitle;
    }
}
