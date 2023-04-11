package com.dailydatahub.dailydatacrawler.aws.instance.controller;

import java.io.File;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/instance")
public class InstanceController {

    /**
     * create aws instance of ec2
     * instance type : micro t2
     * group type : ~~~
     * 
     * @param news select news category
     * @return
     */
    @RequestMapping(value = "/aws", method = RequestMethod.POST)
    public boolean awsCreate() throws Exception{

        boolean totalJobFlag = false;
        AmazonEC2 ec2Client = AmazonEC2ClientBuilder.standard().build();
        RunInstancesRequest runRequest = new RunInstancesRequest()
        .withImageId("ami-0c94855ba95c71c99") // Amazon Linux 2 AMI
        .withInstanceType("t2.micro")
        .withMinCount(1)
        .withMaxCount(1)
        .withKeyName("myKeyPair")
        .withSecurityGroupIds("mySecurityGroupId");
        RunInstancesResult runResponse = ec2Client.runInstances(runRequest);
        String instanceId = runResponse.getReservation().getInstances().get(0).getInstanceId();
        DescribeInstancesRequest describeRequest = new DescribeInstancesRequest()
        .withInstanceIds(instanceId);

        boolean isRunning = false;
        while (!isRunning) {

            DescribeInstancesResult describeResponse = ec2Client.describeInstances(describeRequest);
            Reservation reservation = describeResponse.getReservations().get(0);
            Instance instance = reservation.getInstances().get(0);
            isRunning = instance.getState().getName().equals("running");
            Thread.sleep(5000);

            // connect bia ssh to aws micro t2 ec2 for jar job of jvm
            JSch jsch = new JSch();
            Session session = jsch.getSession("ec2-user", instance.getPublicDnsName(), 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand("java -jar myJob.jar");
            channelExec.connect();

            // Wait for the job to finish
            while (!channelExec.isClosed()) Thread.sleep(1000);
            channelExec.disconnect();
            session.disconnect();

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
            String bucketName = "myBucket";
            String objectKey = "myData.txt";
            s3Client.putObject(bucketName, objectKey, new File("/path/to/myData.txt"));
            TerminateInstancesRequest terminateRequest = new TerminateInstancesRequest().withInstanceIds(instanceId);
            InstanceState instanceState = instance.getState();
            if(instanceState.getName().equals(InstanceStateName.Terminated)){
                totalJobFlag = true;
            }
        }
        return totalJobFlag;
    }
}  