package org.cloudsimplus.examples;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.resources.CoreCapacity;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;

public class AwsSimulation {

    public static void main(String[] args) {

        System.out.println("AWS Cloud Simulation - SJF Scheduling");

        // 1. Initialize CloudSim
        CloudSimPlus simulation = new CloudSimPlus();

        // 2. Create physical host  (Host, not HostSimple)
        Host host = HostSimple.of(
            8192, 100000, 1000000,
            new CoreCapacity(4, 2000)
        );

        // 3. Create datacenter
        DatacenterSimple.of(simulation, List.of(host));

        // 4. Create broker
        DatacenterBrokerSimple broker =
            new DatacenterBrokerSimple(simulation);

        // 5. Create two virtual machines  (List<Vm>, Vm)
        List<Vm> vmList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {

            Vm vm = VmSimple.of(
                new CoreCapacity(2, 1000)
            );

            vm.setRam(2048);
            vm.setBw(10000);
            vm.setSize(20000);

            vmList.add(vm);
        }

        // 6. Create cloudlets (jobs)
        List<Cloudlet> cloudletList = new ArrayList<>();

        long[] jobLengths = {10000, 3000, 7000, 2000};

        UtilizationModelDynamic utilization =
            new UtilizationModelDynamic(0.5);

        for (long length : jobLengths) {

            Cloudlet cloudlet = CloudletSimple.of(   // Cloudlet, not CloudletSimple
                length, 1, utilization
            );

            cloudlet.setSizes(1024);
            cloudletList.add(cloudlet);
        }

        // 7. Apply Shortest Job First scheduling
        broker.setCloudletComparator(
            Comparator.comparingLong(Cloudlet::getLength)
        );

        System.out.println("\nSJF Scheduling Order:");

        cloudletList.stream()
            .sorted(Comparator.comparingLong(Cloudlet::getLength))
            .forEach(c ->
                System.out.println(
                    "Job Length: " + c.getLength() + " MI"
                )
            );

        // 8. Submit VMs and cloudlets
        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);

        // 9. Start simulation
        System.out.println("\nStarting Cloud Simulation...\n");

        simulation.start();

        // 10. Display results
        System.out.println("\nSimulation Results:");

        new CloudletsTableBuilder(
            broker.getCloudletFinishedList()
        ).build();

        System.out.println("\nAWS Cloud Simulation Completed!");
    }
}
