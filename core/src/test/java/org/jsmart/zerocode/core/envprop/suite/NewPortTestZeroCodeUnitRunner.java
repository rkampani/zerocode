package org.jsmart.zerocode.core.envprop.suite;

import org.jsmart.simulator.main.SimpleRestJsonSimulatorsMain;
import org.jsmart.zerocode.core.runner.ZeroCodePackageRunner;
import org.jsmart.zerocode.core.utils.SmartUtils;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewPortTestZeroCodeUnitRunner extends ZeroCodePackageRunner {
    SimpleRestJsonSimulatorsMain simulator ;
    public static final int PORT = 9997;
    private static final Logger logger = LoggerFactory.getLogger(NewPortTestZeroCodeUnitRunner.class);

    public NewPortTestZeroCodeUnitRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        logger.info("#################starting SimpleRestJsonSimulatorsMain with port "+PORT);
        simulator = new SimpleRestJsonSimulatorsMain(PORT);
        simulator.start();
    }

    public NewPortTestZeroCodeUnitRunner(Class<?> testClass, SmartUtils smartUtils) throws InitializationError {
        super(testClass, smartUtils);
    }

}