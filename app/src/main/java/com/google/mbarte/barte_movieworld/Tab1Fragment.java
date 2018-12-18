package com.google.mbarte.barte_movieworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Tab1Fragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "Tab1Fragment";



    private Button reservebutton1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1_fragment, container, false);
        reservebutton1 = (Button)view.findViewById(R.id.reserve1);
        reservebutton1.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reserve1:

                Intent intent = new Intent(getActivity(), SelectActivity.class);
                startActivity(intent);
                break;
        }
    }
}
