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
import com.example.cryptocurrencyjavaapplication.databinding.FragmentWatchListBinding;

public class WatchListFragment extends Fragment {

    FragmentWatchListBinding binding;
    MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.fragment_watch_list,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolBar(view);
    }
    private void setupToolBar(View view){
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.watchListFragment).setOpenableLayout(mainActivity.drawerLayout).build();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.watchListFragment) {
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_24);
                    toolbar.setTitle("Watch List");
                }
            }
        });
    }
}