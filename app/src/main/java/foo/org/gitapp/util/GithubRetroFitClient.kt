package foo.org.gitapp.util

import foo.org.gitapp.models.Commit
import foo.org.gitapp.models.Repository


/**
 * Created by noxo on 08/01/2018.
 */
interface GithubRetroFitClient {
    @retrofit2.http.GET("users/{user}/repos")
    fun getRepositories(@retrofit2.http.Path("user") githubUsername: String): io.reactivex.Observable<List<Repository>>

    @retrofit2.http.GET("repos/{user}/{repo}/commits")
    fun getCommits(@retrofit2.http.Path("user") githubUsername: String,
                   @retrofit2.http.Path("repo") githubRepositoryName: String,
                   @retrofit2.http.Query("page") page: Int = 1,
                   @retrofit2.http.Query("per_page") perPage: Int = 20): io.reactivex.Observable<List<Commit>>
}