![Magma](https://img.hexeption.co.uk/magma-new.png)

# Magma

![](https://img.shields.io/badge/Minecraft%20Forge-1.12.2%20--%202847-orange.svg?style=for-the-badge) [![](https://img.shields.io/jenkins/build/https/ci.hexeption.co.uk/job/Magma?label=CI&style=for-the-badge)](https://ci.hexeption.co.uk)

### What's Magma?

Magma next  generation in hybrid servers.

Magma is based on Forge and Paper builds, meaning it can run both Craftbukkit/Spigot/Paper-based plugins and forge-based mods.

We hope to eliminate all issues with craftbukkit forge servers. In the end, we envision a seamless, low lag Magma experience with support for new 1.12+ versions of Minecraft.

## Deployment

### Installation

1. Download the recommended builds from the [**Releases** section](https://github.com/KettleFoundation/Kettle/releases)
   1. Download Beta builds from the [**CI**](https://ci.hexeption.co.uk/job/Magma/)
2. Make a new folder for the server
3. Move the jar to the folder
4. Run the jar

### Building the sources

All builds are available in `build/distributions`

- Clone the Project
  - You can use GitHub Desktop/GitKraken or clone using the terminal 
    - `git clone https://github.com/MagmaFoundation/Magma` 
  - Next you are gonna want to clone the submodule
    - `git submodule update --init --recursive` 
- Building
  - First you want to run the build command 
    - `./gradlew launch4j`
  - Now go and get a drink this may take some time

### Contribute to Magma
If you wish to actually inspect Magma, submit PRs or otherwise work with Magma itself, you're in the right place!.

Please read the [CONTRIBUTING.md](https://github.com/magmafoundation/Magma/blob/master/CONTRIBUTING.md) to see how to contribute and how to setup and run.


## Chat

You are welcome to visit Magma Discord server [here](https://discord.gg/6rkqngA).

## Unstable/Test builds

For unstable/test builds you can check the [__CI__](https://ci.hexeption.co.uk/job/Magma)
