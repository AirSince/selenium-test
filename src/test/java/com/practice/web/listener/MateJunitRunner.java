package com.practice.web.listener;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class MateJunitRunner extends SpringJUnit4ClassRunner {

    public MateJunitRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new MateTestListener());
        notifier.fireTestRunStarted(getDescription());
        super.run(notifier);
    }


}
