package pt.unl.fct.campus.firstwebapp.data.Events;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;


    public class EventViewModelFactory implements ViewModelProvider.Factory {

        private Executor executor;

        public EventViewModelFactory(Executor executor){
            this.executor = executor;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(EventViewModel.class)) {
                return (T) new EventViewModel(EventRepository.getInstance(new EventDataSource()),executor);
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }
    }
