package com.yelp.clientlib.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.yelp.clientlib.annotation.Nullable;

import java.io.Serializable;

@AutoValue
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = AutoValue_User.Builder.class)
public abstract class User implements Serializable {

    public abstract String id();

    @Nullable
    public abstract String imageUrl();

    @Nullable
    public abstract String name();

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    @JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
    public abstract static class Builder {

        public abstract Builder id(String id);

        public abstract Builder imageUrl(String imageUrl);

        public abstract Builder name(String name);

        public abstract User build();
    }

    public static Builder builder() {
        return new AutoValue_User.Builder();
    }
}
