package ca.mcgill.cs.table;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class AbstractRow {
    public AbstractRow() {

    }

    @NotNull
    public abstract String getFocalMehtod();

    @NotNull
    public abstract String getScenario();

    @NotNull
    public abstract String getExpectedResult();

    @NotNull
    public abstract UUID getId();

    @NotNull
    public abstract String getName();

}
