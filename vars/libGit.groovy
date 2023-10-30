def checkout(Map map) {
    required = ["repo", "branches"]
    DEFAULTS = loadDefaults()

    map = map.findAll{ it.value!= "" }
    required.each { item ->
        if (map[item] == null) {
            throw new Exception("${item} is not defined")
        }
    }

    checkout scmGit(
        branches: [[name: map.get("branches")]],
        userRemoteConfigs: [[
            credentialsId: map.get("gitCredential", DEFAULTS.gitCredential),
            url: "git@${map.get('gitURL', DEFAULTS.gitURL)}:${map.get('repo')}"
        ]]
    )
}