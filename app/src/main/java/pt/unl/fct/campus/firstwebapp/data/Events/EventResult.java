package pt.unl.fct.campus.firstwebapp.data.Events;

import androidx.annotation.Nullable;

public class EventResult {

    @Nullable
    private EventCreatedView success;
    @Nullable
    private Integer error;

    EventResult(@Nullable Integer error) {
        this.error = error;
    }

    EventResult(@Nullable EventCreatedView success) {
        this.success = success;
    }

    @Nullable
    EventCreatedView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
