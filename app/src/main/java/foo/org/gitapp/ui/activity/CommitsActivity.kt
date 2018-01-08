package foo.org.gitapp.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar

import com.google.gson.Gson
import com.orhanobut.logger.Logger

import java.util.Arrays

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import foo.org.gitapp.DI.DaggerGithubComponent
import foo.org.gitapp.DI.GithubModule
import foo.org.gitapp.R
import foo.org.gitapp.models.Commit
import foo.org.gitapp.ui.adapters.CommitListAdapter
import foo.org.gitapp.util.GithubClient

/**
 * Created by enoks on 22.2.2017.
 */

class CommitsActivity : AppCompatActivity() {

    @BindView(R.id.commitsListView)
    lateinit var chatMessagesList: ListView
    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.progress)
    lateinit var mProgressView: ProgressBar
    @BindView(R.id.mainLayout)
    lateinit var coordinatorLayout: CoordinatorLayout
    @Inject
    lateinit var githubClient: GithubClient

    lateinit var commitListAdapter: CommitListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_commits)
        ButterKnife.bind(this)

        DaggerGithubComponent.builder().githubModule(GithubModule()).build().inject(this)

        commitListAdapter = CommitListAdapter(this)
        chatMessagesList.adapter = commitListAdapter

        setSupportActionBar(toolbar)
        pullCommits()

    }

    private fun pullCommits() {

        val githubUsername = intent.getStringExtra("user")
        val githubRepositoryName = intent.getStringExtra("repository")

        showProgress(true)

        githubClient.getCommits(this, githubUsername, githubRepositoryName, object : GithubClient.GithubClientHandler {
            override fun success(jsonString: String) {
                val gson = Gson()
                val commits = Arrays.asList(*gson.fromJson<Array<Commit>>(jsonString, Array<Commit>::class.java!!))
                commitListAdapter.update(commits)
                Logger.i("messages pulled")
                showProgress(false)
            }

            override fun fail(error: Throwable, frienlyErrorMsg: String) {
                Logger.e(error, "failed pulling messages")
                showProgress(false)
                showError(frienlyErrorMsg)
            }
        })

    }

    private fun showError(msg: String) {
        val snackbar = Snackbar
                .make(coordinatorLayout!!, msg, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.RED)
        snackbar.show()
    }

    private fun showProgress(show: Boolean) {
        mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
    }
}
