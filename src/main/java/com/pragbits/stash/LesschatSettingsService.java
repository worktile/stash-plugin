package com.pragbits.stash;

import com.atlassian.stash.repository.Repository;
import javax.annotation.Nonnull;

public interface LesschatSettingsService {

    @Nonnull
    LesschatSettings getlesschatSettings(@Nonnull Repository repository);

    @Nonnull
    LesschatSettings setlesschatSettings(@Nonnull Repository repository, @Nonnull LesschatSettings settings);

}
