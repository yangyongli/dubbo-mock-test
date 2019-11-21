/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.dubbo.mock.test.model;


import org.apache.dubbo.common.utils.StringUtils;

import java.util.List;

public class PomModel {

    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
    private List<Exculsion> exculsions;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<Exculsion> getExculsions() {
        return exculsions;
    }

    public void setExculsions(List<Exculsion> exculsions) {
        this.exculsions = exculsions;
    }

    public boolean isBroken() {
        return StringUtils.isEmpty(groupId) || StringUtils.isEmpty(artifactId) || StringUtils.isEmpty(version);
    }

    public static class Exculsion {
        private String groupId;
        private String artifactId;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }
    }
}
