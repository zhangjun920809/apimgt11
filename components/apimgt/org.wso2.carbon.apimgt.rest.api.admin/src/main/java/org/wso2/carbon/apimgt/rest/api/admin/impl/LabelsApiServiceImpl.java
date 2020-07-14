/*
*  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.apimgt.rest.api.admin.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIAdmin;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.Label;
import org.wso2.carbon.apimgt.impl.APIAdminImpl;
import org.wso2.carbon.apimgt.rest.api.admin.*;
import org.wso2.carbon.apimgt.rest.api.admin.dto.LabelListDTO;
import org.wso2.carbon.apimgt.rest.api.admin.dto.LabelDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.wso2.carbon.apimgt.rest.api.admin.utils.mappings.LabelMappingUtil;
import org.wso2.carbon.apimgt.rest.api.util.RestApiConstants;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;

import javax.ws.rs.core.Response;

public class LabelsApiServiceImpl extends LabelsApiService {

    private static final Log log = LogFactory.getLog(PoliciesApiServiceImpl.class);

    /**
     * Delete label
     *
     * @param labelId         Label identifier
     * @param ifMatch           If-Match header value
     * @param ifUnmodifiedSince If-Unmodified-Since header value
     * @return Status of label Deletion
     */
    @Override
    public Response labelsLabelIdDelete(String labelId, String ifMatch, String ifUnmodifiedSince) {
        try {
            APIAdmin apiAdmin = new APIAdminImpl();
            apiAdmin.deleteLabel(labelId);
            return Response.ok().build();
        } catch (APIManagementException e) {
            String errorMessage = "Error while deleting API : " + labelId;
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        }
        return null;
    }

    /**
     * Returns list of labels
     *
     * @return Matched labels for given search condition
     */
    @Override
    public Response labelsGet() {
        try {
            APIAdmin apiAdmin = new APIAdminImpl();
            String tenantDomain = RestApiUtil.getLoggedInUserTenantDomain();
            List<Label> labelList = apiAdmin.getAllLabels(tenantDomain);
            LabelListDTO labelListDTO =
                    LabelMappingUtil.fromLabelListToLabelListDTO(labelList);
            return Response.ok().entity(labelListDTO).build();
        } catch (APIManagementException e) {
            String errorMessage = "Error while retrieving labels";
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        }
        return null;
    }

    /**
     * Add a label
     *
     * @param body          Label DTO as request body
     * @return 200 response if successful
     */
    @Override
    public Response labelsPost(LabelDTO body) {
        Label label = null;
        try {
            APIAdmin apiAdmin = new APIAdminImpl();
            String tenantDomain = RestApiUtil.getLoggedInUserTenantDomain();
            label = LabelMappingUtil.labelDTOToLabel(body);
            LabelDTO labelDTO = LabelMappingUtil.
                    fromLabelToLabelDTO(apiAdmin.addLabel(tenantDomain, label));
            URI location = new URI(RestApiConstants.RESOURCE_PATH_LABEL + "/" +
                    labelDTO.getId());
            return Response.created(location).entity(labelDTO).build();
        } catch (APIManagementException | URISyntaxException e) {
            String errorMessage = "Error while adding new Label for API : "
                    + body.getName() + "-" + e.getMessage() ;
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        }
        return null;
    }

    /**
     * Updates an existing label
     *
     * @param body              content to update
     * @return 200 response if successful
     */
    @Override
    public Response labelsLabelIdPut(String labelId, LabelDTO body) {
        Label label = null;
        try {
            APIAdmin apiAdmin = new APIAdminImpl();
            label = LabelMappingUtil.labelDTOToLabelPut(labelId, body);
            LabelDTO labelDTO = LabelMappingUtil.
                    fromLabelToLabelDTO(apiAdmin.updateLabel(label));
            URI location = new URI(RestApiConstants.RESOURCE_PATH_LABEL + "/" +
                    labelDTO.getId());
            return Response.ok(location).entity(labelDTO).build();
        } catch (APIManagementException | URISyntaxException e) {
            String errorMessage = "Error while updating Label : " + labelId;
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        }
        return null;
    }
}
