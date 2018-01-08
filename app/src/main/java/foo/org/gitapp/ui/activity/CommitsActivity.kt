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

import javax.inject.Inject
import butterknife.BindView
import butterknife.ButterKnife
import foo.org.gitapp.DI.DaggerGithubComponent
import foo.org.gitapp.DI.GithubModule
import foo.org.gitapp.R
import foo.org.gitapp.ui.adapters.CommitListAdapter
import foo.org.gitapp.util.GithubRetroFitClient
import io.reactivex.schedulers.Schedulers

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
    lateinit var githubRetroClient: GithubRetroFitClient

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

        githubRetroClient.getCommits(githubUsername, githubRepositoryName)
                .subscribeOn(Schedulers.computation())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe ({
                    result ->
                    showProgress(false)
                    commitListAdapter.update(result)
                }, { error ->
                    showProgress(false)
                    showError(error.toString())
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
