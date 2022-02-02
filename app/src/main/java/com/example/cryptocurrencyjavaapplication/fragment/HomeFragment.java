package com.example.cryptocurrencyjavaapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cryptocurrencyjavaapplication.MainActivity;
import com.example.cryptocurrencyjavaapplication.R;
import com.example.cryptocurrencyjavaapplication.adapter.sliderImageAdapter;
import com.example.cryptocurrencyjavaapplication.databinding.FragmentHomeBinding;
import com.example.cryptocurrencyjavaapplication.viewmodel.AppViewModel;

import java.util.ArrayList;

import javax.inject.Inject;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    MainActivity mainActivity;
    AppViewModel appViewModel;

    @Inject
    String name;

    // get instance of main activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_home, container, false);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        setupViewPager2();

        return binding.getRoot();
    }


    private void setupViewPager2(){

        appViewModel.getMutableLiveData().observe((LifecycleOwner) getActivity(), new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> pics) {
                binding.viewPagerImageSlider.setAdapter(new sliderImageAdapter(pics));
                binding.viewPagerImageSlider.setOffscreenPageLimit(3);
                binding.viewPagerImageSlider.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment).setOpenableLayout(mainActivity.drawerLayout).build();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.homeFragment) {
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_24);
                    toolbar.setTitle("Home");
                }
            }
        });
    }
}