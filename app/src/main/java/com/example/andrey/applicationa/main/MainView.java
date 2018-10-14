package com.example.andrey.applicationa.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.andrey.applicationa.database.ReferenceData;

import java.util.List;

@StateStrategyType(SkipStrategy.class)
public interface MainView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setReferencesList(List<ReferenceData> referenceDataList);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateReferencesList(List<ReferenceData> referenceDataList);
    void showLoadingState(boolean flag);
}
