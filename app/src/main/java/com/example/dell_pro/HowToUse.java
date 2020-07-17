package com.example.dell_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HowToUse extends AppCompatActivity {
     SliderView sliderView;
     List<ImageSliderModel> imageSliderModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
        imageSliderModelList=new ArrayList<>();
        sliderView=findViewById(R.id.imageSlider);

        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_a,"Welcome"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_b,"Login"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_c,"Register"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_d,"Side Navigation"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_e,"Main Menu"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_f,"Personal Checkup"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_g,"Medical Report"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_ecg,"ECG"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_h,"Chat With Doctor"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_i,"Chatbot"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_j,"Nearby Health Services"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_k,"Med Reminder"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_l,"BMI Calculator"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_m,"News"));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.app_n,"settings"));

        sliderView.setSliderAdapter(new SliderAdapter(this,imageSliderModelList));




        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);

        sliderView.setIndicatorSelectedColor(Color.parseColor("#E91E63"));

        sliderView.setIndicatorUnselectedColor(Color.parseColor("#25955369"));
        sliderView.setScrollTimeInSec(3);
        sliderView.startAutoCycle();


    }
}