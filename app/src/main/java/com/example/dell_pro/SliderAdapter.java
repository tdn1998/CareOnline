package com.example.dell_pro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {
    Context context;
    List<ImageSliderModel> imageSliderModelList;

    public SliderAdapter(Context context, List<ImageSliderModel> imageSliderModelList) {
        this.context = context;
        this.imageSliderModelList = imageSliderModelList;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slideritemlayout,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        viewHolder.sliderImageView.setImageResource(imageSliderModelList.get(position).getImage());
        viewHolder.sliderText.setText(imageSliderModelList.get(position).getSliderText());
        viewHolder.sliderText.setTextSize(25);
    }

    @Override
    public int getCount() {
        return imageSliderModelList.size();
    }

    class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView sliderImageView;
        TextView sliderText;

        public SliderViewHolder(View itemView) {
            super(itemView);
            sliderImageView = itemView.findViewById(R.id.slider_image_view);
            sliderText = itemView.findViewById(R.id.slider_text);
        }
    }
}