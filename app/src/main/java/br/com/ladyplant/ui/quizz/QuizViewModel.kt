package br.com.ladyplant.ui.quizz

import androidx.lifecycle.viewModelScope
import br.com.ladyplant.domain.model.DomainResult
import br.com.ladyplant.domain.model.Plant
import br.com.ladyplant.domain.model.Question
import br.com.ladyplant.domain.usecase.interfaces.GetQuizResult
import br.com.ladyplant.ui.base.BaseViewModel
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuizResult: GetQuizResult, // TODO
    override val viewState: QuizViewState,
) : BaseViewModel<QuizViewState, QuizViewAction>() {

    fun getQuestions(): List<Question> {
        return questionList
    }

    fun setSelectedAnswer(questionId: Int, ans: Int) {
        questionList.find { it.id == questionId }?.answer = ans
    }

    private fun logResult() {
        Firebase.analytics.logEvent("quiz_complete", null)
        Firebase.analytics.logEvent("q1_ans_" + questionList[0].answer.toString(), null)
        Firebase.analytics.logEvent("q2_ans_" + questionList[1].answer.toString(), null)
        Firebase.analytics.logEvent("q3_ans_" + questionList[2].answer.toString(), null)
        Firebase.analytics.logEvent("q4_ans_" + questionList[3].answer.toString(), null)
        Firebase.analytics.logEvent("q5_ans_" + questionList[4].answer.toString(), null)
        Firebase.analytics.logEvent("q6_ans_" + questionList[5].answer.toString(), null)
        Firebase.analytics.logEvent("q7_ans_" + questionList[6].answer.toString(), null)
    }

    override fun dispatchViewAction(viewAction: QuizViewAction) {
        viewModelScope.launch(Dispatchers.IO) {
            when (viewAction) {
                is QuizViewAction.GetQuizResult -> {
                    viewState.loading.postValue(true)
                    logResult()
                    when (
                        val plantResult = getQuizResult(
                            idClimate = questionList[0].answer,
                            idGardenCare = questionList[1].answer,
                            idAppearance = questionList[2].answer,
                            idLight = questionList[3].answer,
                            idInplace = questionList[4].answer,
                            idPurpose = questionList[5].answer,
                            idEatable = questionList[6].answer
                        )
                    ) {
                        is DomainResult.Success -> {
                            viewState.action.postValue(
                                QuizViewState.Action.GoToResultList(
                                    plantResult.data as ArrayList<Plant>
                                )
                            )
                            viewState.loading.postValue(false)
                        }
                        is DomainResult.Failure -> {

                            viewState.loading.postValue(false)
                        }
                    }
                }
            }
        }
    }

    companion object {
        val questionList = listOf(
            Question(
                1,
                "Let’s begin! What kind of climate are you in?",
                listOf(
                    "Tropical", "Cold", "Dry", "Mild"
                )
            ),
            Question(
                2,
                "What are your gardening skills? Be honest, there's no judgment here.",
                listOf(
                    "I can't keep dirt alive",
                    "Somewhere close to good",
                    "I definitely have a green thumb"
                )
            ),
            Question(
                3,
                "What kind of light is there where you want to put your plant?",
                listOf(
                    "Direct sunlight",
                    "There's brightness but no direct sunlight",
                    "Only artificial lights or a little brightness from the sun"
                )
            ),
            Question(
                4,
                "What are you looking for in your plant?",
                listOf(
                    "Something colorful", "Something with interesting leafs", "I don’t really mind"
                )
            ),
            Question(
                5,
                "Are you thinking of a ground plant to put on a vase, or a hanging plant to stay high?",
                listOf(
                    "Ground plant", "Hanging plant"
                )
            ),
            Question(
                6,
                "Are you looking for just a pretty plant or something with a purpose?",
                listOf(
                    "It's more about aesthetics",
                    "Something for cooking or with medicinal properties"
                )
            ),
            Question(
                7,
                "This one is very important. Do you have a small kid or pet that might eat the plant?",
                listOf(
                    "Yes, that’s a risk", "Nope, it’s all good"
                )
            ),
        )
    }
}
