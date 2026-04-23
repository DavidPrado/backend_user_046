package com.baser_users.matchers;

import org.springframework.data.domain.ExampleMatcher;

public class UserMatchers {

    public static ExampleMatcher listUsersFilter() {
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreCase(false)
                .withMatcher("id", m -> m.exact().ignoreCase(false))
                .withMatcher("name", m -> m.contains().ignoreCase())
                .withMatcher("email", m -> m.contains().ignoreCase())
                .withMatcher("code", ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("password", "mustChangePassword");
    }

    public static ExampleMatcher strictUserSearch() {
        return ExampleMatcher.matchingAll()
                .withIgnoreNullValues();
    }
}
