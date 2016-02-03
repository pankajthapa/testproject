package beans;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by cynoteck on 1/27/2016.
 */
public class ParticularPetBeanClass {
    private int pet_Age, category_Id, district_Id,pet_serial_Id,months,state_Id,pet_price,sub_district_Id,years,pet_Breed_Id;
    private String breed,pet_category,cityname,country,pet_description,created_Date,district,pet_owner,pet_Id,pet_Name,pet_Title,state,user_Id;
    private boolean isActive,isEdited,isSold,isVerified;
    private Bitmap pet_profile_Image;
    private ArrayList<PetImages> pet_Images_array;

    public ParticularPetBeanClass(){
    }

    public void setPet_Age(int pet_Age){
        this.pet_Age = pet_Age;
    }
    public int getAge(){
        return this.pet_Age;
    }

    public void setCategory_Id(int category_Id){
        this.category_Id = category_Id;
    }
    public int getCategory_Id(){
        return this.category_Id;
    }

    public void setDistrict_Id(int district_Id){
        this.district_Id = district_Id;
    }
    public int getDistrict_Id(){
        return this.district_Id;
    }

    public void setPet_serial_Id(int pet_serial_Id){
        this.pet_serial_Id = pet_serial_Id;
    }
    public int getPet_serial_Id(){
        return this.pet_serial_Id;
    }

    public void setMonths(int months){
        this.months = months;
    }
    public int getMonths(){
        return this.months;
    }

    public void setState_Id(int state_Id){
        this.state_Id = state_Id;
    }
    public int getState_Id(){
        return state_Id;
    }

    public void setPet_price(int pet_price){
        this.pet_price = pet_price;
    }
    public int getPet_price(){
        return this.pet_price;
    }

    public void setSub_district_Id(int sub_district_Id){
        this.sub_district_Id = sub_district_Id;
    }
    public int getSub_district_Id(){
        return sub_district_Id;
    }

    public void setYears(int years){
        this.years = years;
    }
    public int getYears(){
        return this.years;
    }

    public void setPet_Breed_Id(int pet_breed_id){
        this.pet_Breed_Id = pet_breed_id;
    }
    public int getPet_Breed_Id(){
        return this.pet_Breed_Id;
    }

    public void setBreed(String breed){
        this.breed = breed;
    }
    public String getBreed(){
        return this.breed;
    }

    public void setPet_category(String pet_category){
        this.pet_category = pet_category;
    }
    public String getPet_category(){
        return this.pet_category;
    }

    public void setCityname(String cityname){
        this.cityname = cityname;
    }
    public String getCityname(){
        return this.cityname;
    }

    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return this.country;
    }

    public void setPet_description(String pet_description){
        this.pet_description = pet_description;
    }
    public String getPet_description(){
        return this.pet_description;
    }

    public void setCreated_Date(String created_Date){
        this.created_Date = created_Date;
    }
    public String getCreated_Date(){
        return this.created_Date;
    }

    public void setDistrict(String district){
        this.district = district;
    }
    public String getDistrict(){
        return this.district;
    }

    public void setPet_owner(String pet_owner){
        this.pet_owner = pet_owner;
    }
    public String getPet_owner(){
        return this.pet_owner;
    }

    public void setPet_Id(String pet_Id){
        this.pet_Id = pet_Id;
    }
    public String getPet_Id(){
        return this.pet_Id;
    }

    public void setPet_Name(String pet_name){
        this.pet_Name = pet_name;
    }
    public String getPet_Name(){
        return this.pet_Name;
    }

    public void setPet_Title(String pet_title){
        this.pet_Title = pet_title;
    }
    public String getPet_Title(){
        return this.pet_Title;
    }

    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        return this.state;
    }

    public void setUser_Id(String user_Id){
        this.user_Id = user_Id;
    }
    public String getUser_Id(){
        return this.user_Id;
    }

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }
    public boolean getIsActive(){
        return this.isActive;
    }

    public void setIsEdited(boolean isEdited){
        this.isEdited = isEdited;
    }
    public boolean getIsEdited(){
        return this.isEdited;
    }

    public void setIsSold(boolean isSold){
        this.isSold = isSold;
    }
    public boolean getIsSold(){
        return this.isSold;
    }

    public void setIsVerified(boolean isVerified){
        this.isVerified = isVerified;
    }
    public boolean getIsVerified(){
        return this.isVerified;
    }

    public void setPet_profile_Image(Bitmap pet_profile_Image){
        this.pet_profile_Image = pet_profile_Image;
    }
    public Bitmap getPet_profile_Image(){
        return this.pet_profile_Image;
    }

    public void setPet_Images_array(ArrayList<PetImages> pet_images_array){
        this.pet_Images_array = pet_images_array;
    }
    public ArrayList<PetImages> getPet_Images_array(){
        return this.pet_Images_array;
    }

    public class PetImages{
        private Bitmap relatedImage;
        private boolean isDefaultImage;

        public PetImages(){
        }

        public void setRelatedImage(Bitmap relatedImage){
            this.relatedImage = relatedImage;
        }
        public Bitmap getRelatedImage(){
            return this.relatedImage;
        }

        public void setIsDefaultImage(boolean isDefaultImage){
            this.isDefaultImage = isDefaultImage;
        }
        public boolean getIsDefaultImage(){
            return this.isDefaultImage;
        }
    }
}
