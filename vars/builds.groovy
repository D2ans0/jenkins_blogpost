def maven(Map map) {
    DEFAULTS = loadDefaults()
    withMaven(maven: "${map.get('mavenVersion', DEFAULTS.mavenVersion)}") {
    sh """
        cd ${WORKSPACE}/${map.get("workDir", DEFAULTS.workDir)}
        mvn ${map.get("mavenLifecycles", DEFAULTS.mavenLifecycles)} ${map.get("options", DEFAULTS.mavenOptions)}
    """
    }
}

def docker(Map map) {
    required = ["name", "version"]
    DEFAULTS = loadDefaults()

    map = map.findAll{ it.value!= "" }
    map.dockerfilePath = map.get("dockerfilePath", "./").replaceAll("(?i)Dockerfile", "")
    required.each { item ->
        if ((map[item] == null) & (map.get("ignoreNulls", false) != true)) {
            throw new Exception("${item} is not defined")
        }
    }

    println("STARTING BUILD")
    println(map)
    sh """
      podman build ${map.get("options", " -t ${map.name}")} ${map.get("dockerfilePath", "./")}
      podman tag ${map.name} ${map.get("registryURL", DEFAULTS.registryURL)}/${map.get("name")}:latest
      podman tag ${map.name} ${map.get("registryURL", DEFAULTS.registryURL)}/${map.get("name")}:${map.get("version", "badTag")}.${BUILD_NUMBER}
    """
    if (map.get("registryPush", false) == true) {
        sh """
        podman push --tls-verify=${map.get("registryTLS", DEFAULTS.registryTLS)} ${map.get("registryURL", DEFAULTS.registryURL)}/${map.get("name")}:latest
        podman push --tls-verify=${map.get("registryTLS", DEFAULTS.registryTLS)} ${map.get("registryURL", DEFAULTS.registryURL)}/${map.get("name")}:${map.get("version", "badTag")}.${BUILD_NUMBER}
        """
    }
}