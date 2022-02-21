package com.example.cryptocurrencyjavaapplication.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.cryptocurrencyjavaapplication.adapter.marketRV_Adapter;
import com.example.cryptocurrencyjavaapplication.databinding.FragmentMarketBinding;
import com.example.cryptocurrencyjavaapplication.models.cryptolistmodel.AllMarketModel;
import com.example.cryptocurrencyjavaapplication.models.cryptolistmodel.DataItem;
import com.example.cryptocurrencyjavaapplication.room.entity.MarketListEntity;
import com.example.cryptocurrencyjavaapplication.viewmodel.AppViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MarketFragment extends Fragment {

    FragmentMarketBinding binding;
    MainActivity mainActivity;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppViewModel appViewModel;
    CompositeDisposable compositeDisposable;
    List<DataItem> dataItemList;
    marketRV_Adapter marketRVAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.fragment_market,container,false);

        compositeDisposable = new CompositeDisposable();
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        getMarketListDataFromDb();
        return binding.getRoot();
    }

    private void getMarketListDataFromDb(){
        Disposable completable = appViewModel.getAllMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MarketListEntity>() {
                    @Override
                    public void accept(MarketListEntity marketListEntity) throws Throwable {
                        AllMarketModel allMarketModel = marketListEntity.getAllMarketModel();
                        dataItemList = allMarketModel.getRootData().getCryptoCurrencyList();


                        if (binding.marketRv.getAdapter() == null){
                            marketRVAdapter = new marketRV_Adapter((ArrayList<DataItem>) dataItemList);
                            binding.marketRv.setAdapter(marketRVAdapter);
                        }else {
                            marketRVAdapter = (marketRV_Adapter) binding.marketRv.getAdapter();
                            marketRVAdapter.updateData((ArrayList<DataItem>) dataItemList);
                        }

                    }
                });

        compositeDisposable.add(completable);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);
    }
    private void setupToolbar(View view) { 
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.marketFragment).setOpenableLayout(mainActivity.drawerLayout).build();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_market_tb);
        NavigationUI.setupWithNavController(collapsingToolbarLayout,toolbar,navController,appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
               if (destination.getId()==R.id.marketFragment){
                   collapsingToolbarLayout.setTitleEnabled(false);
                   toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_24);
                   toolbar.setTitle("Market");
                   toolbar.setTitleTextColor(Color.WHITE);
               }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}