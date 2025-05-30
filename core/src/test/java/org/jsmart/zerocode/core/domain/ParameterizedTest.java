package org.jsmart.zerocode.core.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import org.jsmart.zerocode.core.di.main.ApplicationMainModule;
import org.jsmart.zerocode.core.guice.ZeroCodeGuiceTestRule;
import org.jsmart.zerocode.core.di.provider.CsvParserProvider;
import org.jsmart.zerocode.core.utils.SmartUtils;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class ParameterizedTest {

    @Rule
    public ZeroCodeGuiceTestRule guiceRule = new ZeroCodeGuiceTestRule(this, ParameterizedTest.ZeroCodeTestModule.class);
    public static class ZeroCodeTestModule extends AbstractModule {
        @Override
        protected void configure() {
            ApplicationMainModule applicationMainModule = new ApplicationMainModule("config_hosts_test.properties");
            install(applicationMainModule);
        }
    }

    @Inject
    SmartUtils smartUtils;

    @Inject
    private ObjectMapper mapper;

    @Inject
    private CsvParserProvider csvParser;

    @Test
    public void testSerDe_valueSource() throws Exception {
        String jsonDocumentAsString =
                smartUtils.getJsonDocumentAsString("unit_test_files/engine_unit_test_jsons/08_parameterized.json");
        Parameterized parameterized = mapper.readValue(jsonDocumentAsString, Parameterized.class);

        assertThat(parameterized.getValueSource(), hasItem("hello"));
        assertThat(parameterized.getValueSource(), hasItem(123));
        assertThat(parameterized.getValueSource(), hasItem(true));

        String actualJson = mapper.writeValueAsString(parameterized);
        assertThat(actualJson, is("{\"valueSource\":[\"hello\",123,true],\"csvSource\":[\"1,        2,        200\",\"11,      22,        400\"]}"));
    }

    @Test
    public void testSerDe_csvSource() throws Exception {
        String jsonDocumentAsString =
                smartUtils.getJsonDocumentAsString("unit_test_files/engine_unit_test_jsons/08_parameterized.json");
        Parameterized parameterized = mapper.readValue(jsonDocumentAsString, Parameterized.class);

        assertThat(parameterized.getCsvSource(), hasItem("1,        2,        200"));
        assertThat(parameterized.getCsvSource(), hasItem("11,      22,        400"));
    }

    @Test
    public void shouldReadCsvSourceFromCsvFile() throws IOException {
        //given
        String jsonDocumentAsString =
                smartUtils.getJsonDocumentAsString("unit_test_files/engine_unit_test_jsons/08.1_parameterized_csv_source_from_file.json");

        //when
        Parameterized parameterized = mapper.readValue(jsonDocumentAsString, Parameterized.class);

        //then
        assertThat(parameterized.getCsvSource(), hasItem("octocat,The Octocat,San Francisco,583231"));
        assertThat(parameterized.getCsvSource(), hasItem("siddhagalaxy,Sidd,UK,33847730"));
    }

    @Test
    public void shouldReadCsvSourceFromCsvFileIgnoringHeader() throws IOException {
        //given
        String jsonDocumentAsString =
                smartUtils.getJsonDocumentAsString("unit_test_files/engine_unit_test_jsons/08.2_parameterized_csv_source_from_file_containing_header.json");

        //when
        Parameterized parameterized = mapper.readValue(jsonDocumentAsString, Parameterized.class);

        //then
        assertThat(parameterized.getCsvSource(), hasItem("octocat,The Octocat,San Francisco,583231"));
        assertThat(parameterized.getCsvSource(), hasItem("siddhagalaxy,Sidd,UK,33847730"));
        assertThat(parameterized.getCsvSource(), everyItem(not(is("user,name,city,userid"))));//assert header is ignored
    }

}