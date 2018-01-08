package foo.org.gitapp.DI

import javax.inject.Singleton

import dagger.Component
import foo.org.gitapp.ui.activity.CommitsActivity
import foo.org.gitapp.ui.activity.RepositoriesActivity

/**
 * Created by Erkki Nokso-Koivisto on 5.4.2017.
 */
@Singleton
@Component(modules = arrayOf(GithubModule::class))
interface GithubComponent {
    fun inject(activity: CommitsActivity)
    fun inject(activity: RepositoriesActivity)
}
