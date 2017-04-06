package foo.org.gitapp.DI;

import javax.inject.Singleton;

import dagger.Component;
import foo.org.gitapp.ui.activity.CommitsActivity;
import foo.org.gitapp.ui.activity.RepositoriesActivity;

/**
 * Created by Erkki Nokso-Koivisto on 5.4.2017.
 */
@Singleton
@Component(modules = {GithubModule.class})
public interface GithubComponent {
    void inject(CommitsActivity activity);
    void inject(RepositoriesActivity activity);
}
