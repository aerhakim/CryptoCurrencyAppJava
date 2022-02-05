package com.example.cryptocurrencyjavaapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cryptocurrencyjavaapplication.databinding.ActivityMainBinding;
import com.example.cryptocurrencyjavaapplication.models.cryptolistmodel.AllMarketModel;
import com.example.cryptocurrencyjavaapplication.viewmodel.AppViewModel;

import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavHostFragment navHostFragment;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    public DrawerLayout drawerLayout;
    AppViewModel appViewModel;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawerLayout = binding.drawerLayout;

        // start observe
        compositeDisposable = new CompositeDisposable();

        setAppViewModel();
        setupNavigationComponent();
        callListApiRequest();
    }

    private void setAppViewModel() {
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }


    private void callListApiRequest() {
        Observable.interval(20, TimeUnit.SECONDS)
                .flatMap(n -> appViewModel.marketFutureCall().get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllMarketModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull AllMarketModel allMarketModel) {

                        Log.e("TAG", "onNext: " + allMarketModel.getRootData().getCryptoCurrencyList().get(0).getName() );
                        Log.e("TAG", "onNext: " + allMarketModel.getRootData().getCryptoCurrencyList().get(1).getName() );

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.e("TAG", "onError: ");
                    }

                    @Override
                    public void onComplete() {

                        Log.e("TAG", "onComplete: " );
                    }
                });
    }

    private void setupNavigationComponent() {
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        //setup drawer layout
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.marketFragment, R.id.watchListFragment).setOpenableLayout(binding.drawerLayout).build();

        NavigationUI.setupWithNavController(binding.navigationView, navController);

        setupSmoothBottomNavigation();
    }

    private void setupSmoothBottomNavigation() {
        PopupMenu popupMenu = new PopupMenu(this, null);
        popupMenu.inflate(R.menu.menu);
        Menu menu = popupMenu.getMenu();
        binding.bottomBar.setupWithNavController(menu, navController);
//        binding.bottomBar.setupWithNavController(menu, navController)
    }


    // when main activity destroyed, finish observable
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}