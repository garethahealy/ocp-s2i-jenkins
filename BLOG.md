# How to programmatically generate a Jenkins plugins.txt
As part of the Red Hat [UKI Professional Services](https://www.redhat.com/en/services/consulting) team,
I regularly work with customers who are adopting [OpenShift Container Platform (OCP)](https://developers.redhat.com/products/openshift/overview/)
as their chosen application platform.

There are several stages that I am typically involved with:
- Installing and configuring the platform; [Day 1 install](https://docs.openshift.com/container-platform/latest/install/index.html)
- Assisting with knowledge transfer around operational readiness of the platform; [Day 2 operations](https://docs.openshift.com/container-platform/latest/day_two_guide/index.html)
- Assisting with on-boarding of applications onto the platform; [Day 3 on-boarding](https://www.redhat.com/files/summit/session-assets/2018/How-to-build-a-successful-onboarding-program-for-OpenShift.pdf)

.....

chicken and the egg?




## Whats a plugins.txt?
[Plugins.txt](https://github.com/jenkinsci/docker#plugin-version-format) is a file that contains one or more lines of ${plugin-name}:${plugin-version}.
When Jenkins starts in a container, it executes [install-plugins.sh](https://github.com/jenkinsci/docker/blob/master/install-plugins.sh), 
passing in the contents of plugins.txt

An example extract:

    ace-editor:latest
    ant:latest
    antisamy-markup-formatter:latest
    authentication-tokens:latest
    blueocean:1.0.0-b24 
    blueocean-autofavorite:latest
    blueocean-commons:1.0.0-b24


https://docs.openshift.com/container-platform/3.11/using_images/other_images/jenkins.html#jenkins-as-s2i-builder


## Whats the jenkins-plugin-generator-lib (JPGLib)

    #!/usr/bin/env groovy
    @Library("jenkins-plugin-generator-lib@1.0.0") _
    
    node() {
        stage('Checkout') {
            withCredentials([usernameColonPassword(credentialsId: 'github-auth', variable: 'USERPASS')]) {
                sh """
                    git clone https://${USERPASS}@github.com/garethahealy/jenkins-master-s2i.git .
                    git config --global user.email jenkins-master-s2i@garethahealy.com
                    git config --global user.name jenkins-master-s2i
                """
            }
        }
        
        stage('Generate plugins file from the latest stable Jenkins update centre') {
            sh "curl -L -s -o ${WORKSPACE}/jenkins.json https://updates.jenkins.io/stable/update-center.actual.json"
    
            resolvePluginVersions([pluginTemplatePath:"file://${WORKSPACE}/plugins.txt.template", updateCentrePath:"file://${WORKSPACE}/jenkins.json"])
        }
    
        stage('Push generated plugins file to git') {
            sh """
                git add plugins.txt
                git commit -m 'Generated plugins.txt - build ${env.BUILD_NUMBER}'
    
                git push origin master
            """
        }
    
        stage('Trigger jenkins s2i build to ') {
            openshift.withCluster() {
                openshift.withProject() {
                    def buildConfigSelector = openshift.selector("bc", "custom-jenkins-build")
                    def buildSelector = buildConfigSelector.startBuild()
                    buildSelector.logs('-f')
                }
            }
        }
    }

## What does it do?
You provide a plugins.txt.template which names what plugins you want. 
If no version is included, the latest is used. 
If a version is included, then it is regarded as 'pinned' and that version is used. 
All plugins and their dependencies will be included in the final plugins.txt


## What it does not do?
Resolve plugin conflicts. If you hit plugin conflicts (which is possible if you need an old plugin); 
you’ll need to pin the correct plugin versions.

## <a name="DISCLAIMER"></a>DISCLAIMER
[1] Although JPGLib is developer by Red Hat employees, it is not supported under a Red Hat subscription and is strictly an upstream project.