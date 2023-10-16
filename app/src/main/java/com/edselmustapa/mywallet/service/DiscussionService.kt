package com.edselmustapa.mywallet.service

import com.edselmustapa.mywallet.config.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.Date

class DiscussionService {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val repo: DiscussionRepo = retrofit.create(DiscussionRepo::class.java)


    suspend fun getDiscussion() = repo.getDiscussion()
    suspend fun getAnswer(questionId: String) =
        repo.getAnswer(DiscussionRequest("getAnswers", questionId))

    suspend fun sendAnswer(
        questionId: String,
        owner: String,
        dateAnswered: String,
        content: String,
        email: String
    ) = repo.sendAnswer(
        DiscussionRequest(
            "sendAnswer",
            questionId, email,
            owner, dateAnswered, content
        )
    )
}

interface DiscussionRepo {
    @POST("discussion")
    suspend fun getDiscussion(
        @Body discussReq: DiscussionRequest = DiscussionRequest("getDiscussions")
    ): List<Discussion>

    @POST("discussion")
    suspend fun getAnswer(
        @Body answerReq: DiscussionRequest = DiscussionRequest("getAnswers")
    ): List<Answer>

    @POST("discussion")
    suspend fun sendAnswer(
        @Body answerReq: DiscussionRequest = DiscussionRequest("sendAnswer")
    ): AnswerResponse
}


data class DiscussionRequest(
    val action: String,

    val question_id: String = "",

    val email: String = "",
    val owner: String = "",
    val dateAnswered: String = "",
    val content: String = "",

    )

data class Discussion(
    val _id: String,
    val topic: String,
    val content: String,
    val owner: String,
    val asked: String
)


data class Answer(
    val _id: String,
    val owner: String,
    val dateAnswered: Date,
    val content: String,
    val question_id: String,
    val user_id: String
)

data class AnswerResponse(
    val acknowledged: Boolean,
    val insertedId: String
)