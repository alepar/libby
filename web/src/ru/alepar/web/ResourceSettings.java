package ru.alepar.web;

import java.util.ResourceBundle;

public class ResourceSettings implements Settings {

    private final ResourceBundle bundle;

    public ResourceSettings(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public String traumRoot() {
        return bundle.getString("traum.root");
    }

    @Override
    public String traumIndex() {
        return bundle.getString("traum.index");
    }

    @Override
    public boolean reindex() {
        return "true".equals(bundle.getString("reindex"));
    }
}
