package com.example.cryptocurrencyjavaapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.example.cryptocurrencyjavaapplication.adapter.TopCoinRvAdapter;
import com.example.cryptocurrencyjavaapplication.adapter.sliderImageAdapter;
import com.example.cryptocurrencyjavaapplication.databinding.FragmentHomeBinding;
import com.example.cryptocurrencyjavaapplication.models.cryptolistmodel.AllMarketModel;
import com.example.cryptocurrencyjavaapplication.models.cryptolistmodel.DataItem;
import com.example.cryptocurrencyjavaapplication.room.entity.MarketListEntity;
import com.example.cryptocurrencyjavaapplication.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    MainActivity mainActivity;
    AppViewModel appViewModel;
    TopCoinRvAdapter topCoinRvAdapter;

    public List<String> top_want = Arrays.asList("BTC","ETH","BNB","ADA","XRP","DOGE","DOT","UNI","LTC","LINK");

    CompositeDisposable compositeDisposable;

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
        compositeDisposable = new CompositeDisposable();
        setupViewPager2();
        getAllMarketDataFromDb();

        return binding.getRoot();
    }


    private void setupViewPager2() {

        appViewModel.getMutableLiveData().observe((LifecycleOwner) getActivity(), new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> pics) {
                binding.viewPagerImageSlider.setAdapter(new sliderImageAdapter(pics));
                binding.viewPagerImageSlider.setOffscreenPageLimit(3);
                binding.viewPagerImageSlider.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getAllMarketDataFromDb() {
        Disposable disposable = appViewModel.getAllMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MarketListEntity>() {
                    @Override
                    public void accept(MarketListEntity marketListEntity) throws Throwable {

                        AllMarketModel allMarketModel = marketListEntity.getAllMarketModel();
//                        Log.e("TAG", "onAccept: " + allMarketModel.getRootData().getCryptoCurrencyList().get(0).getName() );
//                        Log.e("TAG", "onAccept: " + allMarketModel.getRootData().getCryptoCurrencyList().get(1).getName() );
                        ArrayList<DataItem> top10 = new ArrayList<>();
                        for (int i = 0;i < allMarketModel.getRootData().getCryptoCurrencyList().size();i++){
                            for (int j = 0; j<top_want.size();j++){
                                String coin_name = top_want.get(j);
                                if (allMarketModel.getRootData().getCryptoCurrencyList().get(i).getSymbol().equals(coin_name)){
                                    DataItem dataItem = allMarketModel.getRootData().getCryptoCurrencyList().get(i);
                                    top10.add(dataItem);
                                }
                            }
                        }

                        if (binding.TopCoinRv.getAdapter() != null) {
                            topCoinRvAdapter = (TopCoinRvAdapter) binding.TopCoinRv.getAdapter();
                            topCoinRvAdapter.updateData(top10);
                        } else {
                            topCoinRvAdapter = new TopCoinRvAdapter(top10);
                            binding.TopCoinRv.setAdapter(topCoinRvAdapter);
                        }
                    }
                });
        compositeDisposable.add(disposable);

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
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

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