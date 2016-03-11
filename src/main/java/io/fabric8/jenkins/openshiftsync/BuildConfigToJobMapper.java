package io.fabric8.jenkins.openshiftsync;

import hudson.model.Job;
import hudson.plugins.git.BranchSpec;
import hudson.plugins.git.GitSCM;
import io.fabric8.openshift.api.model.BuildConfig;
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition;
import org.jenkinsci.plugins.workflow.flow.FlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

public class BuildConfigToJobMapper {

  public static Job<WorkflowJob, WorkflowRun> mapBuildConfigToJob(BuildConfig bc) {
    GitSCM scm = new GitSCM(bc.getSpec().getSource().getGit().getUri());

    FlowDefinition flowDefinition = new CpsScmFlowDefinition(scm, "Jenkinsfile");

    WorkflowJob job = new WorkflowJob(null, jobName(bc));
    job.setDefinition(flowDefinition);

    return job;
  }

  public static String jobName(BuildConfig buildConfig) {
    return buildConfig.getMetadata().getNamespace() + "-" + buildConfig.getMetadata().getName();
  }

}