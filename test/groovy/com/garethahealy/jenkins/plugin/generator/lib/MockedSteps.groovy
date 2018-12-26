package com.garethahealy.jenkins.plugin.generator.lib;

class MockedSteps {

    String readFile(Map args) {
        return new File(args.get("file")).getText(args.get("encoding"))
    }

    void echo(String args) {
        println(args)
    }
}
