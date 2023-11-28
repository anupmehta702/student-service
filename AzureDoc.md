## Azure automated deployment via Azure Devops

I couldnt run the pipeline from azure.devops site directly as it requires permission to run,
so I had to download Azure pipeline agent and configure it on personal laptop. <br /> 
Download agent software and unzip and run config.cmd from C:/agents folder. <br />
Refer link -- https://learn.microsoft.com/en-us/azure/devops/pipelines/agents/windows-agent?view=azure-devops <br />
<br />
To ensure your pipeline runs via Windows agent on local machine and not on azure.devops site,
you need to make below change in pipeline's (azure-pipelines)yaml file - <br />

pool: <br />
 name: Default <br />

Refer --> https://www.pluralsight.com/cloud-guru/labs/azure/building-apps-using-self-hosted-build-agents-in-azure-pipelines <br /> 

## References 
https://learn.microsoft.com/en-us/azure/spring-apps/how-to-cicd?pivots=programming-language-java <br />
