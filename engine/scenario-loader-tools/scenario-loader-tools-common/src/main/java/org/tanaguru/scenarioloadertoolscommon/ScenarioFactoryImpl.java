package org.tanaguru.scenarioloadertoolscommon;

import org.json.JSONException;
import org.tanaguru.scenarioloader.ScenarioFactory;
import org.tanaguru.scenarioloader.ScenarioRunner;
import org.tanaguru.sebuilder.tools.ScenarioBuilder;
import org.tanaguru.selenese.tools.SeleneseBuilder;

import java.util.List;

public class ScenarioFactoryImpl implements ScenarioFactory {
    @Override
    public String make(List<String> urls, ScenarioRunner scenarioRunner) {
        String scenario = "";
        switch(scenarioRunner){
            case SEBUILDER:
                scenario = ScenarioBuilder.buildScenario(urls);
                break;

            case SELENESE:
                try {
                    scenario = SeleneseBuilder.buildFromListOfUrls("Tanaguru scenario", urls).getScenario();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        return scenario;
    }

    @Override
    public String make(String url, ScenarioRunner scenarioRunner) {
        String scenario = "";
        switch(scenarioRunner){
            case SEBUILDER:
                scenario = ScenarioBuilder.buildScenario(url);
                break;

            case SELENESE:
                try {
                    scenario = SeleneseBuilder.buildFromUrl("Tanaguru scenario", url).getScenario();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        return scenario;
    }
}