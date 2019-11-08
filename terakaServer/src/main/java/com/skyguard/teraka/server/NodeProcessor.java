package com.skyguard.teraka.server;

import com.google.common.collect.Lists;
import com.skyguard.teraka.util.IpUtil;
import com.skyguard.teraka.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class NodeProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(NodeProcessor.class);

    public static void processNodes(){

        try {

            String serverNodes = PropertyUtil.getValue("teraka.serverNodeList");

            if (StringUtils.isNotEmpty(serverNodes)) {
                List<String> nodes = Lists.newArrayList(serverNodes.split(","));
                List<Node> serverNodeList = nodes.stream().map(node -> {
                    String ip = node.indexOf(":") > -1 ? node.substring(0, node.indexOf(":")) : node;
                    int port = node.indexOf(":") > -1 ? Integer.valueOf(node.substring(node.indexOf(":") + 1)) : 0;
                    return new Node(ip, port);
                }).collect(Collectors.toList());
                ServerNodes.setServerNodes(serverNodeList);
            }
        }catch (Exception e){
            LOG.error("process node error",e);
        }


    }

    public static List<Node> getServerNode(){
        String ip = IpUtil.getLocalIpAddr();
        int port = Integer.parseInt(PropertyUtil.getValue("teraka.client.port"));
        List<Node> nodes = ServerNodes.getServerNodes();
        List<Node> serverNodes = Lists.newArrayList();
        for(Node node:nodes){
            if(!node.getIp().equals(ip)||node.getPort()!=port){
                serverNodes.add(node);
            }
        }

        return serverNodes;
    }




}
