package ru.alepar.setting;

import java.util.MissingResourceException;
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
        try {
            return bundle.getString("traum.index");
        } catch (MissingResourceException e) {
            return null;
        }
    }

    @Override
    public boolean traumReindex() {
        try {
            return "true".equals(bundle.getString("traum.reindex"));
        } catch (MissingResourceException e) {
            return false;
        }
    }

    @Override
    public String calibreConvert() {
        return bundle.getString("calibre.convert");
    }
}
