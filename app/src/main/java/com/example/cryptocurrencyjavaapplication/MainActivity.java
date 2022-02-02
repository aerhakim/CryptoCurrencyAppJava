package com.example.cryptocurrencyjavaapplication;

import android.os.Bundle;
import android.view.Menu;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cryptocurrencyjavaapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavHostFragment navHostFragment;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        drawerLayout = binding.drawerLayout;
        setupNavigationComponent();
    }

    private void setupNavigationComponent() {
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        //setup drawer layout
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.marketFragment, R.id.watchListFragment).setOpenableLayout(binding.drawerLayout).build();

        NavigationUI.setupWithNavController(binding.navigationView,navController);

        setupSmoothBottomNavigation();
    }

    private void setupSmoothBottomNavigation() {
        PopupMenu popupMenu = new PopupMenu(this, null);
        popupMenu.inflate(R.menu.menu);
        Menu menu = popupMenu.getMenu();
        binding.bottomBar.setupWithNavController(menu, navController);
//        binding.bottomBar.setupWithNavController(menu, navController)
    }
}