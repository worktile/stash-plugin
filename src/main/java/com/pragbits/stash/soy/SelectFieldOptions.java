package com.pragbits.stash.soy;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class SelectFieldOptions {

    private final SelectFieldOption[] options;

    public SelectFieldOptions(SelectFieldOption[] options) {
        this.options = options;
    }

    public List<Map<String, String>> toSoyStructure() {
        return Lists.transform(Lists.newArrayList(options), new Function<SelectFieldOption, Map<String, String>>() {
            @Override
            public Map<String, String> apply(SelectFieldOption level) {
                return ImmutableMap.of("text", level.text(), "value", level.value());
            }
        });
    }

}
