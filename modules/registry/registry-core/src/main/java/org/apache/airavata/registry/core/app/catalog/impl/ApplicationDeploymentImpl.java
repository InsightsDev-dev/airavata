/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.airavata.registry.core.app.catalog.impl;

import org.apache.airavata.model.appcatalog.appdeployment.ApplicationDeploymentDescription;
import org.apache.airavata.model.appcatalog.appdeployment.ApplicationParallelismType;
import org.apache.airavata.model.appcatalog.appdeployment.SetEnvPaths;
import org.apache.airavata.registry.core.app.catalog.resources.*;
import org.apache.airavata.registry.core.app.catalog.util.AppCatalogThriftConversion;
import org.apache.airavata.registry.cpi.AppCatalogException;
import org.apache.airavata.registry.cpi.ApplicationDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationDeploymentImpl implements ApplicationDeployment {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationDeploymentImpl.class);

    @Override
    public String addApplicationDeployment(ApplicationDeploymentDescription deploymentDescription, String gatewayId) throws AppCatalogException {
        try {
            AppDeploymentAppCatalogResourceAppCat deploymentResource = new AppDeploymentAppCatalogResourceAppCat();
            ComputeResourceAppCatalogResourceAppCat computeHostResource = new ComputeResourceAppCatalogResourceAppCat();
            AppModuleAppCatalogResourceAppCat moduleResource = new AppModuleAppCatalogResourceAppCat();
            if (!computeHostResource.isExists(deploymentDescription.getComputeHostId())){
                logger.error("Compute host does not exist in the system. Please create a Compute host first...");
                throw new AppCatalogException("Compute host does not exist in the system. Please create a Compute host first...");
            }
            if (!moduleResource.isExists(deploymentDescription.getAppModuleId())){
                logger.error("Application module does not exist in the system. Please create an application module first...");
                throw new AppCatalogException("Application module does not exist in the system. Please create an application module first...");
            }
            AppModuleAppCatalogResourceAppCat module = (AppModuleAppCatalogResourceAppCat)moduleResource.get(deploymentDescription.getAppModuleId());
            ComputeResourceAppCatalogResourceAppCat hostResource = (ComputeResourceAppCatalogResourceAppCat) computeHostResource.get(deploymentDescription.getComputeHostId());
            deploymentResource.setDeploymentId(hostResource.getHostName() + "_" + deploymentDescription.getAppModuleId());
            deploymentResource.setAppModuleId(deploymentDescription.getAppModuleId());
            deploymentResource.setModuleResource(module);
            deploymentResource.setHostId(deploymentDescription.getComputeHostId());
            deploymentResource.setHostResource(hostResource);
            deploymentResource.setAppDes(deploymentDescription.getAppDeploymentDescription());
            deploymentResource.setExecutablePath(deploymentDescription.getExecutablePath());
            deploymentResource.setGatewayId(gatewayId);
            ApplicationParallelismType parallelism = deploymentDescription.getParallelism();
            if (parallelism != null){
                deploymentResource.setParallelism(parallelism.toString());
            }
            deploymentResource.save();
            deploymentDescription.setAppDeploymentId(deploymentResource.getDeploymentId());

            List<String> moduleLoadCmds = deploymentDescription.getModuleLoadCmds();
            if (moduleLoadCmds != null && !moduleLoadCmds.isEmpty()){
                for (String cmd : moduleLoadCmds){
                    ModuleLoadCmdAppCatalogResourceAppCat cmdResource = new ModuleLoadCmdAppCatalogResourceAppCat();
                    cmdResource.setAppDeploymentId(deploymentDescription.getAppDeploymentId());
                    cmdResource.setCmd(cmd);
                    cmdResource.save();
                }
            }

            List<String> preJobCommands = deploymentDescription.getPreJobCommands();
            if (preJobCommands != null && !preJobCommands.isEmpty()){
                for (String cmd : preJobCommands){
                    PreJobCommandAppCatalogResourceAppCat cmdResource = new PreJobCommandAppCatalogResourceAppCat();
                    cmdResource.setAppDeploymentId(deploymentDescription.getAppDeploymentId());
                    cmdResource.setCommand(cmd);
                    cmdResource.save();
                }
            }

            List<String> postJobCommands = deploymentDescription.getPostJobCommands();
            if (postJobCommands != null && !postJobCommands.isEmpty()){
                for (String cmd : postJobCommands){
                    PostJobCommandAppCatalogResourceAppCat cmdResource = new PostJobCommandAppCatalogResourceAppCat();
                    cmdResource.setAppDeploymentId(deploymentDescription.getAppDeploymentId());
                    cmdResource.setCommand(cmd);
                    cmdResource.save();
                }
            }

            List<SetEnvPaths> libPrependPaths = deploymentDescription.getLibPrependPaths();
            if (libPrependPaths != null && !libPrependPaths.isEmpty()){
                for (SetEnvPaths path : libPrependPaths){
                    LibraryPrepandPathAppCatalogResourceAppCat prepandPathResource = new LibraryPrepandPathAppCatalogResourceAppCat();
                    prepandPathResource.setAppDeploymentResource(deploymentResource);
                    prepandPathResource.setName(path.getName());
                    prepandPathResource.setValue(path.getValue());
                    prepandPathResource.setDeploymentId(deploymentResource.getDeploymentId());
                    prepandPathResource.save();
                }
            }

            List<SetEnvPaths> libApendPaths = deploymentDescription.getLibAppendPaths();
            if (libApendPaths != null && !libApendPaths.isEmpty()){
                for (SetEnvPaths path : libApendPaths){
                    LibraryApendPathAppCatalogResourceAppCat apendPathResource = new LibraryApendPathAppCatalogResourceAppCat();
                    apendPathResource.setAppDeploymentResource(deploymentResource);
                    apendPathResource.setName(path.getName());
                    apendPathResource.setValue(path.getValue());
                    apendPathResource.setDeploymentId(deploymentResource.getDeploymentId());
                    apendPathResource.save();
                }
            }
            List<SetEnvPaths> setEnvironment = deploymentDescription.getSetEnvironment();
            if (setEnvironment != null && !setEnvironment.isEmpty()){
                for (SetEnvPaths path : setEnvironment){
                    AppEnvironmentAppCatalogResourceAppCat environmentResource = new AppEnvironmentAppCatalogResourceAppCat();
                    environmentResource.setAppDeploymentResource(deploymentResource);
                    environmentResource.setName(path.getName());
                    environmentResource.setValue(path.getValue());
                    environmentResource.setDeploymentId(deploymentResource.getDeploymentId());
                    environmentResource.save();
                }
            }
            return deploymentResource.getDeploymentId();
        }catch (Exception e) {
            logger.error("Error while saving application deployment...", e);
            throw new AppCatalogException(e);
        }
    }

    @Override
    public void updateApplicationDeployment(String deploymentId, ApplicationDeploymentDescription updatedDeployment) throws AppCatalogException {
        try {
            AppDeploymentAppCatalogResourceAppCat deploymentResource = new AppDeploymentAppCatalogResourceAppCat();
            AppDeploymentAppCatalogResourceAppCat existingDep = (AppDeploymentAppCatalogResourceAppCat)deploymentResource.get(deploymentId);
            ComputeResourceAppCatalogResourceAppCat computeHostResource = new ComputeResourceAppCatalogResourceAppCat();
            AppModuleAppCatalogResourceAppCat moduleResource = new AppModuleAppCatalogResourceAppCat();
            if (!computeHostResource.isExists(updatedDeployment.getComputeHostId())){
                logger.error("Compute host does not exist in the system. Please create a Compute host first...");
                throw new AppCatalogException("Compute host does not exist in the system. Please create a Compute host first...");
            }
            if (!moduleResource.isExists(updatedDeployment.getAppModuleId())){
                logger.error("Application module does not exist in the system. Please create an application module first...");
                throw new AppCatalogException("Application module does not exist in the system. Please create an application module first...");
            }
            AppModuleAppCatalogResourceAppCat module = (AppModuleAppCatalogResourceAppCat)moduleResource.get(updatedDeployment.getAppModuleId());
            existingDep.setAppModuleId(updatedDeployment.getAppModuleId());
            existingDep.setModuleResource(module);
            existingDep.setHostId(updatedDeployment.getComputeHostId());
            existingDep.setHostResource((ComputeResourceAppCatalogResourceAppCat)computeHostResource.get(updatedDeployment.getComputeHostId()));
            existingDep.setAppDes(updatedDeployment.getAppDeploymentDescription());
            existingDep.setExecutablePath(updatedDeployment.getExecutablePath());
            if (updatedDeployment.getParallelism() != null){
                deploymentResource.setParallelism(updatedDeployment.getParallelism().toString());
            }

            existingDep.save();

            // remove existing module load commands
            ModuleLoadCmdAppCatalogResourceAppCat cmdResource = new ModuleLoadCmdAppCatalogResourceAppCat();
            Map<String, String> ids = new HashMap<String, String>();
            ids.put(AppCatAbstractResource.ModuleLoadCmdConstants.APP_DEPLOYMENT_ID, deploymentId);
            cmdResource.remove(ids);
            List<String> moduleLoadCmds = updatedDeployment.getModuleLoadCmds();
            if (moduleLoadCmds != null && !moduleLoadCmds.isEmpty()){
                for (String cmd : moduleLoadCmds){
                    ids = new HashMap<String, String>();
                    ids.put(AppCatAbstractResource.ModuleLoadCmdConstants.APP_DEPLOYMENT_ID, deploymentId);
                    ids.put(AppCatAbstractResource.ModuleLoadCmdConstants.CMD, cmd);
                    if (cmdResource.isExists(ids)){
                        cmdResource = (ModuleLoadCmdAppCatalogResourceAppCat)cmdResource.get(ids);
                    }
                    cmdResource.setCmd(cmd);
                    cmdResource.setAppDeploymentResource(existingDep);
                    cmdResource.setAppDeploymentId(deploymentId);
                    cmdResource.save();
                }
            }

            PreJobCommandAppCatalogResourceAppCat preJobCommandResource = new PreJobCommandAppCatalogResourceAppCat();
            ids = new HashMap<String, String>();
            ids.put(AppCatAbstractResource.PreJobCommandConstants.DEPLOYMENT_ID, deploymentId);
            preJobCommandResource.remove(ids);
            List<String> preJobCommands = updatedDeployment.getPreJobCommands();
            if (preJobCommands != null && !preJobCommands.isEmpty()){
                for (String cmd : preJobCommands){
                    ids = new HashMap<String, String>();
                    ids.put(AppCatAbstractResource.PreJobCommandConstants.DEPLOYMENT_ID, deploymentId);
                    ids.put(AppCatAbstractResource.PreJobCommandConstants.COMMAND, cmd);
                    if (preJobCommandResource.isExists(ids)){
                        preJobCommandResource = (PreJobCommandAppCatalogResourceAppCat)preJobCommandResource.get(ids);
                    }
                    preJobCommandResource.setCommand(cmd);
                    preJobCommandResource.setAppDeploymentResource(existingDep);
                    preJobCommandResource.setAppDeploymentId(deploymentId);
                    preJobCommandResource.save();
                }
            }

            PostJobCommandAppCatalogResourceAppCat postJobCommandResource = new PostJobCommandAppCatalogResourceAppCat();
            ids = new HashMap<String, String>();
            ids.put(AppCatAbstractResource.PostJobCommandConstants.DEPLOYMENT_ID, deploymentId);
            postJobCommandResource.remove(ids);
            List<String> postJobCommands = updatedDeployment.getPostJobCommands();
            if (postJobCommands != null && !postJobCommands.isEmpty()){
                for (String cmd : postJobCommands){
                    ids = new HashMap<String, String>();
                    ids.put(AppCatAbstractResource.PostJobCommandConstants.DEPLOYMENT_ID, deploymentId);
                    ids.put(AppCatAbstractResource.PostJobCommandConstants.COMMAND, cmd);
                    if (postJobCommandResource.isExists(ids)){
                        postJobCommandResource = (PostJobCommandAppCatalogResourceAppCat)postJobCommandResource.get(ids);
                    }
                    postJobCommandResource.setCommand(cmd);
                    postJobCommandResource.setAppDeploymentResource(existingDep);
                    postJobCommandResource.setAppDeploymentId(deploymentId);
                    postJobCommandResource.save();
                }
            }

            // remove existing lib prepand paths
            LibraryPrepandPathAppCatalogResourceAppCat prepandPathResource = new LibraryPrepandPathAppCatalogResourceAppCat();
            ids = new HashMap<String, String>();
            ids.put(AppCatAbstractResource.LibraryPrepandPathConstants.DEPLOYMENT_ID, deploymentId);
            prepandPathResource.remove(ids);
            List<SetEnvPaths> libPrependPaths = updatedDeployment.getLibPrependPaths();
            if (libPrependPaths != null && !libPrependPaths.isEmpty()){
                for (SetEnvPaths path : libPrependPaths){
                    ids = new HashMap<String, String>();
                    ids.put(AppCatAbstractResource.LibraryPrepandPathConstants.DEPLOYMENT_ID, deploymentId);
                    ids.put(AppCatAbstractResource.LibraryPrepandPathConstants.NAME, path.getName());
                    if (prepandPathResource.isExists(ids)){
                        prepandPathResource = (LibraryPrepandPathAppCatalogResourceAppCat)prepandPathResource.get(ids);
                    }
                    prepandPathResource.setAppDeploymentResource(existingDep);
                    prepandPathResource.setName(path.getName());
                    prepandPathResource.setValue(path.getValue());
                    prepandPathResource.setDeploymentId(deploymentId);
                    prepandPathResource.save();
                }
            }

            List<SetEnvPaths> libApendPaths = updatedDeployment.getLibAppendPaths();
            // remove lib append paths
            LibraryApendPathAppCatalogResourceAppCat apendPathResource = new LibraryApendPathAppCatalogResourceAppCat();
            ids = new HashMap<String, String>();
            ids.put(AppCatAbstractResource.LibraryApendPathConstants.DEPLOYMENT_ID, deploymentId);
            apendPathResource.remove(ids);
            if (libApendPaths != null && !libApendPaths.isEmpty()){
                for (SetEnvPaths path : libApendPaths){
                    ids = new HashMap<String, String>();
                    ids.put(AppCatAbstractResource.LibraryApendPathConstants.DEPLOYMENT_ID, deploymentId);
                    ids.put(AppCatAbstractResource.LibraryApendPathConstants.NAME, path.getName());
                    if (apendPathResource.isExists(ids)){
                        apendPathResource = (LibraryApendPathAppCatalogResourceAppCat)apendPathResource.get(ids);
                    }
                    apendPathResource.setAppDeploymentResource(existingDep);
                    apendPathResource.setName(path.getName());
                    apendPathResource.setValue(path.getValue());
                    apendPathResource.setDeploymentId(deploymentId);
                    apendPathResource.save();
                }
            }

            List<SetEnvPaths> setEnvironment = updatedDeployment.getSetEnvironment();
            // remove existing setEnvPaths
            AppEnvironmentAppCatalogResourceAppCat environmentResource = new AppEnvironmentAppCatalogResourceAppCat();
            ids = new HashMap<String, String>();
            ids.put(AppCatAbstractResource.AppEnvironmentConstants.DEPLOYMENT_ID, deploymentId);
            environmentResource.remove(ids);
            if (setEnvironment != null && !setEnvironment.isEmpty()){
                for (SetEnvPaths path : setEnvironment){
                    ids = new HashMap<String, String>();
                    ids.put(AppCatAbstractResource.AppEnvironmentConstants.DEPLOYMENT_ID, deploymentId);
                    ids.put(AppCatAbstractResource.AppEnvironmentConstants.NAME, path.getName());
                    if (environmentResource.isExists(ids)){
                        environmentResource = (AppEnvironmentAppCatalogResourceAppCat)environmentResource.get(ids);
                    }
                    environmentResource.setAppDeploymentResource(existingDep);
                    environmentResource.setName(path.getName());
                    environmentResource.setValue(path.getValue());
                    environmentResource.setDeploymentId(deploymentId);
                    environmentResource.save();
                }
            }
        }catch (Exception e) {
            logger.error("Error while updating application deployment...", e);
            throw new AppCatalogException(e);
        }
    }

    @Override
    public ApplicationDeploymentDescription getApplicationDeployement(String deploymentId) throws AppCatalogException {
        try {
            AppDeploymentAppCatalogResourceAppCat deploymentResource = new AppDeploymentAppCatalogResourceAppCat();
            AppDeploymentAppCatalogResourceAppCat appDep = (AppDeploymentAppCatalogResourceAppCat)deploymentResource.get(deploymentId);
            return AppCatalogThriftConversion.getApplicationDeploymentDescription(appDep);
        }catch (Exception e) {
            logger.error("Error while retrieving application deployment...", e);
            throw new AppCatalogException(e);
        }
    }

    @Override
    public List<ApplicationDeploymentDescription> getApplicationDeployements(Map<String, String> filters) throws AppCatalogException {
        List<ApplicationDeploymentDescription> deploymentDescriptions = new ArrayList<ApplicationDeploymentDescription>();
        try {
            AppDeploymentAppCatalogResourceAppCat resource = new AppDeploymentAppCatalogResourceAppCat();
            boolean firstTry=true;
            for (String fieldName : filters.keySet() ){
                List<ApplicationDeploymentDescription> tmpDescriptions = new ArrayList<ApplicationDeploymentDescription>();
                if (fieldName.equals(AppCatAbstractResource.ApplicationDeploymentConstants.APP_MODULE_ID)){
                    List<AppCatalogResource> resources = resource.get(AppCatAbstractResource.ApplicationDeploymentConstants.APP_MODULE_ID, filters.get(fieldName));
                    if (resources != null && !resources.isEmpty()){
                    	tmpDescriptions = AppCatalogThriftConversion.getAppDepDescList(resources);
                    }
                }else if (fieldName.equals(AppCatAbstractResource.ApplicationDeploymentConstants.COMPUTE_HOST_ID)){
                    List<AppCatalogResource> resources = resource.get(AppCatAbstractResource.ApplicationDeploymentConstants.COMPUTE_HOST_ID, filters.get(fieldName));
                    if (resources != null && !resources.isEmpty()){
                    	tmpDescriptions = AppCatalogThriftConversion.getAppDepDescList(resources);
                    }
                } else {
                    logger.error("Unsupported field name for app deployment.", new IllegalArgumentException());
                    throw new IllegalArgumentException("Unsupported field name for app deployment.");
                }
                if (firstTry){
                	deploymentDescriptions.addAll(tmpDescriptions);
                    firstTry=false;
                }else{
                    List<String> ids=new ArrayList<String>();
                	for (ApplicationDeploymentDescription applicationDeploymentDescription : deploymentDescriptions) {
						ids.add(applicationDeploymentDescription.getAppDeploymentId());
					}
                    List<ApplicationDeploymentDescription> tmp2Descriptions = new ArrayList<ApplicationDeploymentDescription>();
                	for (ApplicationDeploymentDescription applicationDeploymentDescription : tmpDescriptions) {
						if (ids.contains(applicationDeploymentDescription.getAppDeploymentId())){
							tmp2Descriptions.add(applicationDeploymentDescription);
						}
					}
                	deploymentDescriptions.clear();
                	deploymentDescriptions.addAll(tmp2Descriptions);
                }
            }
        }catch (Exception e){
            logger.error("Error while retrieving app deployment list...", e);
            throw new AppCatalogException(e);
        }
        return deploymentDescriptions;
    }

    @Override
    public List<ApplicationDeploymentDescription> getAllApplicationDeployements(String gatewayId) throws AppCatalogException {
        List<ApplicationDeploymentDescription> deploymentDescriptions = new ArrayList<ApplicationDeploymentDescription>();
        try {
            AppDeploymentAppCatalogResourceAppCat resource = new AppDeploymentAppCatalogResourceAppCat();
            resource.setGatewayId(gatewayId);
            List<AppCatalogResource> resources = resource.getAll();
            if (resources != null && !resources.isEmpty()){
                deploymentDescriptions = AppCatalogThriftConversion.getAppDepDescList(resources);
            }

        }catch (Exception e){
            logger.error("Error while retrieving app deployment list...", e);
            throw new AppCatalogException(e);
        }
        return deploymentDescriptions;
    }

    @Override
    public List<String> getAllApplicationDeployementIds() throws AppCatalogException {
        try {
            AppDeploymentAppCatalogResourceAppCat resource = new AppDeploymentAppCatalogResourceAppCat();
            return resource.getAllIds();
        }catch (Exception e){
            logger.error("Error while retrieving app deployment list...", e);
            throw new AppCatalogException(e);
        }
    }

    @Override
    public boolean isAppDeploymentExists(String deploymentId) throws AppCatalogException {
        try {
           AppDeploymentAppCatalogResourceAppCat deploymentResource = new AppDeploymentAppCatalogResourceAppCat();
            return deploymentResource.isExists(deploymentId);
        }catch (Exception e){
            logger.error("Error while retrieving app deployment...", e);
            throw new AppCatalogException(e);
        }
    }

    @Override
    public void removeAppDeployment(String deploymentId) throws AppCatalogException {
        try {
            AppDeploymentAppCatalogResourceAppCat deploymentResource = new AppDeploymentAppCatalogResourceAppCat();
            deploymentResource.remove(deploymentId);
        }catch (Exception e){
            logger.error("Error while deleting app deployment...", e);
            throw new AppCatalogException(e);
        }
    }
}