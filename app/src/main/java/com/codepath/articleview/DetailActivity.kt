package com.codepath.articleview

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

private const val TAG = "DetailActivity"

class DetailActivity : AppCompatActivity() {
    private lateinit var mediaImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var bylineTextView: TextView
    private lateinit var abstractTextView: TextView
    private lateinit var pubdateTextView: TextView
    private lateinit var newsDeskTextView: TextView
    private lateinit var sectionNameTextView: TextView
    private lateinit var snippetTextView: TextView
    private lateinit var leadParagraphTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mediaImageView = findViewById(R.id.mediaImage)
        titleTextView = findViewById(R.id.mediaTitle)
        bylineTextView = findViewById(R.id.mediaByline)
        abstractTextView = findViewById(R.id.mediaAbstract)
        pubdateTextView = findViewById(R.id.mediaPubDate)
        newsDeskTextView = findViewById(R.id.mediaNewsDesk)
        sectionNameTextView = findViewById(R.id.mediaSectionName)
        snippetTextView = findViewById(R.id.mediaSnippet)


        val article = intent.getSerializableExtra(ARTICLE_EXTRA) as Article

        titleTextView.text = article.headline
        bylineTextView.text = article.byline
        abstractTextView.text = article.leadParagraph
        pubdateTextView.text = "Published: " + article.pubDate
        newsDeskTextView.text = "NewsDesk: " + article.newsDesk
        sectionNameTextView.text = "Section: " + article.sectionName
        snippetTextView.text = "Summary: " + article.snippet

        Glide.with(this).load(article.largeImageUrl).into(mediaImageView)
    }
}