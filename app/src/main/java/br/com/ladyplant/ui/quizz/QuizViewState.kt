package br.com.ladyplant.ui.quizz

import br.com.ladyplant.domain.model.Plant
import br.com.ladyplant.ui.base.SingleLiveEvent

class QuizViewState {

    val action: SingleLiveEvent<Action> = SingleLiveEvent()
    val loading: SingleLiveEvent<Boolean> = SingleLiveEvent()

    sealed class Action {
        data class GoToResultList(val resultList: ArrayList<Plant>) : Action()
        data class ShowError(val errorMsg: String) : Action()
    }
}
