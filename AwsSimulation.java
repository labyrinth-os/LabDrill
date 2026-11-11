
package org.cloudsimplus.examples;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.vms.VmSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;

public class AwsSimulation {

    public static void main(String[] args) {

        System.out.println("AWS Cloud Simulation - SJF Scheduling");

        // Initialize CloudSim
        CloudSimPlus simulation = new CloudSimPlus();

        // Create physical host
        List<PeSimple> peList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            peList.add(new PeSimple(2000));
        }

        HostSimple host = new HostSimple(
            8192, 100000, 1000000, peList
        );

        // Create datacenter
        new DatacenterSimple(simulation, List.of(host));

        // Create broker
        DatacenterBrokerSimple broker =
            new DatacenterBrokerSimple(simulation);

        // Create two virtual machines
        List<VmSimple> vmList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            VmSimple vm = new VmSimple(1000, 2);

            vm.setRam(2048);
            vm.setBw(10000);
            vm.setSize(20000);

            vmList.add(vm);
        }

        // Create cloudlets
        List<Cloudlet> cloudletList = new ArrayList<>();

        long[] jobLengths = {10000, 3000, 7000, 2000};

        for (long length : jobLengths) {

            CloudletSimple cloudlet = new CloudletSimple(
                length, 1,
                new UtilizationModelDynamic(0.5)
            );

            cloudlet.setSizes(1024);
            cloudletList.add(cloudlet);
        }

        // Apply SJF scheduling
        broker.setCloudletComparator(
            Comparator.comparingLong(Cloudlet::getLength)
        );

        System.out.println("\nSJF Scheduling Order:");

        cloudletList.stream()
            .sorted(Comparator.comparingLong(Cloudlet::getLength))
            .forEach(c ->
                System.out.println("Job Length: " + c.getLength() + " MI")
            );

        // Submit VMs and cloudlets
        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);

        // Start simulation
        System.out.println("\nStarting AWS Cloud Simulation...\n");

        simulation.start();

        // Display results
        System.out.println("\nSimulation Results:");

        new CloudletsTableBuilder(
            broker.getCloudletFinishedList()
        ).build();

        System.out.println("\nAWS Cloud Simulation Completed!");
    }
}
