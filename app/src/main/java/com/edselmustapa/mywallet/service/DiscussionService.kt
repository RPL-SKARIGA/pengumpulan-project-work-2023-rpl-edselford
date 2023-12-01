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
    ) = try {
        repo.sendAnswer(
            DiscussionRequest(
                action = "sendAnswer",
                question_id = questionId,
                email = email,
                owner = owner,
                dateAnswered = dateAnswered,
                content = content
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }

    suspend fun createDiscussion(
        topic: String,
        content: String,
        email: String,
    ) = repo.createDiscussion(
        DiscussionRequest(
            "submitDiscussion",
            email = email,
            topic = topic,
            content = content,
        )
    )


    suspend fun deleteDiscussion(
        discussion_id: String,
        email: String
    ) = repo.deleteDiscussion(
        DiscussionRequest(
            "deleteDiscussion",
            discussion_id = discussion_id,
            email = email
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

    @POST("discussion")
    suspend fun createDiscussion(
        @Body discussReq: DiscussionRequest = DiscussionRequest("submitDiscussion")
    ): AnswerResponse

    @POST("discussion")
    suspend fun deleteDiscussion(
        @Body discussReq: DiscussionRequest = DiscussionRequest("deleteDiscussion")
    ): DeleteResponse
}


data class DiscussionRequest(
    val action: String,

    val question_id: String = "",
    val discussion_id: String = "",

    val email: String = "",
    val owner: String = "",
    val dateAnswered: String = "",
    val content: String = "",

    val topic: String = "",
    val asked: String = ""

)

data class Discussion(
    val _id: String,
    val topic: String,
    val content: String,
    val owner: String,
    val asked: Date,
    val username: String,
    val email: String
)


data class Answer(
    val _id: String,
    val owner: String,
    val dateAnswered: Date,
    val content: String,
    val question_id: String,
    val user_id: String,
    val email: String
)

data class AnswerResponse(
    val acknowledged: Boolean,
    val insertedId: String
)

data class DeleteResponse(
    val deleted: String
)