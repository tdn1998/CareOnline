package com.example.dell_pro;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PanicFragment extends BottomSheetDialogFragment {

    private PanicListner listener;

    FloatingActionButton amb,fire,police;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_panic,container,false);

        amb = v.findViewById(R.id.ambulance);
        fire=v.findViewById(R.id.fire);
        police = v.findViewById(R.id.police);

        onclick();

        return v;
    }

    private void onclick() {
        amb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClicked("1");
                dismiss();
            }
        });

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClicked("2");
                dismiss();
            }
        });

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClicked("3");
                dismiss();
            }
        });
    }

    public interface PanicListner{
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (PanicListner) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    "must implement BottomSheetListner");
        }
    }
}
