package com.first_player_games.ludoApi.model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.first_player_games.Api.LudoApiRepository;
import com.first_player_games.Api.Resource;
import com.first_player_games.ludoApi.TableMaster;

import java.util.List;

public class LudoViewModel extends ViewModel {

    private static LudoViewModel mInstance;

    public static synchronized LudoViewModel getInstance(){

        if(mInstance == null)
            mInstance = new LudoViewModel();

        return mInstance;
    }

    private LudoApiRepository apiRepository;
    LiveData<Resource<List<TableMaster.TableDatum>>> tablemasterLivedata;
    LiveData<Resource<String>> getTableData;
    LiveData<Resource<Integer>> roleDiceLiveData;
    Context context;

    public LudoViewModel init(Context context) {
        try {
            if (context != null) {
                this.context = context;
                apiRepository = LudoApiRepository.getInstance();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void loadTableMaster(){
      tablemasterLivedata = apiRepository.call_api_getTableMaster();
    }

    public LiveData<Resource<List<TableMaster.TableDatum>>> getTableMaster(){
        return tablemasterLivedata;
    }

    public void loadgetTable(String boot_value,String room_id){
      getTableData = apiRepository.call_api_getTable(boot_value,room_id);
    }

    public LiveData<Resource<String>> getTableData(){
        return getTableData;
    }

    public void loadDiceStep(){
        roleDiceLiveData = apiRepository.call_api_rolldice();
    }

    public LiveData<Resource<Integer>> getDiceSteps(){
        return roleDiceLiveData;
    }

}
