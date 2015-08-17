package com.pragbits.stash;

import com.atlassian.stash.repository.Repository;
import javax.annotation.Nonnull;

public interface SlackSettingsService {

    @Nonnull
    SlackSettings getSlackSettings(@Nonnull Repository repository);

    @Nonnull
    SlackSettings setSlackSettings(@Nonnull Repository repository, @Nonnull SlackSettings settings);

}
