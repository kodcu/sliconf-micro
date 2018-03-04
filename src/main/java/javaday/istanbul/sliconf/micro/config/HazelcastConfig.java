package javaday.istanbul.sliconf.micro.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Value("${sliconf.hazelcast.multicast-group}")
    private String multicastGroup;

    @Value("${sliconf.hazelcast.tcp-ip}")
    private String tcpIp;

    @Value("${sliconf.hazelcast.group-name}")
    private String groupName;

    @Value("${sliconf.hazelcast.group-pass}")
    private String groupPass;

    @Bean
    public HazelcastInstance hazelcastInstance() {

        MulticastConfig multicastConfig = new MulticastConfig()
                .setEnabled(false)
                .setMulticastGroup(multicastGroup)
                .setMulticastPort(54327);

        TcpIpConfig tcpIpConfig = new TcpIpConfig()
                .setEnabled(true)
                .addMember(tcpIp);

        JoinConfig joinConfig = new JoinConfig()
                .setTcpIpConfig(tcpIpConfig)
                .setMulticastConfig(multicastConfig);

        NetworkConfig networkConfig = new NetworkConfig()
                .setPort(5701)
                .setPortAutoIncrement(false)
                .setJoin(joinConfig);

        GroupConfig groupConfig = new GroupConfig(groupName).setPassword(groupPass);

        Config config = new Config()
                .setGroupConfig(groupConfig)
                .setNetworkConfig(networkConfig);

        return Hazelcast.newHazelcastInstance(config);
    }
}