package ir.adak.Vect.Data.Models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ir.adak.Vect.UI.Activities.AddActionActivity.data_file;

public class addactionViewModel extends ViewModel {
    String Count;
    MutableLiveData<ArrayList<data_file>> liveData=new MutableLiveData<>();
    public String getCount() {
        return Count;
    }

    public void setCount(String count) {

        Count = count;
    }

    public MutableLiveData<ArrayList<data_file>> getLiveData() {
        return liveData;
    }

    public void setLiveData(MutableLiveData<ArrayList<data_file>> liveData) {
        this.liveData = liveData;
    }
}
