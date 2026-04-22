package com.jnetai.mindwell.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jnetai.mindwell.BuildConfig
import com.jnetai.mindwell.R
import com.jnetai.mindwell.databinding.ActivityAboutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AboutActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityAboutBinding
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "About MindWell"

        binding.tvVersion.text = "Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        binding.tvGithubUrl.text = "github.com/jnetai-clawbot/MindWell"

        binding.btnCheckUpdate.setOnClickListener { checkForUpdates() }
        binding.btnShare.setOnClickListener { shareApp() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun checkForUpdates() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnCheckUpdate.isEnabled = false
        binding.tvUpdateStatus.text = "Checking..."

        launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val url = URL("https://api.github.com/repos/jnetai-clawbot/MindWell/releases/latest")
                    val connection = url.openConnection()
                    connection.setRequestProperty("Accept", "application/vnd.github.v3+json")
                    connection.connect()
                    val response = connection.getInputStream().bufferedReader().readText()
                    val json = JSONObject(response)
                    json.optString("tag_name", "") to json.optString("html_url", "")
                }

                val (remoteVersion, releaseUrl) = result
                if (remoteVersion.isEmpty()) {
                    binding.tvUpdateStatus.text = "No releases found yet"
                } else {
                    val currentVersion = "v${BuildConfig.VERSION_NAME}"
                    if (remoteVersion != currentVersion) {
                        binding.tvUpdateStatus.text = "Update available: $remoteVersion"
                        binding.btnDownloadUpdate.visibility = View.VISIBLE
                        binding.btnDownloadUpdate.setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(releaseUrl)))
                        }
                    } else {
                        binding.tvUpdateStatus.text = "You're up to date ($currentVersion)"
                    }
                }
            } catch (e: Exception) {
                binding.tvUpdateStatus.text = "Error checking updates: ${e.message}"
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnCheckUpdate.isEnabled = true
            }
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "MindWell - Mental Health Companion")
            putExtra(Intent.EXTRA_TEXT, "Check out MindWell, a mental health companion app!\n\nhttps://github.com/jnetai-clawbot/MindWell")
        }
        startActivity(Intent.createChooser(shareIntent, "Share MindWell"))
    }
}