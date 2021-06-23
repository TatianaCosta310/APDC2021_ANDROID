package pt.unl.fct.campus.firstwebapp.data.Events;

import androidx.annotation.Nullable;

public class EventFormState {
    /**
     * Data validation state of the event form.
     */

        @Nullable
        private Integer startDate;

        @Nullable
        private Integer finalDate;

    @Nullable
    private Integer startHour;

    @Nullable
    private Integer finalHour;
        @Nullable
        private Integer origin;

        @Nullable
        private Integer place;

    @Nullable
    private Integer name;

    private boolean isDataValid;

        EventFormState(@Nullable Integer name, @Nullable Integer startDate, @Nullable Integer finalDate, @Nullable Integer startHour, @Nullable Integer finalHour, @Nullable Integer origin,@Nullable Integer place) {
            this.startDate = startDate;
            this.finalDate = finalDate;
            this.startHour = startHour;
            this.finalHour = finalHour;
            this.origin = origin;
            this.place = place;
            this.isDataValid = false;
        }

    EventFormState(boolean isDataValid) {
            this.startDate = null;
            this.finalDate = null;
            this.finalHour = null;
            this.startHour = null;
            this.origin = null;
            this.place = null;
            this.isDataValid = isDataValid;
        }

        @Nullable
        Integer getStartDateError() {
            return startDate;
        }

        @Nullable
        Integer getFinalDateError() {
            return finalDate;
        }

    @Nullable
    Integer getStartHour(){return startHour;}

    @Nullable
    Integer getFinalHour(){return finalHour;}

        @Nullable
        Integer getOriginError(){return origin;}

        @Nullable
        Integer getPlaceError(){return place;}

        boolean isDataValid() {
            return isDataValid;
        }
    }

