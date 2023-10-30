def call(Map map) {
    required = ["type"]

    map = map.findAll{ it.value!= "" }
    required.each { item ->
        if (map[item] == null) {
            throw new Exception("${item} is not defined")
        }
    }

    println("STARTING ${map.type} BUILD")
    println("RUN VARIABLES:\n ${map}")

    switch(map.type) {
        case "maven":
        case "mvn":
            builds.maven(map)
            break
        case "docker":
        case "podman":
            builds.docker(map)
            break
    }
}
