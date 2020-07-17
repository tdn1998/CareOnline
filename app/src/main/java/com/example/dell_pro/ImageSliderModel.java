package com.example.dell_pro;

public class ImageSliderModel {
    int image;
    String SliderText;

    public ImageSliderModel() {
    }

    public ImageSliderModel(int image, String sliderText) {
        this.image = image;
        SliderText = sliderText;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getSliderText() {
        return SliderText;
    }

    public void setSliderText(String sliderText) {
        SliderText = sliderText;
    }
}
