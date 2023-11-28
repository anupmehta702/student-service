
# student-service
This repository is used for spring cloud functionality

java -jar student-service-0.0.1-SNAPSHOT.jar -Dserver.port=8085

OR

mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8085

# Azure notes
1) Installed Azure CLI 
2) Created resource group in region Central India - myPayAsYouGoSpringAppsSubscription
3) Already created subscription - Pay-As-You-Go (0479e180-7a73-4954-bb1d-1f5e815990bd)

Refer --> https://learn.microsoft.com/en-us/azure/spring-apps/quickstart?tabs=Azure-portal%2CAzure-CLI%2CConsumption-workload&pivots=sc-enterprise&tryIt=true&source=docs#code-try-42 <br />


##### Opened windows powershell-
 az login // it opens a web browser through which you login <br> 
output --> <br />
[
  {
    "cloudName": "AzureCloud",
    "homeTenantId": "c0865a08-7865-4091-809c-34e203a01746",
    "id": "0479e180-7a73-4954-bb1d-1f5e815990bd",
    "isDefault": true,
    "managedByTenants": [],
    "name": "Pay-As-You-Go",
    "state": "Enabled",
    "tenantId": "c0865a08-7865-4091-809c-34e203a01746",
    "user": {
      "name": "anupmehta702@gmail.com",
      "type": "user"
    }
  }
]

 az account set --subscription 0479e180-7a73-4954-bb1d-1f5e815990bd <br />
 az configure --defaults location="centralindia" <br />
 az configure --defaults group="myPayAsYouGoSpringAppsSubscription" <br />

 az configure --list-defaults -l <br /> to list all defaults 
 
## Install extension for springApps
az extension add --name spring --upgrade <br />
az provider register --namespace Microsoft.SaaS <br />

### Create spring apps related resources
az term accept --publisher vmware-inc --product azure-spring-cloud-vmware-tanzu-2 --plan asa-ent-hr-mtr <br />
az spring create --name helloworldspringservice --sku Enterprise <br />
az spring app create --service helloworldspringservice --name studentservice --assign-endpoint true <br />
az spring app deploy --service helloworldspringservice --name studentservice --artifact-path target/student-service-0.0.1-SNAPSHOT.jar <br />

az spring app create --> creates a app and deploys default service. It is deployed in production environment using "default" name <br />
az spring app deploy --> would deploy your application to production by default <br />

You can create a new deployment with a unique name using below command <br />
az spring app deployment create \
    --service helloworldspringservice \
    --app studentservice \
    --name green \
    --env deployed.from=AzureGreen
    --runtime-version Java_17 \
    --artifact-path target/student-service-0.0.1-SNAPSHOT.jar <br />

then hit API --> https://helloworldspringservice.test.azuremicroservices.io/studentservice/green/echoStudentName/Anoop <br />


## Blue green deployement 
create two deployments first -- <br />
 az spring app deployment create --service helloworldspringservice --app studentservice --name blue --env deployed.from=greenAzure <br /> 
 az spring app deployment create --service helloworldspringservice --app studentservice --name green --env deployed.from=blueAzure <br />
 
 Set "blue" deployment as production from UI ( "set as production" option) <br />
 Deploy code to each deployment <br />
 az spring app deploy 
 --service helloworldspringservice 
 --name studentservice 
 --deployment blue 
 --artifact-path target/student-service-0.0.1-SNAPSHOT.jar <br />
 
 hit API --> https://primary:6NUSgE0BH7RDw8h4cjidCdbbRaOSqbVbSivjIKfjTBkqKtI2OeQUEuj9rb0jAXbn@helloworldspringservice.test.azuremicroservices.io/echoStudentName/Anoop <br />
 Notice API does not have app name and deployment name <br />
 
 Now deploy app to green deployment ( staging deployment) -- <br /> 
 az spring app deploy \ 
  --service helloworldspringservice \ 
  --name studentservice \
  --deployment green \
  --artifact-path target/student-service-0.0.1-SNAPSHOT.jar <br />
  
 hit API --> https://primary:6NUSgE0BH7RDw8h4cjidCdbbRaOSqbVbSivjIKfjTBkqKtI2OeQUEuj9rb0jAXbn@helloworldspringservice.test.azuremicroservices.io/studentservice/newgreen/echoStudentName/Anoop <br />
 
Notice API has app name and deployment name <br />


Reference --> https://learn.microsoft.com/en-us/azure/spring-apps/how-to-staging-environment?WT.mc_id=Portal-AppPlatformExtension <br /> 
# Find out 
What is resourcegroup < br/>
Difference between service name, app , deployment --> https://learn.microsoft.com/en-us/azure/spring-apps/concept-understand-app-and-deployment<br/>
