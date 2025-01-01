package com.inomera.integration.config.model;

import java.io.Serializable;
import java.util.List;

public class AdapterLogging implements Serializable {
    private LogStrategy strategy;
    private List<String> sensitiveFields;
    private List<String> nonLoggingFields;

    public AdapterLogging() {
    }

    public AdapterLogging(LogStrategy strategy) {
        this.strategy = strategy;
    }

    public AdapterLogging(LogStrategy strategy, List<String> sensitiveFields, List<String> nonLoggingFields) {
        this.strategy = strategy;
        this.sensitiveFields = sensitiveFields;
        this.nonLoggingFields = nonLoggingFields;
    }

    private AdapterLogging(Builder builder) {
        this.strategy = builder.strategy;
        this.sensitiveFields = builder.sensitiveFields;
        this.nonLoggingFields = builder.nonLoggingFields;
    }

    public static AdapterLogging defaultAdapterLogging() {
        return new AdapterLogging(LogStrategy.REQ_RES);
    }

    public LogStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(LogStrategy strategy) {
        this.strategy = strategy;
    }

    public List<String> getSensitiveFields() {
        return sensitiveFields;
    }

    public void setSensitiveFields(List<String> sensitiveFields) {
        this.sensitiveFields = sensitiveFields;
    }

    public List<String> getNonLoggingFields() {
        return nonLoggingFields;
    }

    public void setNonLoggingFields(List<String> nonLoggingFields) {
        this.nonLoggingFields = nonLoggingFields;
    }

    @Override
    public String toString() {
        return "LoggingDetail{" +
                "strategy=" + strategy +
                ", sensitiveFields=" + sensitiveFields +
                ", nonLoggingFields=" + nonLoggingFields +
                '}';
    }

    public void patch(AdapterLogging commonAdapterLogging) {
        if (commonAdapterLogging == null) {
            return;
        }

        if (getStrategy() == null) {
            setStrategy(commonAdapterLogging.getStrategy());
        }

        if (getSensitiveFields() == null) {
            setSensitiveFields(commonAdapterLogging.getSensitiveFields());
        }

        if (getNonLoggingFields() == null) {
            setNonLoggingFields(commonAdapterLogging.getNonLoggingFields());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LogStrategy strategy;
        private List<String> sensitiveFields;
        private List<String> nonLoggingFields;

        public Builder strategy(LogStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public Builder sensitiveFields(List<String> sensitiveFields) {
            this.sensitiveFields = sensitiveFields;
            return this;
        }

        public Builder nonLoggingFields(List<String> nonLoggingFields) {
            this.nonLoggingFields = nonLoggingFields;
            return this;
        }

        public AdapterLogging build() {
            return new AdapterLogging(this);
        }
    }
}
