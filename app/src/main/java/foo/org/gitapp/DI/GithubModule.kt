package foo.org.gitapp.DI

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import foo.org.gitapp.util.GithubRetroFitClient

/**
 * Created by Erkki Nokso-Koivisto on 5.4.2017.
 */
@Module
class GithubModule {
    @Singleton
    @Provides
    internal fun provideGithubRetrofitClient() : GithubRetroFitClient {
        val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()

        return retrofit.create(foo.org.gitapp.util.GithubRetroFitClient::class.java);
    }
}
