package com.codepath.articleview
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlin.text.removeSurrounding
import java.io.Serializable

data class Article(
    @SerializedName("headline")
    val headline: String?,
    @SerializedName("abstract")
    val abstract: String?,
    @SerializedName("byline")
    val byline: String?,
    @SerializedName("pub_date")
    val pubDate: String?,
    @SerializedName("news_desk")
    val newsDesk: String?,
    @SerializedName("section_name")
    val sectionName: String?,
    @SerializedName("snippet")
    val snippet: String?,
    @SerializedName("lead_paragraph")
    val leadParagraph: String?,
    val smallImageUrl: String?,
    val largeImageUrl: String?,
    //@SerializedName("web_url")
    //val webUrl: String?
) : Serializable