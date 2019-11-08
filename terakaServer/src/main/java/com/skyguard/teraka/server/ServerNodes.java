package com.skyguard.teraka.server;

import com.google.common.collect.Lists;

import java.util.List;

public class ServerNodes {

    private static List<Node> nodeList = Lists.newArrayList();

    public static List<Node> getServerNodes(){
        return nodeList;
    }

    public static void addNode(Node node){
        nodeList.add(node);
    }

    public static void setServerNodes(List<Node> nodes){
        nodeList = nodes;
    }

    public static void removeNode(Node node){
        nodeList.remove(node);
    }


}
