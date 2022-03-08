package com.example.cryptocurrencyjavaapplication.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cryptocurrencyjavaapplication.MainActivity;
import com.example.cryptocurrencyjavaapplication.R;
import com.example.cryptocurrencyjavaapplication.databinding.FragmentProfileBinding;

import me.ibrahimsn.lib.SmoothBottomBar;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding fragmentProfileBinding;
    MainActivity mainActivity;
    SmoothBottomBar smoothBottomBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.fragment_profile,container,false);
        return fragmentProfileBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //bottom navigation view becomes Gone
        smoothBottomBar = requireActivity().findViewById(R.id.bottomBar);
        smoothBottomBar.setVisibility(View.GONE);

        setupToolbar(view);
    }
    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.profileFragment).build();
        Toolbar profileToolbar = view.findViewById(R.id.profiletoolbar);
        NavigationUI.setupWithNavController(profileToolbar,navController,appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.profileFragment){
                    profileToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
                    profileToolbar.setTitle("Profile");
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    //when destroy this fragment, bottom navigation becomes visible
    @Override
    public void onDestroy() {
        super.onDestroy();
        smoothBottomBar.setVisibility(View.VISIBLE);
    }
}

