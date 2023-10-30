def call() {
    return [
        workDir: "./",
        gitURL: "x.y.z.250",
        gitCredential: "git_ssh_key",
        registryURL: "x.y.z.1:5000",
        registryTLS: "false",
        mavenVersion: "393",
        mavenLifecycles: "clean package",
        mavenOptions: "",
        rustCommand: "build",
        rustOptions: "--release",
        goOptions: "build .",
    ]
}