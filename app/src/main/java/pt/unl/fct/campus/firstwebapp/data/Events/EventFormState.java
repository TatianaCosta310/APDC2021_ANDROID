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
        private Integer numVolunteers;

        @Nullable
        private Integer goals;

    @Nullable
    private Integer description;

    @Nullable
    private Integer name;

    private boolean isDataValid;

        EventFormState(@Nullable Integer name, @Nullable Integer startDate, @Nullable Integer finalDate, @Nullable Integer startHour, @Nullable Integer finalHour, @Nullable Integer numVolunteers,@Nullable Integer goals,@Nullable Integer description) {

            this.name = name;
            this.startDate = startDate;
            this.finalDate = finalDate;
            this.startHour = startHour;
            this.finalHour = finalHour;
            this.numVolunteers = numVolunteers;
            this.goals = goals;
            this.description = description;
            this.isDataValid = false;
        }

    EventFormState(boolean isDataValid) {
            this.name = null;
            this.startDate = null;
            this.finalDate = null;
            this.finalHour = null;
            this.startHour = null;
            this.numVolunteers = null;
            this.goals = null;
            this.description = null;
            this.isDataValid = isDataValid;
        }


    @Nullable
    Integer getName() {
        return name;
    }

        @Nullable
        Integer getStartDate() {
            return startDate;
        }

        @Nullable
        Integer getFinalDate() {
            return finalDate;
        }

    @Nullable
    Integer getStartHour(){return startHour;}

    @Nullable
    Integer getFinalHour(){return finalHour;}

        @Nullable
        Integer getNumVolunteers(){return numVolunteers;}

        @Nullable
        Integer getGoals(){return goals;}

    @Nullable
    Integer getDescription(){return description;}

        boolean isDataValid() {
            return isDataValid;
        }
    }

