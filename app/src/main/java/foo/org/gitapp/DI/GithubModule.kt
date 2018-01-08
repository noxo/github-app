package foo.org.gitapp.DI

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import foo.org.gitapp.util.GithubClient

/**
 * Created by Erkki Nokso-Koivisto on 5.4.2017.
 */
@Module
class GithubModule {
    @Singleton
    @Provides
    internal fun provideGithubClient(): GithubClient {
        return GithubClient()
    }
}
