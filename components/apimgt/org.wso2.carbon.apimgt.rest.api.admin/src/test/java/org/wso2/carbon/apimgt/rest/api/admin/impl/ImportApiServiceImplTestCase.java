/*
 *   Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.apimgt.rest.api.admin.impl;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.api.model.Tier;
import org.wso2.carbon.apimgt.impl.utils.APINameComparator;
import org.wso2.carbon.apimgt.rest.api.admin.ImportApiService;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.ws.rs.core.Response;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RestApiUtil.class)
public class ImportApiServiceImplTestCase {
    private final String USER = "admin";
    private ImportApiService importApiService;
    private APIConsumer apiConsumer;

    @Before
    public void init() throws Exception {
        importApiService = new ImportApiServiceImpl();
        apiConsumer = Mockito.mock(APIConsumer.class);
    }

    /**
     * This method tests the functionality of ImportApplicationsGet, for an attempt to import a subscription when
     * the target API is unavailable as it is not published
     *
     * @throws Exception Exception.
     */
    @Test
    public void testImportApplicationsPostAPINotPublishedError() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sampleApp.zip").getFile());
        FileInputStream fis;
        fis = new FileInputStream(file);
        Attachment fileInfo = Mockito.mock(Attachment.class);
        PowerMockito.mockStatic(RestApiUtil.class);
        PowerMockito.when(RestApiUtil.getLoggedInUsername()).thenReturn(USER);
        PowerMockito.when(RestApiUtil.getConsumer(USER)).thenReturn(apiConsumer);
        APIIdentifier apiId = new APIIdentifier("admin_sampleAPI_1.0.0");
        Map matchedAPIs = new HashMap<String, Object>();
        SortedSet<API> apiSet = new TreeSet<>(new APINameComparator());
        API api = new API(apiId);
        Set<Tier> tierSet = new HashSet<>();
        tierSet.add(new Tier("Gold"));
        api.addAvailableTiers(tierSet);
        apiSet.add(api);
        matchedAPIs.put("apis", apiSet);
        Subscriber subscriber = new Subscriber("admin");
        Mockito.when(apiConsumer.getSubscriber("admin")).thenReturn(subscriber);
        Mockito.when(apiConsumer.addApplication(Mockito.any(Application.class), Mockito.anyString())).thenReturn(1);
        PowerMockito.when(RestApiUtil.isTenantAvailable("carbon.super")).thenReturn(true);
        Mockito.when(apiConsumer.searchPaginatedAPIs("name=*sampleAPI*&version=*1.0.0*",
                "carbon.super", 0, Integer.MAX_VALUE, false)).thenReturn(matchedAPIs);
        Mockito.when(apiConsumer.getApplicationById(1)).thenReturn(new Application(1));
        Response response = importApiService.importApplicationsPost(fis, fileInfo, true,
                false, "admin");
        Assert.assertEquals(response.getStatus(), 207);
    }

    /**
     * This method tests the functionality of ImportApplicationsGet, for an attempt to import a subscription when
     * the target tier is unavailable
     *
     * @throws Exception Exception.
     */
    @Test
    public void testImportApplicationsPostTierUnavailableError() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sampleApp.zip").getFile());
        FileInputStream fis;
        fis = new FileInputStream(file);
        Attachment fileInfo = Mockito.mock(Attachment.class);
        PowerMockito.mockStatic(RestApiUtil.class);
        PowerMockito.when(RestApiUtil.getLoggedInUsername()).thenReturn(USER);
        PowerMockito.when(RestApiUtil.getConsumer(USER)).thenReturn(apiConsumer);
        APIIdentifier apiId = new APIIdentifier("admin_sampleAPI_1.0.0");
        Map matchedAPIs = new HashMap<String, Object>();
        SortedSet<API> apiSet = new TreeSet<>(new APINameComparator());
        apiSet.add(new API(apiId));
        matchedAPIs.put("apis", apiSet);
        Subscriber subscriber = new Subscriber("admin");
        Mockito.when(apiConsumer.getSubscriber("admin")).thenReturn(subscriber);
        Mockito.when(apiConsumer.addApplication(Mockito.any(Application.class), Mockito.anyString())).thenReturn(1);
        PowerMockito.when(RestApiUtil.isTenantAvailable("carbon.super")).thenReturn(true);
        Mockito.when(apiConsumer.searchPaginatedAPIs("name=*sampleAPI*&version=*1.0.0*",
                "carbon.super", 0, Integer.MAX_VALUE, false)).thenReturn(matchedAPIs);
        Mockito.when(apiConsumer.getApplicationById(1)).thenReturn(new Application(1));
        Response response = importApiService.importApplicationsPost(fis, fileInfo, true,
                false, "admin");
        Assert.assertEquals(response.getStatus(), 207);
    }

    /**
     * This method tests the functionality of ImportApplicationsGet, for an attempt to import an application
     * when an application with similar identity is already available
     *
     * @throws Exception Exception.
     */
    @Test
    public void testImportApplicationsPostError() throws Exception {
        PowerMockito.mockStatic(RestApiUtil.class);
        PowerMockito.when(RestApiUtil.getConsumer(USER)).thenReturn(apiConsumer);
        PowerMockito.when(RestApiUtil.getLoggedInUsername()).thenReturn(USER);
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sampleApp.zip").getFile());
        FileInputStream fis;
        fis = new FileInputStream(file);
        Subscriber subscriber = new Subscriber("admin");
        Mockito.when(apiConsumer.getSubscriber("admin")).thenReturn(subscriber);
        Mockito.when(apiConsumer.addApplication(Mockito.any(Application.class), Mockito.anyString()))
                .thenThrow(APIManagementException.class);
        Response response = importApiService.importApplicationsPost(fis, null, false,
                false, null);

        Assert.assertNull("Error while importing Application", response);
    }

    /**
     * This method tests the functionality of testImportApplicationsGet, for a cross tenant application
     * import attempt
     *
     * @throws Exception Exception.
     */
    @Test
    public void testImportApplicationsPostCrossTenantError() throws Exception {
        PowerMockito.mockStatic(RestApiUtil.class);
        PowerMockito.when(RestApiUtil.getConsumer(USER)).thenReturn(apiConsumer);
        PowerMockito.when(RestApiUtil.getLoggedInUsername()).thenReturn(USER);
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sampleApp.zip").getFile());
        FileInputStream fis;
        fis = new FileInputStream(file);
        Response response = importApiService.importApplicationsPost(fis, null, false,
                false, "admin@hr.lk");
        Assert.assertEquals(response.getStatus(), 403);
    }

}