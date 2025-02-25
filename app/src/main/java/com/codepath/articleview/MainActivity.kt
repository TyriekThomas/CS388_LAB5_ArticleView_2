package com.codepath.articleview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.articleview.BuildConfig
import com.codepath.articleview.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import okhttp3.Headers
import org.json.JSONException

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "MainActivity/"
private val SEARCH_API_KEY = BuildConfig.API_KEY
private val ARTICLE_SEARCH_URL =
    "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=${SEARCH_API_KEY}"

class MainActivity : AppCompatActivity() {
    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private lateinit var articleAdapter: ArticleAdapter
    private val articles = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        articlesRecyclerView = findViewById(R.id.articles)
        // Set up ArticleAdapter with articles
        articleAdapter = ArticleAdapter(this, articles)
        articlesRecyclerView.adapter = articleAdapter
        articlesRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            articlesRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        val client = AsyncHttpClient()
        client.get(ARTICLE_SEARCH_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch articles: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched articles: $json")
                try {
                    // Create the parsedJSON
                    val parsedJSON = createJson().parseToJsonElement(json.jsonObject.toString()).jsonObject
                    val docs = parsedJSON["response"]?.jsonObject?.get("docs")?.jsonArray
                    if (docs != null) {
                        for (doc in docs) {
                            val articleJson = doc.jsonObject
                            val multimedia = articleJson["multimedia"]?.jsonArray
                            val smallImageUrl = multimedia?.find {
                                it.jsonObject["subtype"]?.toString()?.removeSurrounding("\"") == "xlarge"
                            }?.jsonObject?.get("url")?.toString()?.removeSurrounding("\"")?.let { "https://www.nytimes.com/$it" }
                            val largeImageUrl = multimedia?.find {
                                it.jsonObject["subtype"]?.toString()?.removeSurrounding("\"") == "xlarge"
                            }?.jsonObject?.get("url")?.toString()?.removeSurrounding("\"")?.let { "https://www.nytimes.com/$it" }
                            val bylineOriginal = articleJson["byline"]?.jsonObject?.get("original")?.toString()?.removeSurrounding("\"")

                            val article = Article(
                                headline = articleJson["headline"]?.jsonObject?.get("main")?.toString()?.removeSurrounding("\""),
                                abstract = articleJson["abstract"]?.toString()?.removeSurrounding("\""),
                                byline = bylineOriginal,
                                pubDate = articleJson["pub_date"]?.toString()?.removeSurrounding("\""),
                                newsDesk = articleJson["news_desk"]?.toString()?.removeSurrounding("\""),
                                sectionName = articleJson["section_name"]?.toString()?.removeSurrounding("\""),
                                snippet = articleJson["snippet"]?.toString()?.removeSurrounding("\""),
                                leadParagraph = articleJson["lead_paragraph"]?.toString()?.removeSurrounding("\""),
                                smallImageUrl = smallImageUrl,
                                largeImageUrl = largeImageUrl
                                //webUrl = articleJson["web_url"]?.toString()?.removeSurrounding("\"")
                            )
                            articles.add(article)
                        }
                        articleAdapter.notifyDataSetChanged()
                    }

                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }

        })

    }
}