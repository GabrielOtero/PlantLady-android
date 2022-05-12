package br.com.ladyplant.di

import br.com.ladyplant.ui.result.ResultListViewState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewStateModule {

    @Provides
    fun bindResultListViewState(): ResultListViewState {
        return ResultListViewState()
    }
}