package org.example;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AggregatorTest {

    @TestFactory
    public List<DynamicTest> dynamicTestsFromFiles() {
        List<DynamicTest> dynamicTests = new ArrayList<>();

        File resourceDir = new File(getClass().getClassLoader().getResource("").getFile());
        File[] inputFiles = resourceDir.listFiles((dir, name) -> name.endsWith(".txt"));

        if (inputFiles != null) {
            for (File inputFile : inputFiles) {
                String inputFileName = inputFile.getName();
                String baseName = inputFileName.substring(0, inputFileName.lastIndexOf('.'));
                String outputFileName = baseName + ".out";

                File outputFile = new File(resourceDir, outputFileName);

                if (outputFile.exists()) {
                    dynamicTests.addAll(createDynamicTests(inputFile, outputFile));
                }
            }
        }

        return dynamicTests;
    }

    private List<DynamicTest> createDynamicTests(File inputFile, File outputFile) {
        List<Class<? extends Aggregator>> mainImplementations = findMainImplementations();

        return mainImplementations.stream()
                                  .map(implementation -> createDynamicTestForImplementation(inputFile, outputFile,
                                          implementation))
                                  .collect(Collectors.toList());
    }

    private List<Class<? extends Aggregator>> findMainImplementations() {
        List<Class<? extends Aggregator>> implementations = new ArrayList<>();
        ServiceLoader<Aggregator> loader = ServiceLoader.load(Aggregator.class);
        for (Aggregator impl : loader) {
            implementations.add(impl.getClass());
        }
        return implementations;
    }

    private DynamicTest createDynamicTestForImplementation(File inputFile, File outputFile, Class<? extends Aggregator> implementation) {
        String testName = "Test " + implementation.getSimpleName() + " with " + inputFile.getName();

        Executable testExecutable = () -> {
            Aggregator aggregator = implementation.getDeclaredConstructor().newInstance();
            String actualOutput = aggregator.calculate(inputFile.getPath());

            String expectedOutput = new String(Files.readAllBytes(outputFile.toPath())).trim();
            assertEquals(expectedOutput, actualOutput, "Mismatch between expected and actual output");
        };

        return DynamicTest.dynamicTest(testName, testExecutable);
    }
}
