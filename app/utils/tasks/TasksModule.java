package utils.tasks;

import com.google.inject.AbstractModule;
@Deprecated
public class TasksModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MyActorTask.class).asEagerSingleton();
    }

}