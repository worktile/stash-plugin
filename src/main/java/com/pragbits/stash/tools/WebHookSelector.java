package com.pragbits.stash.tools;

import org.apache.commons.validator.routines.*;

public class WebHookSelector {

    private String globalHook;
    private String localHook;
    private boolean hookValid;
    private String selectedHook;
    private String problem;
    private UrlValidator urlValidator;

    public  WebHookSelector(String globalHook, String localHook) {
        this.globalHook = globalHook;
        this.localHook = localHook;
        this.urlValidator = new UrlValidator();
        this.problem = "";

        setHook();
    }

    public boolean isHookValid() {
        return hookValid;
    }

    public String getSelectedHook() {
        return selectedHook;
    }

    public String getProblem() {
        return problem;
    }

    private void setHook() {
        if (globalHook.isEmpty() && localHook.isEmpty()) {
            hookValid = false;
            problem = "Both global and local hook url are empty.";
            return;
        }

        if (!localHook.isEmpty()) {
            // validate localHook as Url
            if (urlValidator.isValid(localHook)) {
                hookValid = true;
                selectedHook = localHook;
            } else {
                hookValid = false;
                problem = "Local hook url is set, but invalid.";
            }
        } else {
            // validate globalHook as Url
            if (urlValidator.isValid(globalHook)) {
                hookValid = true;
                selectedHook = globalHook;
            } else {
                hookValid = false;
                problem = "Global hook is set, but invalid.";
            }
        }
    }
}
