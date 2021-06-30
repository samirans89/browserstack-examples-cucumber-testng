import org.jenkinsci.plugins.pipeline.modeldefinition.Utils
import groovy.json.JsonSlurperClassic
node {
    try {
        properties([
            pipelineTriggers([
                [
                    $class: 'TimerTrigger',
                    spec: 'H 0 * * 5'
                ],
                [
                    $class: 'GenericTrigger',
                        genericVariables: [
                            [key: 'data', value: '$']
                        ],
                    token: 'testng_cucumber_boilerplate',
                    tokenCredentialId: '',
                    printContributedVariables: true,
                    printPostContent: true,
                    silentResponse: false
                ]
            ]),
            parameters([
                credentials(credentialType: 'com.browserstack.automate.ci.jenkins.BrowserStackCredentials', defaultValue: '45d4ca9d-b349-4c13-8d94-bc81aedf4ac1', description: 'Select your BrowserStack Username', name: 'BROWSERSTACK_USERNAME', required: true),
                [$class: 'ExtensibleChoiceParameterDefinition',
                choiceListProvider: [
                    $class: 'TextareaChoiceListProvider',
                    addEditedValue: false,
                    choiceListText: '''prod_smoke
qa_regression
qa_functional
uat_critical''',
                    defaultChoice: 'qa_regression'
                ],
                description: 'Select the test you would like to run',
                editable: false,
                name: 'TEST_TYPE']
            ])
        ])
        stage('Pull from Github') {
            def branch = 'main_app'
            try {
                def pull_request_data = new JsonSlurperClassic().parseText(env.data)
                branch = pull_request_data.pull_request.head.ref
            } catch(Exception ex) {
                println("Triggered from Jenkins");
            }
            dir('test') {
                git branch: "${branch}", changelog: false, poll: false, url: 'https://github.com/samirans89/browserstack-examples-cucumber-testng.git'
            }
        }
        stage('Run Test') {
            browserstack(credentialsId: "${params.BROWSERSTACK_USERNAME}", localConfig: [localOptions: '', localPath: '']) {
                def user = "${env.BROWSERSTACK_USERNAME}"
                if ( user.contains('-')) {
                    user = user.substring(0, user.lastIndexOf('-'))
                }
                if (!params.TEST_TYPE) {
                    env.TEST_TYPE = "qa_regression"
                }
                withEnv(['BROWSERSTACK_USERNAME=' + user]) {
                    sh label: '', returnStatus: true, script: '''#!/bin/bash -l
                                                                cd test
                                                                mvn clean test -P ${TEST_TYPE}
                                                                '''
                }
            }
        }
    } catch (e) {
        currentBuild.result = 'FAILURE'
        echo e
        throw e
    } finally {
        notifySlack(currentBuild.result)
    }
}
def notifySlack(String buildStatus = 'STARTED') {
    // Build status of null means success.
    buildStatus = buildStatus ?: 'SUCCESS'
    def color
    if (buildStatus == 'STARTED') {
        color = '#D4DADF'
    } else if (buildStatus == 'SUCCESS') {
        color = '#BDFFC3'
    } else if (buildStatus == 'UNSTABLE') {
        color = '#FFFE89'
    } else {
        color = '#FF9FA1'
    }
    def msg = "${buildStatus}: `${env.JOB_NAME}` #${env.BUILD_NUMBER}:\n${env.BUILD_URL}"
    if (buildStatus != 'STARTED' && buildStatus != 'SUCCESS') {
        slackSend(color: color, message: msg)
    }
}