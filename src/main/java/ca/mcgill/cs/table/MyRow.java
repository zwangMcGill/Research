package ca.mcgill.cs.table;

import com.intellij.openapi.util.Comparing;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MyRow extends AbstractRow {

    private final UUID id;

    @NotNull
    private String name;

    private String focalMehtod;

    private String scenario;

    private String expectedResult;

    private boolean active;

    /**
     * 保存当前表格数据
     */

    public MyRow(@NotNull UUID id,
                 @NotNull String name,
                 String focalMethod,
                 String scenario,
                 String expectedResult,
                 boolean active) {
        this.id = id;
        this.name = name;
        this.focalMehtod = focalMethod;
        this.scenario = scenario;
        this.expectedResult = expectedResult;
        this.active = active;
    }

    public void setFocalMethod(@NotNull String value) {
        focalMehtod = value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean value) {
        active = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyRow)) {
            return false;
        }

        MyRow browser = (MyRow) o;
        return getId().equals(browser.getId()) &&
                active == browser.active &&
                Comparing.strEqual(name, browser.name);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String value) {
        name = value;
    }

    @Override
    public String getFocalMehtod() {
        return focalMehtod;
    }

    @Override
    public String getScenario() {
        return scenario;
    }

    public void setScenario(@NotNull String value) {
        scenario = value;
    }

    @Override
    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(@NotNull String value) {
        expectedResult = value;
    }

    @Override
    @NotNull
    public final UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MyRow{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", focalMehtod='" + focalMehtod + '\'' +
                ", scenario='" + scenario + '\'' +
                ", expectedResult='" + expectedResult + '\'' +
                '}';
    }
}
